package com.xjtu.toolbox.lite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.xjtu.toolbox.auth.AccountType
import com.xjtu.toolbox.home.EXAM_KEY
import com.xjtu.toolbox.home.HomeCard
import com.xjtu.toolbox.home.HomeGlanceItem
import com.xjtu.toolbox.home.HomeHost
import com.xjtu.toolbox.home.HomeIcons
import com.xjtu.toolbox.home.HomeInboxSummary
import com.xjtu.toolbox.home.HomeNextUp
import com.xjtu.toolbox.home.HomeStat
import com.xjtu.toolbox.home.HomeStatusLine
import com.xjtu.toolbox.home.HomeTone
import com.xjtu.toolbox.nav.AppRoute

/**
 * 占位宿主：首页的全部业务数据都从这里来。
 *
 * **一律硬编码**，不做任何本地推算——包括「第 N 周」与「下一项安排」。展示仓库不该让周次跟着
 * 日历漂（上游那套是 `HomeHero.weekNumber(开学日期)`，今天跑出来会是「第 10 周」，看着像真数据），
 * 也不该依赖时区 / 学期表。想看真实推算逻辑请回上游仓库。
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

    /** 固定文案，不读系统时间。 */
    override val dateLabel: String = "10月12日 · 周一"

    /** 固定「第 4 周」，不由开学日期推算。 */
    override val weekNumber: Int = 4

    override val statusLine: HomeStatusLine =
        HomeStatusLine("校园网 · 子系统 6/6 就绪", "占位数据：所有子系统连接正常", HomeTone.NORMAL)

    /** 固定的一节课，不用 `HomeHero.find` 按当前时刻去找。 */
    override val nextUp: HomeNextUp = HomeNextUp(
        icon = HomeIcons.of(AppRoute.Schedule),
        title = "高等数学（占位）",
        timeLabel = "14:00",
        timeCaption = "至 15:40",
        eta = "还有 1 小时",
        detail = "教2-101 · 张老师",
        route = AppRoute.Schedule,
    )

    override val glances: List<HomeGlanceItem> = listOf(
        HomeGlanceItem(
            icon = HomeIcons.of(AppRoute.CampusCard),
            label = "校园卡",
            value = "¥123.45",
            caption = "占位余额",
            number = 123.45,
            route = AppRoute.CampusCard,
        ),
        HomeGlanceItem(
            icon = HomeIcons.of(AppRoute.Community),
            label = "社区",
            value = "聊两句",
            caption = "提建议 · 报问题",
            route = AppRoute.Community,
        ),
        HomeGlanceItem(
            icon = HomeIcons.of(AppRoute.SchoolCalendar),
            label = "高数",
            value = "12 天",
            caption = "11-23 09:00",
            tone = HomeTone.ACCENT,
            route = AppRoute.Schedule,
        ),
    )

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
}
