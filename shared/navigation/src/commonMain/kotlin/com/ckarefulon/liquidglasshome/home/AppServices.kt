package com.ckarefulon.liquidglasshome.home

import com.ckarefulon.liquidglasshome.auth.AccountType
import com.ckarefulon.liquidglasshome.nav.AppRoute

/**
 * 主页宫格 / 分类卡用的「功能条目」形状。
 *
 * 模板里它的数据来源是 [PlaceholderServices]；使用方接自己的功能目录时，只要产出
 * 一串 `AppService` 即可，UI 层（`HomeCardGroupScreen` / `HomeCards`）一行都不用改。
 */
data class AppService(
    val route: AppRoute,
    val title: String,
    val subtitle: String,
    val category: ServiceCategory,
    val aliases: List<String> = emptyList(),
    val showOnHome: Boolean = true,
    val audience: AccountType? = null,
    val features: List<String> = emptyList(),
)

/** 分类：组名直接画在主页上，所以是中性占位词。 */
enum class ServiceCategory(val title: String, val subtitle: String) {
    CLASS("分组一", "占位分组"),
    STUDY("分组二", "占位分组"),
    LIFE("分组三", "占位分组"),
    TOOL("分组四", "占位分组"),
    PLAY("分组五", "占位分组"),
}
