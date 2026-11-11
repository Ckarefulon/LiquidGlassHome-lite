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
import androidx.compose.material.icons.filled.Square
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

/**
 * 底栏五格：**主页 / 占位一 / 占位二 / 占位三 / 设置**。
 *
 * 五格是液态玻璃底栏（胶囊 + 滑块）原本的设计数量：滑块宽度、染色层、指针吸附都是按
 * 「格数固定为 5」调过的，改成 4 格会让滑块与胶囊的折射层错位（表现为重影），所以保持 5 格。
 */
private enum class LiteTab(val label: String, val icon: ImageVector, val iconSize: Dp, val target: ShellTarget) {
    HOME("主页", Icons.Filled.Home, 24.dp, ShellTarget.App(AppRoute.Main)),
    SLOT_A("占位一", Icons.Filled.Circle, 22.dp, ShellTarget.Custom("slot_a")),
    SLOT_B("占位二", Icons.Filled.Star, 22.dp, ShellTarget.Custom("slot_b")),
    SLOT_C("占位三", Icons.Filled.Square, 22.dp, ShellTarget.Custom("slot_c")),
    SETTINGS("设置", Icons.Filled.Settings, 22.dp, ShellTarget.Custom("settings"));
}

/** 盖满整屏的占位浮层：主页点开的功能入口都落在这里。 */
private data class LiteOverlay(val title: String)

@Composable
fun LiteMainScreen(glassStyle: Boolean) {
    val tabs = remember { ShellTabs(LiteTab.entries.map { it.asShellTab() }) }
    var selectedId by rememberSaveable { mutableStateOf(LiteTab.HOME.name) }
    val selected = tabs.byId(selectedId) ?: tabs.first
    var overlay by remember { mutableStateOf<LiteOverlay?>(null) }
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    var showSettings by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val haptics = rememberHaptics()

    val onNavigate: (AppRoute) -> Unit = { route ->
        overlay = LiteOverlay(AppServices.all.firstOrNull { it.route.id == route.id }?.title ?: route.id)
    }
    // 注意：这几个回调**不要**用 remember 包住。remember 会把第一次组合的 lambda 记下来，
    // 里面写 showSettings 时捕获的是旧快照，于是点了「没反应」。
    val onOpenSettings: () -> Unit = { showSettings = true }

    val nav = ShellNavState(
        tabs = tabs,
        target = selected.target,
        onSelect = { tab ->
            if (tab.id != selectedId) haptics.tick()
            selectedId = tab.id
        },
    )

    // 主页设置抽屉挂在 Scaffold 内容之后的一层：放在 AppShell 外层时 OverlayBottomSheet
    // 拿不到宿主窗口，会画出「像点了但什么都没出现」。
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
        sheets = {
            // sheets 槽位在 Scaffold 内容层里，OverlayBottomSheet 能正常弹出来
            HomeSettingsHost(show = showSettings, onDismiss = { showSettings = false })
        },
        page = { shellPage, insets ->
            when (shellPage.tab?.id) {
                LiteTab.HOME.name -> LiteHomeTab(
                    insets = insets,
                    onNavigate = onNavigate,
                    onOpenSettings = onOpenSettings,
                )
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
private fun LiteTab.asShellTab() =
    ShellTab(id = name, label = label, icon = icon, iconSize = iconSize, target = target)
