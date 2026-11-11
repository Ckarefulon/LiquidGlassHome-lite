// :feature:settings —— lite 版只保留主页设置抽屉三件：
// HomeSettingsSheet / HomeBackgroundPreference / SettingsIcon。只依赖 :shared:*。
plugins {
    id("toolbox.kmp.library")
    id("toolbox.kmp.compose")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":shared:model"))
            api(project(":shared:navigation"))
            api(project(":shared:designsystem"))
        }
    }
}