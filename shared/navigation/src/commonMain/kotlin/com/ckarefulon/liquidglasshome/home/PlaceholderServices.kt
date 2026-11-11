package com.ckarefulon.liquidglasshome.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import com.ckarefulon.liquidglasshome.nav.AppRoute

/**
 * **占位功能目录**。
 *
 * 本仓库是可复用的主页模板，不该带任何具体项目的功能语义。所以首页宫格由这张表驱动：
 * 名称、副标题全是中性的占位词，图标从一组通用图标里取，配色统一用主题色（见 `serviceColor`）。
 *
 * 想接自己的目录？把 `HomeCardGroupScreen` 里读 [PlaceholderServices] 的地方换成你自己的
 * `List<AppService>` 即可，UI 层不用改。
 */
data class PlaceholderService(
    val route: AppRoute,
    val title: String,
    val subtitle: String,
    val category: ServiceCategory,
    val icon: ImageVector,
)

object PlaceholderServices {

    /**
     * 12 个中性占位，分 3 组（每组 4 个）。
     *
     * 路由复用 [AppRoute] 里已有的常量（模板不新增业务路由）；界面点开只落到占位页，
     * 所以具体是哪个 route 不影响观感，只影响点击后那一页的标题。
     */
    val all: List<PlaceholderService> = listOf(
        PlaceholderService(AppRoute.Schedule, "功能 A", "占位说明", ServiceCategory.CLASS, Icons.Default.CalendarMonth),
        PlaceholderService(AppRoute.Library, "功能 B", "占位说明", ServiceCategory.CLASS, Icons.AutoMirrored.Filled.LibraryBooks),
        PlaceholderService(AppRoute.JwappScore, "功能 C", "占位说明", ServiceCategory.CLASS, Icons.Default.Assessment),
        PlaceholderService(AppRoute.Attendance, "功能 D", "占位说明", ServiceCategory.CLASS, Icons.Default.AssignmentTurnedIn),
        PlaceholderService(AppRoute.EmptyRoom, "功能 E", "占位说明", ServiceCategory.STUDY, Icons.Default.Chair),
        PlaceholderService(AppRoute.Jiaocai, "功能 F", "占位说明", ServiceCategory.STUDY, Icons.AutoMirrored.Filled.MenuBook),
        PlaceholderService(AppRoute.SchoolCourse, "功能 G", "占位说明", ServiceCategory.STUDY, Icons.Default.MeetingRoom),
        PlaceholderService(AppRoute.Jiaocai1, "功能 H", "占位说明", ServiceCategory.STUDY, Icons.Default.Description),
        PlaceholderService(AppRoute.CampusCard, "功能 I", "占位说明", ServiceCategory.LIFE, Icons.Default.GridOn),
        PlaceholderService(AppRoute.Venue, "功能 J", "占位说明", ServiceCategory.LIFE, Icons.Default.Widgets),
        PlaceholderService(AppRoute.Community, "功能 K", "占位说明", ServiceCategory.TOOL, Icons.Default.Forum),
        PlaceholderService(AppRoute.Games, "功能 L", "占位说明", ServiceCategory.TOOL, Icons.Default.SportsEsports),
    )

    fun homeFor(): List<PlaceholderService> = all

    /** 点开某个入口时的占位页标题。 */
    fun titleOfRoute(route: AppRoute): String =
        all.firstOrNull { it.route.id == route.id }?.title ?: "占位页"
}

/** 占位图标池：给「添加卡片」悬浮条等需要「一个中性图标」的地方用。 */
object PlaceholderIcons {
    private val pool: List<ImageVector> = listOf(
        Icons.Default.Widgets,
        Icons.Default.Apps,
        Icons.Default.GridOn,
        Icons.Default.Grain,
        Icons.Default.Dashboard,
        Icons.Default.Groups,
    )

    fun of(index: Int): ImageVector = pool[Math.floorMod(index, pool.size)]
}

/** 占位目录 → 主页 UI 认识的那份目录形状（[AppService]）。 */
fun List<PlaceholderService>.toAppServices(): List<AppService> = map {
    AppService(
        route = it.route,
        title = it.title,
        subtitle = it.subtitle,
        category = it.category,
    )
}
