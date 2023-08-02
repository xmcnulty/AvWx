package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.measurement.WindSpeedUnits
import org.junit.Assert.assertEquals
import org.junit.Test

class WindsTest {

    @Test
    fun testCodeAndDescriptionWithDirectionAndGust() {
        val winds = Winds(direction = 180, speed = 10, gust = 15, units = WindSpeedUnits.KNOTS)
        assertEquals("18010G15kt", winds.code)
        assertEquals("Winds at 180 degrees, at 10 knots gusting to 15 knots.", winds.description)
    }

    @Test
    fun testCodeAndDescriptionWithoutDirectionAndGust() {
        val winds = Winds(direction = null, speed = 5, gust = null, units = WindSpeedUnits.MILES_PER_HOUR)
        assertEquals("VRB05mph", winds.code)
        assertEquals("Winds variable, at 5 miles per hour.", winds.description)
    }

    @Test
    fun testCodeAndDescriptionWithVariationAndGust() {
        val winds = Winds(direction = 270, speed = 20, gust = 25, variation = Winds.Variation(250, 310), units = WindSpeedUnits.KNOTS)
        assertEquals("27020G25kt 250V310", winds.code)
        assertEquals("Winds varying between 250 and 310 degrees, at 20 knots gusting to 25 knots.", winds.description)
    }

    @Test
    fun testCodeAndDescriptionWithDirectionWithoutGust() {
        val winds = Winds(direction = 90, speed = 15, gust = null, units = WindSpeedUnits.KNOTS)
        assertEquals("09015kt", winds.code)
        assertEquals("Winds at 90 degrees, at 15 knots.", winds.description)
    }

    @Test
    fun testCodeAndDescriptionWithVariationWithoutGust() {
        val winds = Winds(direction = 180, speed = 10, gust = null, variation = Winds.Variation(160, 200), units = WindSpeedUnits.KNOTS)
        assertEquals("18010kt 160V200", winds.code)
        assertEquals("Winds varying between 160 and 200 degrees, at 10 knots.", winds.description)
    }

    @Test
    fun testCodeAndDescriptionWithoutDirectionAndVariation() {
        val winds = Winds(direction = null, speed = 5, gust = null, units = WindSpeedUnits.MILES_PER_HOUR)
        assertEquals("VRB05mph", winds.code)
        assertEquals("Winds variable, at 5 miles per hour.", winds.description)
    }

    @Test
    fun testWindsCalm() {
        val winds = Winds(direction = 0, speed = 0, units = WindSpeedUnits.KNOTS)
        assertEquals("00000kt", winds.code)
        assertEquals("Winds calm.", winds.description)
    }
}