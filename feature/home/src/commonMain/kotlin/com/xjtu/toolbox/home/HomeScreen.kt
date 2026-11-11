package com.xjtu.toolbox.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.ScrollBehavior

/**
 * 首页入口。
 *
 * 只剩卡片组这一套（原「小组件」那套早已并进它，经典分类卡在本仓库里整条移除）：
 * 位置模型 / 拖拽 / 分组用卡片组的，材质、一镜到底、付款码这些能力从小组件那边搬了过来
 * （见 [HomeCardGroupScreen] 的类头）。
 */
@Composable
fun HomeScreen(
    host: HomeHost,
    modifier: Modifier = Modifier,
    scrollBehavior: ScrollBehavior? = null,
    /** 悬浮底栏的总占位高度：底栏浮在内容之上，页面末尾得自己留出来。 */
    extraBottomPadding: Dp = 0.dp,
    /** 玻璃顶栏的高度：内容铺到顶栏下面，这段留白放进滚动内容里。经典风格为 0。 */
    contentTopPadding: Dp = 0.dp,
    /** 壳把整页上移的量（见 `ShellPageInsets.contentTopShift`）；模糊带按它补回屏幕顶边。 */
    contentTopShift: () -> Dp = { 0.dp },
    /** 顶栏最左边的按钮（`:app` 是「扫一扫」）；null = 这一端没有。 */
    topLeading: (@Composable () -> Unit)? = null,
    /** 顶栏右侧那一排（`:app` 是「搜索」+「通知」）。null = 这一端没有顶栏。 */
    topActions: (@Composable () -> Unit)? = null,
) {
    HomeCardGroupScreen(
        host = host,
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        extraBottomPadding = extraBottomPadding,
        contentTopPadding = contentTopPadding,
        contentTopShift = contentTopShift,
        topLeading = topLeading,
        topActions = topActions,
    )
}
