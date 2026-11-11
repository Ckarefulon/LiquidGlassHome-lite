package com.ckarefulon.liquidglasshome.inbox

import kotlin.test.Test
import kotlin.test.assertEquals

class InboxMarkdownPlainTest {
    @Test
    fun stripsCommonMarkdown() {
        val md = """
            ## 今日简报
            - **8:00** 主楼 A-201 *示例课*
            * 交 `实验报告`，见[示例](https://lms.example.edu)
            ---
            > 余额 12.5 元
        """.trimIndent()
        assertEquals(
            "今日简报\n· 8:00 示例楼 A-201 课程\n· 交 实验报告，见示例\n余额 12.5 元",
            InboxRules.markdownToPlain(md),
        )
    }

    @Test
    fun keepsPlainTextAndArithmetic() {
        assertEquals("3*4 间隔 2 小时", InboxRules.markdownToPlain("3*4 间隔 2 小时"))
    }
}
