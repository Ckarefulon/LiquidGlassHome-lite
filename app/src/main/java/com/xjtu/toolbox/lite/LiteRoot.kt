package com.xjtu.toolbox.lite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.xjtu.toolbox.settings.HomeLayoutRevision
import com.xjtu.toolbox.settings.SettingsStore
import com.xjtu.toolbox.ui.LocalIsWideLayout
import com.xjtu.toolbox.ui.calculateIsWideLayout
import com.xjtu.toolbox.ui.glass.LocalGlassStyle
import com.xjtu.toolbox.ui.theme.XJTUToolBoxTheme

/** 主题 + 全局 Local：与上游 AppRoot 同一套缝，只是没有登录态与一镜到底。 */
@Composable
fun LiteRoot() {
    val store = remember { SettingsStore() }
    // 主页设置抽屉改完偏好会 bump 这个信号：风格 / 明暗在这里重读，不用重开整页
    val revision = HomeLayoutRevision.current
    val glassStyle = remember(revision) { store.navBarStyle == SettingsStore.NAV_STYLE_FLOATING }
    val darkMode = remember(revision) { store.darkMode }
    XJTUToolBoxTheme(darkModeOverride = darkMode) {
        CompositionLocalProvider(
            LocalIsWideLayout provides calculateIsWideLayout(),
            LocalGlassStyle provides glassStyle,
        ) {
            LiteMainScreen(glassStyle = glassStyle)
        }
    }
}