package com.ckarefulon.liquidglasshome.home

import com.ckarefulon.liquidglasshome.auth.AccountType
import com.ckarefulon.liquidglasshome.nav.AppRoute

/**
 * 主页宫格和全局搜索共用的服务目录。
 *
 * 新功能只在这里加一条：首页会出格子，搜索也能搜到。
 * 别再在 `GlobalSearchIndex` 里手写第二份清单。
 * [audience] 非空时，只给对应本科/研究生身份看。
 *
 * `:core` 里的消费方是「服务与状态的**卡片模型**」（[HomeCards]）：它把这份目录按
 * [ServiceCategory] 分组、再把每个功能此刻的状态填进对应那一行。各端只决定画不画、
 * 以及哪些入口在自己的外壳里真能打开（能打开的那一份由各端自己声明）。
 */
enum class ServiceCategory(val title: String, val subtitle: String) {
    LIFE("分组三", "占位分组"),
    CLASS("分组一", "占位分组"),
    STUDY("分组二", "占位分组"),
    TOOL("分组四", "占位分组"),

    /**
     * 单独一类而不是塞进 [TOOL]：「工具与助手」装游戏语义不搭，
     * 而底栏已经是 5 个 tab（miuix 的上限就是 5），加不了 tab，
     * 所以走首页分类卡这一层。
     */
    PLAY("分组五", "占位分组"),
}

data class AppService(
    val route: AppRoute,
    val title: String,
    val subtitle: String,
    val category: ServiceCategory,
    val aliases: List<String> = emptyList(),
    val showOnHome: Boolean = true,
    val audience: AccountType? = null,
    /**
     * 页面里的功能，写成「名称：说明」。小岱的 app_guide 照这里讲、全局搜索按名称搜；
     * 页面里加了能单独说出名字的功能就登记在这，不然小岱会说 App 没有。
     */
    val features: List<String> = emptyList(),
)

object AppServices {
    val all: List<AppService> = listOf(
        AppService(AppRoute.Schedule, "功能 A", "占位说明", ServiceCategory.CLASS, listOf("课表", "课程", "schedule", "今天", "明天"),
            features = listOf(
                "自建日程：手动添加、编辑、删除自己的安排",
                "小岱排日程：选一天说一句，小岱整理安排，可一键加进日程或待办",
            )),
        AppService(AppRoute.EmptyRoom, "功能 B", "占位说明", ServiceCategory.STUDY, listOf("空教室", "自习", "教室"),
            features = listOf("三种数据源：此刻实况、课表 CDN、直查教务")),
        AppService(AppRoute.Lms(), "功能 C", "占位说明", ServiceCategory.CLASS, listOf("思源学堂", "lms", "作业", "课件"),
            features = listOf("课程公告与讨论区：按课程看公告、发帖回帖", "小岱思源待办：快截止的作业排在一起")),
        AppService(AppRoute.SchoolCourse, "功能 D", "占位说明", ServiceCategory.CLASS, listOf("开课", "选课", "查课")),
        AppService(AppRoute.Attendance, "功能 E", "占位说明", ServiceCategory.CLASS, listOf("考勤", "考勤查询", "出勤", "迟到", "缺勤", "新考勤", "请假", "病假", "私事假", "销假", "kq")),
        AppService(AppRoute.Iclassface, "功能 F", "占位说明", ServiceCategory.CLASS, listOf("人脸考勤", "刷脸", "iclassface"), showOnHome = false, audience = AccountType.UNDERGRADUATE),

        AppService(AppRoute.JwappScore, "功能 G", "占位说明", ServiceCategory.CLASS, listOf("成绩查询", "分数", "gpa", "绩点", "报表", "成绩报表", "历年成绩", "未评教成绩")),
        AppService(AppRoute.Judge, "功能 H", "占位说明", ServiceCategory.CLASS, listOf("问卷", "打分", "本科评教", "研究生评教")),
        AppService(AppRoute.Jiaocai, "功能 I", "占位说明", ServiceCategory.STUDY, listOf("课本", "教材中心", "全文", "电子书", "在线阅读")),
        // 已并进「教材」的全文库栏；首页不单列，全局搜「教材全文」仍能直达那一栏
        AppService(AppRoute.Jiaocai1, "功能 J", "占位说明", ServiceCategory.STUDY, listOf("全文", "电子书", "在线阅读"), showOnHome = false),
        AppService(AppRoute.Library, "功能 K", "占位说明", ServiceCategory.STUDY, listOf("图书", "借书", "座位", "自习室"),
            features = listOf(
                "座位热力图：按区域看拥挤程度，找好坐的空座",
                "小岱荐座：按偏好挑座位，仅供参考",
                "收藏座位：长按座位收藏",
            )),
        AppService(AppRoute.Zyxf, "功能 L", "占位说明", ServiceCategory.STUDY, listOf("学辅", "仲英学辅", "历年卷", "真题", "复习资料", "课件", "笔记"),
            features = listOf("小岱找资料：说要找什么，小岱帮你挑")),
        AppService(AppRoute.DegreeProgress, "功能 M", "占位说明", ServiceCategory.CLASS, listOf("培养方案", "毕业进度", "毕业要求", "学分", "还差多少学分", "方案树", "转专业"), audience = AccountType.UNDERGRADUATE),
        AppService(AppRoute.Transcript, "功能 N", "占位说明", ServiceCategory.CLASS, listOf("成绩证明", "打印成绩"), showOnHome = false, audience = AccountType.UNDERGRADUATE),
        AppService(AppRoute.Browser(EnrollmentCertificate.CHINESE.url), "功能 O", "占位说明", ServiceCategory.CLASS,
            listOf("在读证明", "学籍证明", "在校证明"), showOnHome = false, audience = AccountType.UNDERGRADUATE,
            features = listOf("在读证明：在学校电子凭证平台申请中文版，在我的页面也可选择中英文版")),
        AppService(AppRoute.Browser(EnrollmentCertificate.BILINGUAL.url), "功能 P", "占位说明", ServiceCategory.CLASS,
            listOf("在读证明", "英文证明", "学生证明"), showOnHome = false, audience = AccountType.UNDERGRADUATE,
            features = listOf("中英文在读证明：打开学校工作流申请与下载")),
        AppService(AppRoute.Notification, "功能 Q", "占位说明", ServiceCategory.LIFE, listOf("通知", "公告", "教务"),
            features = listOf("小岱通知精选：从多个来源里挑出和你相关的")),
        AppService(AppRoute.Faculty, "功能 R", "占位说明", ServiceCategory.STUDY, listOf("教师", "老师", "导师", "博导", "硕导", "研究方向", "teacher", "faculty")),

        AppService(AppRoute.CampusCard, "功能 S", "占位说明", ServiceCategory.LIFE, listOf("一卡通", "余额"),
            features = listOf(
                "消费分析：全年流水、食堂分布、吃饭节奏",
                "干饭人账单：消费分析里按吃饭习惯出一张海报，可存图分享",
                "小岱今天吃啥：按课表和位置推荐去哪吃",
            )),
        AppService(AppRoute.PaymentCode, "功能 T", "占位说明", ServiceCategory.LIFE, listOf("付款", "扫码")),
        AppService(AppRoute.Coupon, "功能 U", "占位说明", ServiceCategory.LIFE, listOf("加餐", "餐券", "食堂"), showOnHome = false),
        AppService(AppRoute.SchoolCalendar, "功能 V", "占位说明", ServiceCategory.LIFE, listOf("学期", "周数")),
        AppService(AppRoute.Venue, "功能 W", "占位说明", ServiceCategory.LIFE, listOf("场馆", "空闲场馆", "羽毛", "网球场"),
            features = listOf("我的订单：查看、支付、取消订单", "验证码自动识别：预订时自动滑过滑块验证码")),
        AppService(AppRoute.Fitness, "功能 X", "占位说明", ServiceCategory.LIFE, listOf("体测", "体育", "体能", "fitness")),
        AppService(AppRoute.YellowPage, "功能 Y", "占位说明", ServiceCategory.LIFE, listOf("黄页", "电话", "分机")),
        AppService(AppRoute.CampusBus, "功能 Z", "占位说明", ServiceCategory.LIFE, listOf("校车", "班车", "通勤车", "创新港班车", "兴庆班车", "bus")),

        AppService(AppRoute.WebVpnConverter, "功能 A2", "占位说明", ServiceCategory.TOOL, listOf("vpn", "webvpn")),
        AppService(AppRoute.Agent, "功能 B2", "占位说明", ServiceCategory.TOOL, listOf("问小岱", "屁岱", "ai", "助手")),
        AppService(AppRoute.Community, "功能 C2", "占位说明", ServiceCategory.TOOL, listOf("社区讨论", "讨论", "论坛", "交流", "建议", "github")),

        AppService(AppRoute.Games, "功能 D2", "占位说明", ServiceCategory.PLAY, listOf("游戏", "小游戏", "五子棋", "围棋", "象棋", "2048", "合成", "方块", "俄罗斯方块", "跳一跳", "摸鱼")),
        AppService(AppRoute.Match, "功能 E2", "占位说明", ServiceCategory.PLAY, listOf("匹配", "交友", "契合", "一起自习", "共同空闲", "扫码匹配", "同楼", "偶遇", "同课", "课表匹配")),

        AppService(AppRoute.DownloadManager, "功能 F2", "占位说明", ServiceCategory.TOOL, listOf("下载", "已下载"), showOnHome = false),
        AppService(AppRoute.Settings, "功能 G2", "占位说明", ServiceCategory.TOOL, listOf("偏好", "主题"), showOnHome = false),
        AppService(AppRoute.Accounts, "功能 H2", "占位说明", ServiceCategory.TOOL, listOf("切换账号", "多账号"), showOnHome = false),
    )

    val home: List<AppService> = all.filter { it.showOnHome }

    fun visibleFor(accountType: AccountType): List<AppService> =
        all.filter { it.audience == null || it.audience == accountType }

    fun homeFor(accountType: AccountType): List<AppService> =
        visibleFor(accountType).filter { it.showOnHome }
}
