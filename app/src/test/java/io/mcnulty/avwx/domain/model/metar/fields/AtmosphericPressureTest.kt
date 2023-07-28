package io.mcnulty.avwx.domain.model.metar.fields

import org.junit.Assert.*
import org.junit.Test

class AtmosphericPressureTest {
    // test inHg pressure
    @Test
    fun testInHg() {
        val pressure = AtmosphericPressure.Altimeter(29.92)
        assertEquals("A2992", pressure.code)
        assertEquals("29.92 inHg", pressure.description)
    }

    // test hPa pressure
    @Test
    fun testHpa() {
        val pressure = AtmosphericPressure.Qnh(1013)
        assertEquals("QNH1013", pressure.code)
        assertEquals("1013 hPa", pressure.description)
    }
}