package io.mcnulty.avwx.domain.model

import org.junit.Assert.*
import org.junit.Test

class PrecipitationTest {
    @Test
    fun testRain() {
        val rain = Precipitation.Rain()
        assertEquals("RA", rain.marker)
        assertEquals(Precipitation.Intensity.MODERATE, rain.intensity)
        assertEquals("RA", rain.toString())
    }
}