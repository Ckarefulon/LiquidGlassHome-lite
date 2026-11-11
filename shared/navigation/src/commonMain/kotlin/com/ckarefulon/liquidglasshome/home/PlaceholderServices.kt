package com.ckarefulon.liquidglasshome.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Stadium
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.ui.graphics.vector.ImageVector
import com.ckarefulon.liquidglasshome.nav.AppRoute

/**
 * **占位功能目录**。
 *
 * 本仓库是可复用的主页模板，不该带任何具体项目的功能语义。所以首页宫格由这张表驱动：
 * 名称、副标题全是中性的占位词，图标是中性的通用图标，配色统一用主题色。
 *
 * **条目数量与默认排布尺寸刻意与上游模板保持一致（25 格、3 组）**，这样液态玻璃主页在
 * 默认状态下就是设计好的密度——格子太少会让整页显得空，也会让「添加卡片」悬浮条没东西可加。
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

    /** 25 个占位，分 5 组，与上游默认布局同数量同分组。 */
    val all: List<PlaceholderService> = listOf(
        // ── 教务与课程 ──
        PlaceholderService(AppRoute.Schedule, "功能 A", "占位说明", ServiceCategory.CLASS, Icons.Default.CalendarMonth),
        PlaceholderService(AppRoute.EmptyRoom, "功能 B", "占位说明", ServiceCategory.CLASS, Icons.Default.MeetingRoom),
        PlaceholderService(AppRoute.Lms(), "功能 C", "占位说明", ServiceCategory.CLASS, Icons.Default.School),
        PlaceholderService(AppRoute.SchoolCourse, "功能 D", "占位说明", ServiceCategory.CLASS, Icons.Default.TravelExplore),
        PlaceholderService(AppRoute.Attendance, "功能 E", "占位说明", ServiceCategory.CLASS, Icons.Default.AssignmentTurnedIn),
        PlaceholderService(AppRoute.JwappScore, "功能 F", "占位说明", ServiceCategory.CLASS, Icons.Default.Assessment),
        PlaceholderService(AppRoute.Judge, "功能 G", "占位说明", ServiceCategory.CLASS, Icons.Default.Face),

        // ── 资料与自习 ──
        PlaceholderService(AppRoute.Jiaocai, "功能 H", "占位说明", ServiceCategory.STUDY, Icons.AutoMirrored.Filled.MenuBook),
        PlaceholderService(AppRoute.Library, "功能 I", "占位说明", ServiceCategory.STUDY, Icons.Default.Chair),
        PlaceholderService(AppRoute.Zyxf, "功能 J", "占位说明", ServiceCategory.STUDY, Icons.AutoMirrored.Filled.LibraryBooks),
        PlaceholderService(AppRoute.Transcript, "功能 K", "占位说明", ServiceCategory.STUDY, Icons.Default.Description),
        PlaceholderService(AppRoute.Faculty, "功能 L", "占位说明", ServiceCategory.STUDY, Icons.Default.PersonSearch),

        // ── 校园生活 ──
        PlaceholderService(AppRoute.Notification, "功能 M", "占位说明", ServiceCategory.LIFE, Icons.Default.Notifications),
        PlaceholderService(AppRoute.CampusCard, "功能 N", "占位说明", ServiceCategory.LIFE, Icons.Default.CreditCard),
        PlaceholderService(AppRoute.PaymentCode, "功能 O", "占位说明", ServiceCategory.LIFE, Icons.Default.QrCode),
        PlaceholderService(AppRoute.Coupon, "功能 P", "占位说明", ServiceCategory.LIFE, Icons.Default.Restaurant),
        PlaceholderService(AppRoute.SchoolCalendar, "功能 Q", "占位说明", ServiceCategory.LIFE, Icons.Default.CalendarMonth),
        PlaceholderService(AppRoute.Venue, "功能 R", "占位说明", ServiceCategory.LIFE, Icons.Default.Stadium),
        PlaceholderService(AppRoute.YellowPage, "功能 S", "占位说明", ServiceCategory.LIFE, Icons.Default.ContactPhone),
        PlaceholderService(AppRoute.CampusBus, "功能 T", "占位说明", ServiceCategory.LIFE, Icons.Default.DirectionsBus),

        // ── 工具与助手 ──
        PlaceholderService(AppRoute.WebVpnConverter, "功能 U", "占位说明", ServiceCategory.TOOL, Icons.Default.VpnKey),
        PlaceholderService(AppRoute.Community, "功能 V", "占位说明", ServiceCategory.TOOL, Icons.Default.Forum),
        PlaceholderService(AppRoute.Agent, "功能 W", "占位说明", ServiceCategory.TOOL, Icons.Default.SmartToy),

        // ── 课余 ──
        PlaceholderService(AppRoute.Games, "功能 X", "占位说明", ServiceCategory.PLAY, Icons.Default.SportsEsports),
        PlaceholderService(AppRoute.Match, "功能 Y", "占位说明", ServiceCategory.PLAY, Icons.Default.Groups),
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
