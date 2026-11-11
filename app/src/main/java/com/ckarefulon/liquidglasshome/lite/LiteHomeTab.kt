package com.ckarefulon.liquidglasshome.lite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import com.ckarefulon.liquidglasshome.auth.LoginType
import com.ckarefulon.liquidglasshome.home.HomeScreen
import com.ckarefulon.liquidglasshome.home.LocalHomeBarBackdrop
import com.ckarefulon.liquidglasshome.nav.AppRoute
import com.ckarefulon.liquidglasshome.platform.BackHandler
import com.ckarefulon.liquidglasshome.settings.HomeLayoutRevision
import com.ckarefulon.liquidglasshome.settings.HomeSettingsSheet
import com.ckarefulon.liquidglasshome.settings.SettingsStore
import com.ckarefulon.liquidglasshome.shell.ShellPageInsets
import com.ckarefulon.liquidglasshome.ui.components.pressScale
import com.ckarefulon.liquidglasshome.ui.theme.LocalIsDarkTheme
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.theme.MiuixTheme

/** 首页这一格：占位宿主 + 主页设置抽屉 + 子系统状态弹窗 + 顶栏圆钮。 */
@Composable
fun LiteHomeTab(
    insets: ShellPageInsets,
    onNavigate: (AppRoute) -> Unit,
    onOpenPlaceholder: (String) -> Unit,
    onProfile: () -> Unit,
) {
    var showStatus by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    val host = remember {
        LiteHomeHost(
            onNavigate = onNavigate,
            onProfileTap = onProfile,
            onOpenStatus = { showStatus = true },
            onOpenSettings = { showSettings = true },
        )
    }

    LiteStatusSheet(show = showStatus, onDismiss = { showStatus = false })
    HomeSettingsSheet(
        showSettings,
        onDismiss = { showSettings = false },
        showTopBarStyle = true,
    )

    HomeScreen(
        host = host,
        extraBottomPadding = insets.bottomBarReserve,
        contentTopPadding = insets.contentTopPadding,
        contentTopShift = insets.contentTopShift,
        topLeading = {
            // 「功能按钮统一右对齐」关掉时，扫码单独站到最左边（与上游同一套排法）
            if (!rememberTopActionsRight()) {
                HomeScanAction(onScan = { onOpenPlaceholder("扫一扫") })
            }
        },
        topActions = {
            HomeTopActions(
                onInbox = { onNavigate(AppRoute.Inbox) },
                onScan = { onOpenPlaceholder("扫一扫") },
                onSearch = { onOpenPlaceholder("搜索") },
                withScan = rememberTopActionsRight(),
            )
        },
    )
}

/** 读在槽位内部：拨完开关回到首页，顶栏当场换排法（与上游 MainScreen 同一条注释的道理）。 */
@Composable
private fun rememberTopActionsRight(): Boolean {
    val revision = HomeLayoutRevision.current
    val store = remember { SettingsStore() }
    return remember(store, revision) { store.homeTopActionsRight }
}

@Composable
private fun HomeScanAction(onScan: () -> Unit) {
    val tint = MiuixTheme.colorScheme.onSurface
    HomeRoundAction(onClick = onScan) {
        Icon(Icons.Filled.QrCodeScanner, contentDescription = "扫一扫", tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun HomeTopActions(
    onInbox: () -> Unit,
    onScan: () -> Unit,
    onSearch: () -> Unit,
    withScan: Boolean,
) {
    val tint = MiuixTheme.colorScheme.onSurface
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeRoundAction(onClick = onSearch) {
            Icon(Icons.Filled.Search, contentDescription = "搜索", tint = tint, modifier = Modifier.size(21.dp))
        }
        if (withScan) HomeScanAction(onScan = onScan)
        // 铃铛：占位红点画在圆钮外面这一层（圆钮的玻璃会把内容裁成圆形）
        Box {
            HomeRoundAction(onClick = onInbox) {
                Icon(Icons.Filled.Notifications, contentDescription = "消息", tint = tint, modifier = Modifier.size(22.dp))
            }
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 4.dp)
                    .size(9.dp)
                    .background(Color(0xFFE53935), CircleShape),
            )
        }
    }
}

private val HomeTopActionSize = 44.dp

/**
 * 顶栏圆钮：卡片组布局下采首页递过来的内容层（模糊 + 活力 + 高光 + 折射透镜）；
 * 经典布局不递这一层，退回半透明底色圆钮。画法与上游 MainScreen 的 HomeRoundAction 一致。
 */
@Composable
private fun HomeRoundAction(
    onClick: (() -> Unit)?,
    size: Dp = HomeTopActionSize,
    content: @Composable () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val isDark = LocalIsDarkTheme.current
    val liquid = LocalHomeBarBackdrop.current
    val fill = if (isDark) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.4f)
    Box(
        Modifier
            .size(size)
            .pressScale(interaction, pressed = 0.92f)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                clip = false,
                ambientColor = Color.White.copy(alpha = if (isDark) 0.28f else 0.6f),
                spotColor = Color.White.copy(alpha = if (isDark) 0.28f else 0.6f),
            )
            .then(
                if (liquid != null) {
                    Modifier.drawBackdrop(
                        backdrop = liquid,
                        shape = { CircleShape },
                        effects = {
                            padding = maxOf(padding, 8.dp.toPx())
                            vibrancy()
                            blur(6.dp.toPx())
                            lens(8.dp.toPx(), 10.dp.toPx())
                        },
                        highlight = { Highlight.Default.copy(alpha = 0.65f) },
                        shadow = null,
                        onDrawSurface = { drawRect(fill) },
                    )
                } else {
                    Modifier.background(
                        MiuixTheme.colorScheme.surfaceContainerHigh.copy(alpha = if (isDark) 0.52f else 0.6f),
                        CircleShape,
                    )
                },
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interaction, indication = null, onClick = onClick)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) { content() }
}

private val STATUS_GREEN = Color(0xFF2E7D32)

/** 子系统连接明细（占位：全部已连接）。 */
@Composable
private fun LiteStatusSheet(show: Boolean, onDismiss: () -> Unit) {
    if (!show) return
    BackHandler(onBack = onDismiss)
    OverlayBottomSheet(show = true, title = "子系统连接状态", onDismissRequest = onDismiss) {
        Column(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .heightIn(max = 460.dp)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            LoginType.entries.forEach { t ->
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = STATUS_GREEN,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(t.label, style = MiuixTheme.textStyles.body1, fontWeight = FontWeight.Medium)
                        Text(t.description, style = MiuixTheme.textStyles.footnote1, color = MiuixTheme.colorScheme.onSurfaceVariantSummary)
                    }
                    Text("已连接（占位）", style = MiuixTheme.textStyles.footnote1, color = STATUS_GREEN)
                }
            }
        }
    }
}