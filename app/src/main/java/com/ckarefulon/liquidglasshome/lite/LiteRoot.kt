package com.ckarefulon.liquidglasshome.lite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.ckarefulon.liquidglasshome.settings.HomeLayoutRevision
import com.ckarefulon.liquidglasshome.settings.SettingsStore
import com.ckarefulon.liquidglasshome.ui.LocalIsWideLayout
import com.ckarefulon.liquidglasshome.ui.calculateIsWideLayout
import com.ckarefulon.liquidglasshome.ui.glass.LocalGlassStyle
import com.ckarefulon.liquidglasshome.ui.theme.LiquidGlassHomeTheme

/** 主题 + 全局 Local。 */
@Composable
fun LiteRoot() {
    val store = remember { SettingsStore() }
    // 主页设置抽屉改完偏好会 bump 这个信号：风格 / 明暗 / 主题色在这里重读，不用重开整页
    val revision = HomeLayoutRevision.current
    val glassStyle = remember(revision) { store.navBarStyle == SettingsStore.NAV_STYLE_FLOATING }
    val darkMode = remember(revision) { store.darkMode }
    // 主题色：默认亮 #24F0EA / 暗 #5D4EA8，可在「主页设置 → 主题色」里改
    val customLight = remember(revision) { store.customColorHex(dark = false) }
    val customDark = remember(revision) { store.customColorHex(dark = true) }
    val useCustom = remember(revision) { store.customColorEnabled }
    LiquidGlassHomeTheme(
        darkModeOverride = darkMode,
        customColorHex = if (useCustom) customLight else "",
        customDarkColorHex = if (useCustom) customDark else "",
    ) {
        CompositionLocalProvider(
            LocalIsWideLayout provides calculateIsWideLayout(),
            LocalGlassStyle provides glassStyle,
        ) {
            LiteMainScreen(glassStyle = glassStyle)
        }
    }
}
