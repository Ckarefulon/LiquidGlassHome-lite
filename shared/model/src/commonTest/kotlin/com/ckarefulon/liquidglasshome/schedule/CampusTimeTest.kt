package com.ckarefulon.liquidglasshome.schedule

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** [CampusTime] 的学年/学期换算。 */
class CampusTimeTest {

    @Test
    fun `九月开学推到秋季学期`() {
        assertEquals("2026-2027-1", CampusTime.expectedTermCode(LocalDate(2026, 9, 14)))
    }

    @Test
    fun `一月仍算上一学年的秋季学期`() {
        assertEquals("2025-2026-1", CampusTime.expectedTermCode(LocalDate(2026, 1, 10)))
    }

    @Test
    fun `三月推到春季学期`() {
        assertEquals("2025-2026-2", CampusTime.expectedTermCode(LocalDate(2026, 3, 2)))
    }

    @Test
    fun `六月底仍是春季学期`() {
        assertEquals("2025-2026-2", CampusTime.expectedTermCode(LocalDate(2026, 6, 30)))
    }

    @Test
    fun `七八月分不清短学期与暑假返回空`() {
        assertNull(CampusTime.expectedTermCode(LocalDate(2026, 7, 15)))
        assertNull(CampusTime.expectedTermCode(LocalDate(2026, 8, 20)))
    }
}
