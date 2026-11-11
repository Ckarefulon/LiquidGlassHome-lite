package com.ckarefulon.liquidglasshome.lite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ckarefulon.liquidglasshome.home.HomeScreen
import com.ckarefulon.liquidglasshome.home.LocalHomeBarBackdrop
import com.ckarefulon.liquidglasshome.nav.AppRoute
import com.ckarefulon.liquidglasshome.shell.ShellPageInsets
import com.ckarefulon.liquidglasshome.ui.components.pressScale
import com.ckarefulon.liquidglasshome.ui.theme.LocalIsDarkTheme
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.effects.vibrancy
import com.kyant.backdrop.highlight.Highlight
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 主页这一格。
 *
 * 顶栏是**左右都有**的那种：左边「菜单」一个圆钮，右边「搜索 + 通知」两个圆钮，
 * 通知角标只挂 1 个。中间不画标题——刊头已经去掉了，页面上只留功能宫格。
 */
@Composable
fun LiteHomeTab(
    insets: ShellPageInsets,
    onNavigate: (AppRoute) -> Unit,
    onOpenSettings: () -> Unit,
) {
    // 不用 remember 包 host：remember 会记住第一次组合的闭包，里面捕获的 onOpenSettings /
    // onNavigate 是旧快照，于是「点齿轮没反应」。构造 host 很便宜，每次组合新建即可。
    val host = LiteHomeHost(onNavigate = onNavigate, onOpenSettings = onOpenSettings)
    HomeScreen(
        host = host,
        extraBottomPadding = insets.bottomBarReserve,
        contentTopPadding = insets.contentTopPadding,
        contentTopShift = insets.contentTopShift,
        topLeading = {
            // 左边只有「扫码」
            HomeRoundAction(onClick = { onNavigate(AppRoute.Notification) }) {
                Icon(
                    Icons.Filled.QrCodeScanner,
                    contentDescription = "扫一扫",
                    tint = MiuixTheme.colorScheme.onSurface,
                    modifier = Modifier.size(21.dp),
                )
            }
        },
        topActions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 右边两颗：搜索 + 通知（通知红点默认挂 1 个）
                HomeRoundAction(onClick = { onNavigate(AppRoute.Notification) }) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "搜索",
                        tint = MiuixTheme.colorScheme.onSurface,
                        modifier = Modifier.size(21.dp),
                    )
                }
                Box {
                    HomeRoundAction(onClick = { onNavigate(AppRoute.Notification) }) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "通知",
                            tint = MiuixTheme.colorScheme.onSurface,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                    // 未读数：默认 1
                    Box(
                        Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-2).dp, y = 4.dp)
                            .size(9.dp)
                            .background(Color(0xFFE53935), CircleShape),
                    )
                }
            }
        },
    )
}

private val HomeTopActionSize = 44.dp

/**
 * 顶栏圆钮：卡片组布局下采首页递过来的内容层（模糊 + 活力 + 高光 + 折射透镜）；
 * 没有采样源时退回半透明底色圆钮。
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
