package com.ckarefulon.liquidglasshome.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.ui.graphics.vector.ImageVector
import com.ckarefulon.liquidglasshome.nav.AppRoute

/**
 * 首页「哪个功能配哪个图标」的那张表。
 *
 * **模板用占位图标**：所有入口都从一组中性图标里按路由 id 取一个，
 * 不暴露任何具体业务语义。接自己的目录时，把 [of] 换回「路由 → 图标」的映射即可。
 */
object HomeIcons {

    /** 中性占位图标池：轮流分配，保证同一路由每次拿到的图标稳定。 */
    private val placeholderPool: List<ImageVector> = listOf(
        Icons.Default.Widgets,
        Icons.Default.Apps,
        Icons.Default.GridOn,
        Icons.Default.Grain,
        Icons.Default.Dashboard,
        Icons.Default.Groups,
        Icons.Default.CalendarMonth,
        Icons.Default.Description,
    )

    fun of(route: AppRoute): ImageVector {
        // 用 route.id 的稳定哈希取模：同一路由永远同一个图标，且不依赖枚举顺序
        val index = Math.floorMod(route.id.hashCode(), placeholderPool.size)
        return placeholderPool[index]
    }

    /** 分类卡的图标。 */
    fun category(category: ServiceCategory): ImageVector = when (category) {
        ServiceCategory.CLASS -> Icons.Default.School
        ServiceCategory.STUDY -> Icons.Default.Assessment
        ServiceCategory.LIFE -> Icons.Default.Restaurant
        ServiceCategory.TOOL -> Icons.Default.SmartToy
        ServiceCategory.PLAY -> Icons.Default.SportsEsports
    }

    /** 分类借用的配色键（= 该分类里那个代表性功能的 [AppRoute.id]）。 */
    fun accentKey(category: ServiceCategory): String = when (category) {
        ServiceCategory.CLASS -> AppRoute.Schedule
        ServiceCategory.STUDY -> AppRoute.JwappScore
        ServiceCategory.LIFE -> AppRoute.CampusCard
        ServiceCategory.TOOL -> AppRoute.Agent
        ServiceCategory.PLAY -> AppRoute.Games
    }.id
}
