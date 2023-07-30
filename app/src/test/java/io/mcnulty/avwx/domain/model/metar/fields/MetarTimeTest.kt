package io.mcnulty.avwx.domain.model.metar.fields

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class MetarTimeTest {

    private lateinit var monthYear: String

    @Before
    fun setUp() {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val format = SimpleDateFormat("MMMM YYYY")
        format.timeZone = TimeZone.getTimeZone("UTC")
        monthYear = format.format(calendar.time)
    }

    @Test
    fun `test valid MetarTime creation`() {
        val metarTime = MetarTime(15, 12, 30)
        assertEquals("15", metarTime.day.toString())
        assertEquals("12", metarTime.hour.toString())
        assertEquals("30", metarTime.minute.toString())

        assertEquals("151230Z", metarTime.code)
        assertEquals("Reported: 15 $monthYear 12:30 UTC", metarTime.description)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test invalid day`() {
        MetarTime(0, 12, 30)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test invalid hour`() {
        MetarTime(15, 24, 30)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test invalid minute`() {
        MetarTime(15, 12, 60)
    }

    @Test
    fun `test code generation`() {
        val metarTime = MetarTime(15, 12, 30)
        assertEquals("151230Z", metarTime.code)
    }

    @Test
    fun `test code generation has correct padding`() {
        val metarTime = MetarTime(5, 2, 3)
        assertEquals("050203Z", metarTime.code)
    }

    @Test
    fun `test description generation`() {
        val metarTime = MetarTime(15, 12, 30)
        val expectedDescription = "Reported: 15 $monthYear 12:30 UTC"
        assertEquals(expectedDescription, metarTime.description)
    }
}