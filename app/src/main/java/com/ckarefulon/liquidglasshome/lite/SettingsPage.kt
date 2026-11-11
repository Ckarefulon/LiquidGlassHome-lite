package com.ckarefulon.liquidglasshome.lite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ckarefulon.liquidglasshome.settings.HomeSettingsSheet
import com.ckarefulon.liquidglasshome.settings.SettingsStore
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme

/** 主页设置抽屉的宿主：顶栏齿轮与「设置」格共用同一个抽屉。 */
@Composable
fun HomeSettingsHost(show: Boolean, onDismiss: () -> Unit) {
    HomeSettingsSheet(
        show,
        onDismiss = onDismiss,
        showTopBarStyle = true,
    )
}

/**
 * 底栏「设置」格。
 *
 * 模板不实现完整的设置页，只给一个入口 + 一句说明：点按钮打开主页设置抽屉
 * （里面能调主题色、背景、液态玻璃、网格、卡片圆角、布局导入导出）。
 * 使用方把它换成自己的设置页即可。
 */
@Composable
fun SettingsPage(
    bottomReserve: Dp,
    contentTopPadding: Dp,
    onOpenSettings: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = contentTopPadding)
            .padding(bottom = bottomReserve)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(Modifier.height(24.dp))
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MiuixTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "设置",
            style = MiuixTheme.textStyles.title4,
            fontWeight = FontWeight.Bold,
            color = MiuixTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "这个格子是占位页。主页相关的外观设置都在主页设置抽屉里。",
            style = MiuixTheme.textStyles.body2,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        )
        Spacer(Modifier.height(20.dp))
        TextButton(text = "打开主页设置", onClick = onOpenSettings)
        Spacer(Modifier.height(24.dp))
        Text(
            "主题色默认：亮色 " + SettingsStore.DEFAULT_THEME_COLOR_LIGHT +
                " · 深色 " + SettingsStore.DEFAULT_THEME_COLOR_DARK,
            style = MiuixTheme.textStyles.footnote1,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
        )
        Box(Modifier.fillMaxWidth())
    }
}
