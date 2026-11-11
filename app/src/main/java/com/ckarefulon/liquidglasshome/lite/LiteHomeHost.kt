package com.ckarefulon.liquidglasshome.lite

import com.ckarefulon.liquidglasshome.auth.AccountType
import com.ckarefulon.liquidglasshome.home.HomeCard
import com.ckarefulon.liquidglasshome.home.HomeGlanceItem
import com.ckarefulon.liquidglasshome.home.HomeHost
import com.ckarefulon.liquidglasshome.home.HomeInboxSummary
import com.ckarefulon.liquidglasshome.home.HomeNextUp
import com.ckarefulon.liquidglasshome.home.HomeStat
import com.ckarefulon.liquidglasshome.home.HomeStatusLine
import com.ckarefulon.liquidglasshome.home.AppServices
import com.ckarefulon.liquidglasshome.nav.AppRoute

/**
 * 占位宿主：主页需要的业务数据全在这里，**一律硬编码**。
 *
 * 模板刻意只留「功能宫格」这一块内容——刊头 / 问候 / 头卡 / 速览 / 待办都不提供：
 * 那几块要么带具体项目语义，要么得接真实数据才有意义。
 *
 * 接自己的数据时：把 [honoredCards] 里需要的项打开，并在对应成员里返回真实数据即可
 * （接口其余成员都有默认实现，不写就不画）。
 */
class LiteHomeHost(
    private val onNavigate: (AppRoute) -> Unit,
    private val onOpenSettings: () -> Unit,
) : HomeHost {

    /** 只声明「功能宫格」与它的状态两块；其余块（刊头、速览、待办……）一律不画。 */
    override val honoredCards: Set<HomeCard> = setOf(
        HomeCard.SERVICE_GRID,
        HomeCard.CATEGORY_STATS,
    )

    override val audience: AccountType = AccountType.UNDERGRADUATE

    /** 不显示问候语（模板不画刊头）。 */
    override val greetingName: String = ""

    override val dateLabel: String = ""

    override val weekNumber: Int = 0

    override val statusLine: HomeStatusLine? = null

    override val nextUp: HomeNextUp? = null

    override val glances: List<HomeGlanceItem> = emptyList()

    override val quickRoutes: List<AppRoute> = emptyList()

    override val quickEditable: Boolean = false

    /** 每个占位功能带一行占位状态（宫格里那块小字）。 */
    override val stats: Map<String, HomeStat> = AppServices.all.associate {
        it.route.id to HomeStat("占位状态")
    }

    override val inbox: HomeInboxSummary? = null

    override fun onRoute(route: AppRoute) {
        onNavigate(route)
    }

    override fun onOpenHomeSettings() {
        onOpenSettings()
    }

    override val hasHomeSettings: Boolean = true
}
