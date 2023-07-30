package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.airport.runway.RunwayPosition
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits
import org.junit.Assert.assertEquals
import org.junit.Test

class RunwayVisualRangeTest {

    @Test
    fun testCodeWithoutMinVisibility() {
        val runway = Runway(9)
        val rvr = RunwayVisualRange(
            runway,
            maxVisibility = 1500,
            marker = RunwayVisualRange.Marker.LESS_THAN,
            units = VisibilityUnits.METERS
        )

        assertEquals("09/M1500M", rvr.code)
    }

    @Test
    fun testCodeWithMinVisibility() {
        val runway = Runway(number=27)

        val maxVisibility = 600
        val minVisibility = 200

        val rvr = RunwayVisualRange(
            runway,
            maxVisibility,
            minVisibility
        )

        assertEquals("27/200V600FT", rvr.code)
    }

    @Test
    fun testDescriptionWithoutMinVisibility() {
        val runway = Runway(18, RunwayPosition.RIGHT)
        val maxVisibility = 1800
        val marker = RunwayVisualRange.Marker.GREATER_THAN

        val rvr = RunwayVisualRange(runway, maxVisibility, marker = marker, units = VisibilityUnits.METERS)

        assertEquals("Runway 18R visual range: greater than 1800 meters", rvr.description)
    }

    @Test
    fun testDescriptionWithMinVisibility() {
        val runway = Runway(36, RunwayPosition.LEFT)

        val maxVisibility = 700
        val minVisibility = 300

        val rvr = RunwayVisualRange(runway, maxVisibility, minVisibility)

        assertEquals("Runway 36L visual range: between 300 and 700 feet", rvr.description)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalidMinVisibility() {
        val runway = Runway(9, RunwayPosition.CENTER)
        val maxVisibility = 800
        val minVisibility = 1000

        // Should throw IllegalArgumentException as minVisibility must be less than maxVisibility
        RunwayVisualRange(runway, maxVisibility, minVisibility)
    }
}