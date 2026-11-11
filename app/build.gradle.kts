plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.xjtu.toolbox.lite"
    // compileSdk 跟随 miuix（其 AAR metadata 声明 minCompileSdk = 37）。
    compileSdk {
        version = release(37) { }
    }

    defaultConfig {
        applicationId = "com.xjtu.toolbox.lite"
        minSdk = 31
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            // 展示用仓库：不混淆，崩溃堆栈直接可读。
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