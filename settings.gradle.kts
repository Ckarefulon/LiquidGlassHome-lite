pluginManagement {
    // 约定插件：toolbox.kmp.library / toolbox.kmp.compose（与上游 xjtu-toolbox-android 同一套）
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LiquidGlassHome"

include(":app")
// 三端共用的应用外壳（顶栏 + 底栏 / 侧栏 + 玻璃采样层）
include(":core")
// vendor 进来的 kyant backdrop（见 thirdparty/backdrop/README.md）；根 build.gradle.kts 用
// dependencySubstitution 把 io.github.kyant0:backdrop 顶替成它，调用点不用改。
include(":thirdparty:backdrop")

include(":shared:model")
include(":shared:navigation")
include(":shared:designsystem")
include(":feature:home")
// 只保留主页设置抽屉（HomeSettingsSheet / HomeBackgroundPreference / SettingsIcon）
include(":feature:settings")