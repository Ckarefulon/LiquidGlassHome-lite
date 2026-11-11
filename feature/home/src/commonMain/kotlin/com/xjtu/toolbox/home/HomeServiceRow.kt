package com.xjtu.toolbox.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.xjtu.toolbox.nav.SeamlessCardSource

/**
 * 首页服务条目：卡片组里每一块「面」的数据。
 *
 * 曾经和经典分类卡放在同一个文件里；经典分类卡移除后，这个模型仍被
 * [HomeCardGroupScreen] / [HomeTileBoard] / [HomeAddTileOrb] / [HomeQuickActions] 共用，
 * 所以单独留在这里。
 */
@androidx.compose.runtime.Immutable
internal data class HomeServiceRow(
    val key: String,
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val color: Color,
    val onClick: () -> Unit,
    /** 这一块的几何起点交给宿主：ROM 支持 SharedViewKit 时走独立 Activity 的系统转场。 */
    val onSeamlessClick: ((SeamlessCardSource) -> Unit)? = null,
    val stat: String? = null,
    val statDetail: String? = null,
    val tone: HomeTone = HomeTone.NORMAL,
    /** 大尺寸小组件里列的明细（最近几笔、今天的课……）。 */
    val lines: List<HomeLine> = emptyList(),
)
