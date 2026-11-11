// Top-level build file：插件版本在根工程钉死，子工程只用 bare id。
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
}

// vendor 进来的 backdrop 顶替 Maven 上的 io.github.kyant0:backdrop：`toolbox.kmp.compose`
// 仍然 `api` 那条坐标，这里统一换成 :thirdparty:backdrop，于是所有 `drawBackdrop`
// 调用点一行都不用改。顶替只在这一处声明，别在子工程里再写一遍。
allprojects {
    // 本模块自己就是实现，不能再被顶替成「依赖自己」
    if (path == ":thirdparty:backdrop") return@allprojects
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            substitute(module("io.github.kyant0:backdrop"))
                .using(project(":thirdparty:backdrop"))
                .because("本地 vendor：为「不采样」档加 sampling 开关，视觉代码与上游 2.0.1 一致")
        }
    }
}