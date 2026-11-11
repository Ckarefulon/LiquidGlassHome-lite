package com.ckarefulon.liquidglasshome.lite

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ckarefulon.liquidglasshome.home.AppServices
import com.ckarefulon.liquidglasshome.nav.AppRoute
import com.ckarefulon.liquidglasshome.shell.AppShell
import com.ckarefulon.liquidglasshome.shell.ShellNavState
import com.ckarefulon.liquidglasshome.shell.ShellTab
import com.ckarefulon.liquidglasshome.shell.ShellTabs
import com.ckarefulon.liquidglasshome.shell.ShellTarget
import com.ckarefulon.liquidglasshome.ui.glass.topBarScrim
import com.ckarefulon.liquidglasshome.ui.isWideLayout
import com.ckarefulon.liquidglasshome.ui.rememberHaptics
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.theme.MiuixTheme

/** 底栏五格（与上游默认排布一致）：首页 / 日程 / 小岱 / 学辅 / 我的；除首页外都是占位页。 */
private enum class LiteTab(val label: String, val icon: ImageVector, val iconSize: Dp, val target: ShellTarget) {
    HOME("首页", Icons.Filled.Home, 24.dp, ShellTarget.App(AppRoute.Main)),
    COURSES("日程", Icons.Filled.CalendarMonth, 23.dp, ShellTarget.App(AppRoute.Schedule)),
    PIDAI("小岱", Icons.Filled.SmartToy, 27.dp, ShellTarget.Custom("pidai")),
    TOOLS("学辅", Icons.AutoMirrored.Filled.MenuBook, 22.dp, ShellTarget.Custom("zyxf")),
    PROFILE("我的", Icons.Filled.Person, 27.dp, ShellTarget.Custom("profile"));

    fun shellTab() = ShellTab(id = name, label = label, icon = icon, iconSize = iconSize, target = target)
}

/** 盖满整屏的占位浮层：首页点开的功能入口、扫一扫、搜索都落在这里。 */
private data class LiteOverlay(val title: String)

@Composable
fun LiteMainScreen(glassStyle: Boolean) {
    val tabs = remember { ShellTabs(LiteTab.entries.map { it.shellTab() }) }
    var selectedId by rememberSaveable { mutableStateOf(LiteTab.HOME.name) }
    val selected = tabs.byId(selectedId) ?: tabs.first
    var overlay by remember { mutableStateOf<LiteOverlay?>(null) }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    val context = LocalContext.current
    val haptics = rememberHaptics()

    val onNavigate: (AppRoute) -> Unit = remember {
        { route -> overlay = LiteOverlay(titleOf(route)) }
    }
    val onOpenPlaceholder: (String) -> Unit = remember {
        { title -> overlay = LiteOverlay(title) }
    }
    val onProfile: () -> Unit = remember {
        { selectedId = LiteTab.PROFILE.name }
    }

    val nav = ShellNavState(
        tabs = tabs,
        target = selected.target,
        onSelect = { tab ->
            if (tab.id != selectedId) haptics.tick()
            selectedId = tab.id
        },
    )

    AppShell(
        nav = nav,
        glassStyle = glassStyle,
        bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
        contentWindowInsets = if (isWideLayout()) {
            WindowInsets.systemBars.union(
                WindowInsets.displayCutout.exclude(WindowInsets.displayCutout.only(WindowInsetsSides.Start)),
            )
        } else {
            // 手机：内容铺到屏幕底边，从小白条和玻璃底栏下面滚过；末尾净空由各页自己补
            WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
        },
        topBar = { _, _ ->
            // 首页没有顶栏（刊头在页面里）；其余各格画一个大标题顶栏
            if (selected.id != LiteTab.HOME.name) {
                TopAppBar(
                    color = if (glassStyle) Color.Transparent else MiuixTheme.colorScheme.surface,
                    modifier = if (glassStyle) Modifier.topBarScrim() else Modifier,
                    title = selected.label,
                    largeTitle = selected.label,
                )
            }
        },
        onBack = {
            when {
                overlay != null -> overlay = null
                selectedId != LiteTab.HOME.name -> selectedId = LiteTab.HOME.name
                else -> {
                    val now = System.currentTimeMillis()
                    if (now - lastBackPressTime < 2000) {
                        (context as? Activity)?.finishAffinity()
                    } else {
                        lastBackPressTime = now
                        Toast.makeText(context, "再按一次返回退出", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        },
        overlays = {
            overlay?.let { PlaceholderOverlay(title = it.title, onBack = { overlay = null }) }
        },
        page = { shellPage, insets ->
            when (shellPage.tab?.id) {
                LiteTab.HOME.name -> LiteHomeTab(
                    insets = insets,
                    onNavigate = onNavigate,
                    onOpenPlaceholder = onOpenPlaceholder,
                    onProfile = onProfile,
                )
                else -> PlaceholderPage(
                    title = shellPage.tab?.label.orEmpty(),
                    bottomReserve = insets.bottomBarReserve,
                    contentTopPadding = insets.contentTopPadding,
                )
            }
        },
    )
}

/** 功能入口的占位页标题：服务目录里有名字就用目录的，没有就用路由 id。 */
private fun titleOf(route: AppRoute): String =
    AppServices.all.firstOrNull { it.route.id == route.id }?.title ?: route.id