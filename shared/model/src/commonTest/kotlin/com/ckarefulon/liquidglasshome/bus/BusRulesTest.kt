package com.ckarefulon.liquidglasshome.bus

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BusRulesTest {
    private fun run(line: String, vararg stops: String, time: String = "07:30", used: Int = 0, seats: Int = 49) = BusRun(
        lineName = line, time = time, category = "示例校区 C 班车", seats = seats, usedSeats = used, plate = "陕A00000",
        from = stops.first(), to = stops.last(), stops = stops.map { BusStop(it) },
    )

    private val toPort = run("3号线（示例中心-示例校区 C）", "示例中心", "示例公交站", "示例校区 C")
    private val toCity = run("3号线（示例校区 C-示例中心）", "示例校区 C", "示例公交站", "示例中心")
    private val yanta = run("示例B2线", "示例行政楼", "示例南广场")

    @Test fun directionMatters() {
        assertTrue(BusRules.runsBetween(toPort, "示例中心", "示例校区 C"))
        assertFalse(BusRules.runsBetween(toCity, "示例中心", "示例校区 C"))
    }

    @Test fun campusNamesResolveToStops() {
        assertTrue(BusRules.runsBetween(toPort, "示例校区", "示例校区 C"))
        assertTrue(BusRules.runsBetween(yanta, "示例校区 B", "示例校区"))
        assertFalse(BusRules.runsBetween(yanta, "示例校区", "示例校区 B"))
    }

    @Test fun missingStopNeverMatches() {
        assertFalse(BusRules.runsBetween(toPort, "不存在", null))
        assertFalse(BusRules.runsBetween(toPort, null, "不存在"))
        assertTrue(BusRules.runsBetween(toPort, null, null))
    }

    @Test fun searchUnderstandsRoutesAndSingleWords() {
        val all = listOf(toPort, toCity, yanta)
        assertEquals(listOf(toPort), BusRules.search(all, "示例中心到示例校区 C"))
        assertEquals(listOf(toCity), BusRules.search(all, "示例校区 C 示例中心"))
        assertEquals(listOf(toPort, toCity), BusRules.search(all, "北池头"))
        assertEquals(all, BusRules.search(all, "  "))
    }

    @Test fun seatsLeftAndTightness() {
        assertEquals(0, run("a", "x", "y", used = 60).seatsLeft)
        assertTrue(run("a", "x", "y", used = 49).isFull)
        assertTrue(run("a", "x", "y", used = 40).isTight)
        assertFalse(run("a", "x", "y", used = 30).isTight)
    }

    @Test fun shortNameDropsTheRouteInBrackets() {
        assertEquals("1号线", BusRules.shortLineName("1号线(三村西门-示例校区 C)"))
        assertEquals("示例B2线", BusRules.shortLineName("示例B2线（示例校区 B-示例校区直达）"))
        assertEquals("（怪名字）", BusRules.shortLineName("（怪名字）"))
        assertTrue(BusRules.isDirect(run("1号线(三村西门直通车)", "x", "y")))
        assertFalse(BusRules.isDirect(run("3号线（示例中心-示例校区 C）", "x", "y")))
    }

    @Test fun periodsFollowTheClock() {
        assertEquals("上午", BusRules.periodOf("06:20"))
        assertEquals("下午", BusRules.periodOf("12:00"))
        assertEquals("晚上", BusRules.periodOf("18:00"))
    }

    @Test fun aRunLeavingThisMinuteHasNotDeparted() {
        assertFalse(BusRules.hasDeparted(toPort, "07:30"))
        assertTrue(BusRules.hasDeparted(toPort, "07:31"))
    }
}
