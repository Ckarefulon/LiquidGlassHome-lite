package com.ckarefulon.liquidglasshome.lite

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import com.ckarefulon.liquidglasshome.home.PlaceholderServices
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

/**
 * 底栏四格：**主页 / 占位 / 占位 / 设置**。
 *
 * 模板刻意只留四个位置：一个真页面 + 两个占位 + 一个设置页，使用方把自己的页面填进
 * 那两个占位格里即可（改这个 enum + `page` 分支）。
 */
private enum class LiteTab(val label: String, val icon: ImageVector, val iconSize: Dp, val target: ShellTarget) {
    HOME("主页", Icons.Filled.Home, 24.dp, ShellTarget.App(AppRoute.Main)),
    SLOT_A("占位一", Icons.Filled.Circle, 22.dp, ShellTarget.Custom("slot_a")),
    SLOT_B("占位二", Icons.Filled.Star, 22.dp, ShellTarget.Custom("slot_b")),
    SETTINGS("设置", Icons.Filled.Settings, 22.dp, ShellTarget.Custom("settings"));
}

/** 盖满整屏的占位浮层：主页点开的功能入口都落在这里。 */
private data class LiteOverlay(val title: String)

@Composable
fun LiteMainScreen(glassStyle: Boolean) {
    val tabs = remember { ShellTabs(LiteTab.entries.map { it.shellTabFor() }) }
    var selectedId by rememberSaveable { mutableStateOf(LiteTab.HOME.name) }
    val selected = tabs.byId(selectedId) ?: tabs.first
    var overlay by remember { mutableStateOf<LiteOverlay?>(null) }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    var showSettings by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val haptics = rememberHaptics()

    val onNavigate: (AppRoute) -> Unit = remember {
        { route -> overlay = LiteOverlay(PlaceholderServices.titleOfRoute(route)) }
    }
    val onOpenSettings: () -> Unit = remember { { showSettings = true } }

    val nav = ShellNavState(
        tabs = tabs,
        target = selected.target,
        onSelect = { tab ->
            if (tab.id != selectedId) haptics.tick()
            selectedId = tab.id
        },
    )

    // 主页设置抽屉挂在这里：顶栏齿轮与「设置」格打开的是同一个抽屉
    HomeSettingsHost(show = showSettings, onDismiss = { showSettings = false })

    AppShell(
        nav = nav,
        glassStyle = glassStyle,
        bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
        contentWindowInsets = if (isWideLayout()) {
            WindowInsets.systemBars.union(
                WindowInsets.displayCutout.exclude(WindowInsets.displayCutout.only(WindowInsetsSides.Start)),
            )
        } else {
            WindowInsets.systemBars
                .union(WindowInsets.displayCutout)
                .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
        },
        topBar = { _, _ ->
            // 首页自带顶栏（左右各一排圆钮），其余各格用大标题顶栏
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
                showSettings -> showSettings = false
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
                    onOpenSettings = onOpenSettings,
                )
                // 「设置」格：直接是一个占位页 + 一个进入主页设置抽屉的入口
                LiteTab.SETTINGS.name -> SettingsPage(
                    bottomReserve = insets.bottomBarReserve,
                    contentTopPadding = insets.contentTopPadding,
                    onOpenSettings = onOpenSettings,
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

/** 底栏格 → 壳的 tab。 */
private fun LiteTab.shellTabFor() =
    ShellTab(id = name, label = label, icon = icon, iconSize = iconSize, target = target)
