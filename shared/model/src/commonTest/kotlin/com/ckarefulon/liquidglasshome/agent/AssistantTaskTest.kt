package com.ckarefulon.liquidglasshome.agent

import com.ckarefulon.liquidglasshome.util.CampusTimeZone
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class AssistantTaskTest {

    private val mondayMorning = LocalDateTime(2026, 10, 19, 8, 30) // 周一
    private fun at(t: LocalDateTime) = t.toInstant(CampusTimeZone).toEpochMilliseconds()

    @Test
    fun `过了点、今天在选定的日子里、今天还没跑过才算到点`() {
        val daily = AssistantTask("a", "看通知", 8, 0)
        val mondays = AssistantTask("b", "看奖学金", 8, 0, days = listOf(1))
        val fridays = AssistantTask("c", "周末去哪", 8, 0, days = listOf(5))
        val later = AssistantTask("d", "晚点", 9, 0)
        val ranToday = AssistantTask("e", "跑过了", 8, 0, lastRun = at(LocalDateTime(2026, 10, 19, 8, 5)))
        val ranYesterday = AssistantTask("f", "昨天跑的", 8, 0, lastRun = at(LocalDateTime(2026, 10, 18, 8, 5)))
        val due = AssistantTaskStore.due(mondayMorning, listOf(daily, mondays, fridays, later, ranToday, ranYesterday))
        assertEquals(listOf("a", "b", "f"), due.map { it.id })
    }
}
