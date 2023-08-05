package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.airport.runway.RunwayPosition
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits
import org.junit.Assert.assertEquals
import org.junit.Test

class RunwayVisualRangeTest {

    @Test
    fun testCodeWithoutMinVisibility() {
        val runway = Runway(9)
        val rvr = RunwayVisualRange(
            runway,
            maxVisibility = RunwayVisualRange.RvRVisibility.build("1500"),
            units = AltitudeUnits.METERS
        )

        assertEquals("R09/1500m", rvr.code)
    }

    @Test
    fun testCodeWithMinVisibility() {
        val runway = Runway(number=27)

        val maxVisibility = RunwayVisualRange.RvRVisibility.build("600")
        val minVisibility = RunwayVisualRange.RvRVisibility.build("200")

        val rvr = RunwayVisualRange(
            runway,
            maxVisibility,
            minVisibility
        )

        assertEquals("R27/200V600ft", rvr.code)
    }

    @Test
    fun testDescriptionWithoutMinVisibility() {
        val runway = Runway(18, RunwayPosition.RIGHT)
        val maxVisibility = RunwayVisualRange.RvRVisibility.build("P1800")

        val rvr = RunwayVisualRange(runway, maxVisibility, units = AltitudeUnits.METERS)

        assertEquals("Runway 18R visual range: greater than 1800m", rvr.description)
    }

    @Test
    fun testDescriptionWithMinVisibility() {
        val runway = Runway(36, RunwayPosition.LEFT)

        val rvr = RunwayVisualRange(runway,
            RunwayVisualRange.RvRVisibility.build("700"),
            RunwayVisualRange.RvRVisibility.build("300")
        )

        assertEquals("Runway 36L visual range: varying between 300 and 700ft", rvr.description)
    }

    @Test
    fun `test range with below maximum`() {
        val runway = Runway(36, RunwayPosition.LEFT)

        val rvr = RunwayVisualRange(runway,
            RunwayVisualRange.RvRVisibility.build("M700"),
            RunwayVisualRange.RvRVisibility.build("300")
        )

        assertEquals("Runway 36L visual range: varying between 300 and less than 700ft", rvr.description)
        assertEquals("R36L/300VM700ft", rvr.code)
    }
}