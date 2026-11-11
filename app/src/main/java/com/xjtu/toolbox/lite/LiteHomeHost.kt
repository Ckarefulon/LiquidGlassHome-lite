package com.xjtu.toolbox.lite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.xjtu.toolbox.auth.AccountType
import com.xjtu.toolbox.home.EXAM_KEY
import com.xjtu.toolbox.home.HomeCard
import com.xjtu.toolbox.home.HomeGlanceItem
import com.xjtu.toolbox.home.HomeHero
import com.xjtu.toolbox.home.HomeHost
import com.xjtu.toolbox.home.HomeIcons
import com.xjtu.toolbox.home.HomeInboxSummary
import com.xjtu.toolbox.home.HomeNextUp
import com.xjtu.toolbox.home.HomeStat
import com.xjtu.toolbox.home.HomeStatusLine
import com.xjtu.toolbox.home.HomeTone
import com.xjtu.toolbox.nav.AppRoute
import com.xjtu.toolbox.schedule.CourseItem
import com.xjtu.toolbox.util.todayOnCampus
import kotlinx.datetime.LocalDate

/**
 * 占位宿主：首页的全部业务数据都从这里来，一律是假数据。
 * 「下一项安排」与「第 N 周」仍走真实的本地推算（[HomeHero]），只是喂的是占位课表。
 */
class LiteHomeHost(
    private val onNavigate: (AppRoute) -> Unit,
    private val onProfileTap: () -> Unit,
    private val onOpenStatus: () -> Unit,
    private val onOpenSettings: () -> Unit,
) : HomeHost {

    override val honoredCards: Set<HomeCard> = setOf(
        HomeCard.GREETING,
        HomeCard.WEEK_NUMBER,
        HomeCard.NEXT_UP,
        HomeCard.GLANCE,
        HomeCard.QUICK_ACTIONS,
        HomeCard.CATEGORY_STATS,
        HomeCard.SERVICE_GRID,
        HomeCard.INBOX,
    )

    override val audience: AccountType = AccountType.UNDERGRADUATE

    override val greetingName: String = "同学"

    override val dateLabel: String
        get() {
            val today = todayOnCampus()
            return "${today.month.ordinal + 1}月${today.day}日 · ${WEEKDAY_LABELS[today.dayOfWeek.ordinal]}"
        }

    override val weekNumber: Int get() = HomeHero.weekNumber(TERM_START)

    override val statusLine: HomeStatusLine =
        HomeStatusLine("校园网 · 子系统 6/6 就绪", "占位数据：所有子系统连接正常", HomeTone.NORMAL)

    override val nextUp: HomeNextUp?
        get() = HomeHero.find(
            courses = PLACEHOLDER_COURSES,
            startOfTerm = TERM_START,
            route = AppRoute.Schedule,
        ) ?: HomeNextUp(
            icon = HomeHero.EMPTY_ICON,
            title = HomeHero.NOTHING_TITLE,
            detail = "点这里看整学期",
            route = AppRoute.Schedule,
        )

    override val glances: List<HomeGlanceItem>
        get() = buildList {
            add(
                HomeGlanceItem(
                    icon = HomeIcons.of(AppRoute.CampusCard),
                    label = "校园卡",
                    value = "¥123.45",
                    caption = "占位余额",
                    number = 123.45,
                    route = AppRoute.CampusCard,
                )
            )
            add(
                HomeGlanceItem(
                    icon = HomeIcons.of(AppRoute.Community),
                    label = "社区",
                    value = "聊两句",
                    caption = "提建议 · 报问题",
                    route = AppRoute.Community,
                )
            )
            stats[EXAM_KEY]?.let { exam ->
                add(
                    HomeGlanceItem(
                        icon = HomeHero.EXAM_ICON,
                        label = exam.detail?.substringBefore(" · ")?.ifBlank { null } ?: "下一场考试",
                        value = exam.value,
                        caption = exam.detail?.substringAfter(" · ", "")?.ifBlank { null },
                        tone = HomeTone.ACCENT,
                        route = AppRoute.Schedule,
                    )
                )
            }
        }

    override var quickRoutes: List<AppRoute> by mutableStateOf(
        listOf(AppRoute.Schedule, AppRoute.JwappScore, AppRoute.Library, AppRoute.CampusCard)
    )
        private set

    override val quickEditable: Boolean = true

    override fun onQuickRoutesChange(routes: List<AppRoute>) {
        quickRoutes = routes
    }

    override val stats: Map<String, HomeStat> = mapOf(
        AppRoute.Schedule.id to HomeStat("今天 4 节课", "占位课表 · 详见日程"),
        AppRoute.JwappScore.id to HomeStat("GPA 3.8", "本学期 · 占位"),
        AppRoute.Attendance.id to HomeStat("出勤 96%", "本月 · 占位"),
        AppRoute.Library.id to HomeStat("在借 3 本", "最近到期 11-20 · 占位"),
        AppRoute.CampusCard.id to HomeStat("¥123.45", "占位余额"),
        AppRoute.Lms().id to HomeStat("2 项待交", "最近截止 明天 23:59 · 占位"),
        EXAM_KEY to HomeStat("12 天", "高等数学 · 11-23 09:00"),
    )

    override val inbox: HomeInboxSummary =
        HomeInboxSummary(todoCount = 3, unreadCount = 2, nextTodo = "提交高数作业（占位）")

    override fun onRoute(route: AppRoute) {
        onNavigate(route)
    }

    override fun onProfile() {
        onProfileTap()
    }

    override fun onOpenStatus() {
        onOpenStatus()
    }

    override suspend fun paymentCode(): String = "LITE-PLACEHOLDER-PAYMENT-CODE"

    override fun onOpenHomeSettings() {
        onOpenSettings()
    }

    override val hasHomeSettings: Boolean = true

    companion object {
        /** 占位开学日期：教学周与「下一项安排」都按它推算。 */
        private val TERM_START = LocalDate(2026, 9, 7)

        private val WEEKDAY_LABELS = listOf("周一", "周二", "周三", "周四", "周五", "周六", "周日")

        private const val ALL_WEEKS = "11111111111111111111"

        private val PLACEHOLDER_COURSES = listOf(
            CourseItem("高等数学（占位）", "张老师", "教2-101", ALL_WEEKS, 1, 1, 2),
            CourseItem("大学英语（占位）", "李老师", "外文楼-203", ALL_WEEKS, 1, 3, 4),
            CourseItem("数据结构（占位）", "王老师", "主楼A-302", ALL_WEEKS, 2, 1, 2),
            CourseItem("大学物理（占位）", "赵老师", "教2-205", ALL_WEEKS, 2, 5, 6),
            CourseItem("思政（占位）", "陈老师", "主楼B-101", ALL_WEEKS, 3, 3, 4),
            CourseItem("体育（占位）", "刘老师", "东南田径场", ALL_WEEKS, 4, 5, 6),
            CourseItem("程序设计实践（占位）", "孙老师", "计算中心-401", ALL_WEEKS, 5, 1, 2),
            CourseItem("线性代数（占位）", "周老师", "教2-108", ALL_WEEKS, 5, 3, 4),
        )
    }
}