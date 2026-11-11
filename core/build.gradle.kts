// :core —— 应用外壳（shell/**）：顶栏 + 当前页 + 底栏 / 侧栏 + 玻璃采样层。
// 上游这里还 api 转发全部 feature 与网络栈；lite 版只留壳本身用到的共享层。
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