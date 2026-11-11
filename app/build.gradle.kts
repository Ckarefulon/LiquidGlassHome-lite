import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

/**
 * 发布签名。
 *
 * CI（GitHub Actions）把 keystore 的 base64 与口令塞进环境变量；本地开发机可以放一份
 * `keystore.properties`（已在 .gitignore 里）指向自己的 keystore。
 * 两处都没有时保持**不签名**——APK 仍然产出，但装不上，`./gradlew` 会打印一行提示。
 */
val keystoreProps = Properties().apply {
    val f = rootProject.file("keystore.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}

fun envOrProp(key: String, propKey: String = key): String? =
    System.getenv(key)?.takeIf { it.isNotBlank() } ?: keystoreProps.getProperty(propKey)?.takeIf { it.isNotBlank() }

val releaseStoreFile: String? = envOrProp("LIQUIDGLASS_KEYSTORE_FILE") ?: keystoreProps.getProperty("storeFile")
val releaseStorePassword: String? = envOrProp("LIQUIDGLASS_KEYSTORE_PASSWORD", "storePassword")
val releaseKeyAlias: String? = envOrProp("LIQUIDGLASS_KEY_ALIAS", "keyAlias")
val releaseKeyPassword: String? = envOrProp("LIQUIDGLASS_KEY_PASSWORD", "keyPassword")
val hasReleaseSigning = releaseStoreFile != null && releaseStorePassword != null &&
    releaseKeyAlias != null && releaseKeyPassword != null &&
    file(releaseStoreFile).exists()

android {
    namespace = "com.ckarefulon.liquidglasshome.lite"
    // compileSdk 跟随 miuix（其 AAR metadata 声明 minCompileSdk = 37）。
    compileSdk {
        version = release(37) { }
    }

    defaultConfig {
        applicationId = "com.ckarefulon.liquidglasshome"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = file(releaseStoreFile!!)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        release {
            // material-icons-extended 有 2000+ 个图标 composable，全量进包能到 60MB+。
            // 只展示主页不需要全部图标，必须靠 R8 裁掉用不到的那些。
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            if (hasReleaseSigning) signingConfig = signingConfigs.getByName("release")
        }
        debug {
            // 本机排查用；CI 只出 release。
            isMinifyEnabled = false
        }
    }

    // 与 miuix 各模块对齐 JDK 21（miuix-nav 的 inline 函数要求字节码目标一致）。
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
    androidResources {
        localeFilters += listOf("zh", "en")
    }
}

if (!hasReleaseSigning) {
    logger.lifecycle(
        "⚠ 没有可用的发布签名配置：release APK 会是未签名状态，装不上。" +
            "CI 里请配 LIQUIDGLASS_KEYSTORE_FILE / _PASSWORD / KEY_ALIAS / KEY_PASSWORD；" +
            "本地请放 keystore.properties。"
    )
}

dependencies {
    implementation(project(":core"))
    implementation(project(":feature:home"))
    implementation(project(":feature:settings"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material.icons)
    implementation(libs.coroutines.android)
    implementation(libs.kotlinx.serialization.json)
}
