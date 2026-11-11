import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

internal val Project.libs: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libs")

internal fun VersionCatalog.lib(alias: String) = findLibrary(alias).get()

/** `:shared:data` → `shared-data`：模块名 / 产物名都要全局唯一（`:data` 与 `:shared:data` 同名会撞 kotlin_module 和 jar）。 */
internal val Project.flatName: String
    get() = path.removePrefix(":").replace(':', '-')

/**
 * android（AGP 9 KMP 库插件）单端的公共配置。
 *
 * **只建 Android 目标**：本仓库是主页 UI 的展示仓库，产物只有 `:app` 这一个 APK，
 * 没有任何 jvm / wasmJs 消费方。上游那套 `kmp.jvm()` + `wasmBrowser()` 在这里只会
 * 拖慢 CI、并让一堆没人用的 actual 保持「必须能编译」的状态。
 *
 * JVM 21、compileSdk 37、minSdk 31、namespace = com.ckarefulon.liquidglasshome.<模块路径>。
 */
internal fun Project.configureKmpLibrary() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("com.android.kotlin.multiplatform.library")
        apply("org.jetbrains.kotlin.plugin.serialization")
    }

    pluginManager.apply("base")
    extensions.configure(BasePluginExtension::class.java) { archivesName.set("toolbox-$flatName") }

    val kmp = extensions.getByType(KotlinMultiplatformExtension::class.java)
    // 与 :app 对齐 JVM 21：miuix-nav 的 inline 函数要求字节码目标一致。
    kmp.jvmToolchain(21)

    val kotlinExtensions = (kmp as ExtensionAware).extensions
    val android = (kotlinExtensions.findByName("android") ?: kotlinExtensions.getByName("androidLibrary"))
        as KotlinMultiplatformAndroidLibraryTarget
    android.namespace = "com.ckarefulon.liquidglasshome." + path.removePrefix(":").replace(':', '.')
    android.compileSdk = 37
    android.minSdk = 31

    // AGP 9 的 KMP library 插件**默认不建 host test compilation**：不写这一行，
    // commonTest 里的用例既不会被编译、也不会被执行（任务名 `testAndroidHostTest`）。
    android.withHostTestBuilder {}.configure {}

    val catalog = libs
    kmp.sourceSets.getByName("commonMain").dependencies {
        implementation(catalog.lib("kotlinx-coroutines-core"))
        implementation(catalog.lib("kotlinx-serialization-json"))
        // 用 api：公开签名里到处是 LocalDate / Instant。
        api(catalog.lib("kotlinx-datetime"))
    }
    kmp.sourceSets.getByName("commonTest").dependencies {
        implementation(kotlinModule("test"))
        implementation(catalog.lib("kotlinx-coroutines-test"))
    }

    // 模块名唯一：否则两个都叫 `data` 的模块会在 APK 里撞 META-INF/*.kotlin_module。
    val moduleName = "toolbox-$flatName"
    tasks.withType(KotlinJvmCompile::class.java).configureEach {
        if (!name.contains("Test")) compilerOptions.moduleName.set(moduleName)
    }

    // 键值落盘根：每次 jvmTest 前清空。
    tasks.withType(Test::class.java).configureEach {
        if (name == "jvmTest") {
            val root = layout.buildDirectory.dir("testStoreRoot")
            doFirst { root.get().asFile.deleteRecursively() }
            systemProperty("liquidglass.keyValueRoot", root.get().asFile.absolutePath)
        }
    }
}

private fun kotlinModule(module: String) = "org.jetbrains.kotlin:kotlin-$module"
