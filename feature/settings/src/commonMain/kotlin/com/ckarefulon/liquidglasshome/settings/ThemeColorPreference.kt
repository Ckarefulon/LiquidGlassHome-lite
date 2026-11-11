package com.ckarefulon.liquidglasshome.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.window.WindowDialog

/**
 * 「主题色」设置。
 *
 * 两档各一个色板：**亮色档默认 `#24F0EA`、深色档默认 `#5D4EA8`**（见
 * `SettingsStore.DEFAULT_THEME_COLOR_LIGHT` / `_DARK`）。这一份主题色同时决定
 * 主题色本身与**功能图标的背景色**（`serviceColor` 直接取 `colorScheme.primary`）。
 *
 * 关掉开关 = 回到 MIUIX 自带配色，不动用户选过的色（重新打开即恢复）。
 */
@Composable
internal fun ThemeColorPreference(startAction: @Composable () -> Unit) {
    val settings = remember { SettingsStore() }
    val revision = HomeLayoutRevision.current
    var enabled by remember(revision) { mutableStateOf(settings.customColorEnabled) }
    var lightHex by remember(revision) { mutableStateOf(settings.customColorHex(dark = false)) }
    var darkHex by remember(revision) { mutableStateOf(settings.customColorHex(dark = true)) }
    var picking by remember { mutableStateOf<String?>(null) }

    SwitchPreference(
        title = "使用自定义主题色",
        summary = "主题色与功能图标背景都取它；关掉则回到内置配色",
        checked = enabled,
        startAction = startAction,
        onCheckedChange = {
            enabled = it
            settings.customColorEnabled = it
            HomeLayoutRevision.bump()
        },
    )

    ThemeColorRow(
        title = "亮色",
        hex = lightHex,
        onPick = { picking = "light" },
    )
    ThemeColorRow(
        title = "深色",
        hex = darkHex,
        onPick = { picking = "dark" },
    )
    ResetThemeColorRow {
        val l = SettingsStore.DEFAULT_THEME_COLOR_LIGHT
        val d = SettingsStore.DEFAULT_THEME_COLOR_DARK
        lightHex = l
        darkHex = d
        settings.setCustomColorHex(dark = false, value = l)
        settings.setCustomColorHex(dark = true, value = d)
        HomeLayoutRevision.bump()
    }

    picking?.let { which ->
        val isDark = which == "dark"
        ThemeColorPickerDialog(
            initial = if (isDark) darkHex else lightHex,
            onDismiss = { picking = null },
            onConfirm = { hex ->
                if (isDark) {
                    darkHex = hex
                    settings.setCustomColorHex(dark = true, value = hex)
                } else {
                    lightHex = hex
                    settings.setCustomColorHex(dark = false, value = hex)
                }
                picking = null
                HomeLayoutRevision.bump()
            },
        )
    }
}

/** 一行：色块 + 名字 + 当前色号；点整行打开取色器。 */
@Composable
private fun ThemeColorRow(title: String, hex: String, onPick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onPick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(24.dp)
                .background(parseHex(hex) ?: Color.Gray, CircleShape),
        )
        Spacer(Modifier.width(12.dp))
        Text(title, style = MiuixTheme.textStyles.body1, modifier = Modifier.weight(1f))
        Text(
            hex.uppercase(),
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        )
    }
}

@Composable
private fun ResetThemeColorRow(onReset: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("恢复默认主题色", style = MiuixTheme.textStyles.body1, modifier = Modifier.weight(1f))
        TextButton(onClick = onReset) {
            Text(
                "恢复",
                style = MiuixTheme.textStyles.footnote1,
                color = MiuixTheme.colorScheme.primary,
            )
        }
    }
}

/** `#RRGGBB` / `RRGGBB` → Color；解析不出来返回 null。 */
internal fun parseHex(hex: String): Color? {
    val h = hex.trim().removePrefix("#")
    if (h.length != 6) return null
    val v = h.toLongOrNull(16) ?: return null
    return Color(0xFF000000 or v)
}

/** 取色器：一排预设 + 十六进制输入。模板够用，不引整套取色面板。 */
@Composable
private fun ThemeColorPickerDialog(
    initial: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf(initial.trim().removePrefix("#").uppercase()) }
    val preview = parseHex(text) ?: Color.Gray
    val presets = listOf(
        "24F0EA", "5D4EA8", "2F7CFF", "00BFA5",
        "7E57C2", "EC407A", "FFA726", "26A69A",
    )
    WindowDialog(show = true, title = "主题色", onDismissRequest = onDismiss) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(40.dp).background(preview, CircleShape))
                Spacer(Modifier.width(12.dp))
                BasicTextField(
                    value = text,
                    onValueChange = { text = it.uppercase().filter { c -> c in "0123456789ABCDEF" }.take(6) },
                    textStyle = TextStyle(
                        color = MiuixTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .background(MiuixTheme.colorScheme.surfaceContainerHigh, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                )
            }
            Spacer(Modifier.height(16.dp))
            presets.chunked(4).forEach { row ->
                Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    row.forEach { p ->
                        Box(
                            Modifier
                                .padding(end = 10.dp)
                                .size(36.dp)
                                .background(parseHex(p) ?: Color.Gray, CircleShape)
                                .clickable { text = p },
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onDismiss) { Text("取消") }
                TextButton(onClick = {
                    val normalized = SettingsStore.normalizeThemeColor(text)
                    if (normalized.isNotEmpty()) onConfirm(normalized)
                }) { Text("确定") }
            }
        }
    }
}
