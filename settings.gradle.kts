pluginManagement {
    // 约定插件：toolbox.kmp.library / toolbox.kmp.compose（与上游项目同一套）
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
    // FAIL_ON_PROJECT_REPOS：子工程再声明仓库会当场报错，不会像 PREFER_SETTINGS 那样静默忽略
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LiquidGlassHome-lite"

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