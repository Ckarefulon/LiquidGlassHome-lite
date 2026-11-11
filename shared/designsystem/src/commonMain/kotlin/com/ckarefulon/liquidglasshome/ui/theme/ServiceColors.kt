package com.ckarefulon.liquidglasshome.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 功能入口的图标配色。
 *
 * 作为可复用模板，图标背景一律取**主题色**，不按功能 id 散成十几种固定色——
 * 使用方换了主题色，整页图标跟着变。主题色默认亮色 `#24f0ea` / 深色 `#5d4ea8`
 * （见 `SettingsStore.DEFAULT_THEME_COLOR_LIGHT` / `_DARK`）。
 */
@Composable
fun serviceColor(@Suppress("UNUSED_PARAMETER") key: String): Color = MiuixTheme.colorScheme.primary
