package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.metar.AltimeterDto
import io.mcnulty.avwx.domain.model.metar.measurement.PressureUnits
import org.junit.Assert.assertEquals
import org.junit.Test

class AltimeterParserTest {
    @Test
    fun `parse should return AtmosphericPressure Altimeter when using PressureUnits INCHES_OF_MERCURY`() {
        // Arrange
        val altimeterDto = AltimeterDto("A2966", "two nine point six six", 29.66)
        val units = PressureUnits.INCHES_OF_MERCURY

        // Act
        val result = AltimeterParser.parse(altimeterDto, units)

        assertEquals("A2966", result.code)
        assertEquals("29.66 inHg", result.description)
    }

    @Test
    fun `parse should return AtmosphericPressure Qnh when using PressureUnits HECTOPASCALS`() {
        // Arrange
        val altimeterDto = AltimeterDto("Q1000", "one zero zero zero", 1000)
        val units = PressureUnits.HECTOPASCALS

        // Act
        val result = AltimeterParser.parse(altimeterDto, units)

        assertEquals("QNH1000", result.code)
        assertEquals("1000 hPa", result.description)
    }
}
