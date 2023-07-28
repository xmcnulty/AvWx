package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits
import org.junit.Assert.*
import org.junit.Test

class CloudsTest {

    // test clouds in the hundreds offt
    @Test
    fun testCloudsHundredsOfFeet() {
        val clouds = Clouds(100, Clouds.Coverage.FEW)
        assertEquals("FEW001", clouds.code)
        assertEquals("Few at 100ft", clouds.description)
    }

    @Test
    fun testCloudsThousandsOfFeet() {
        val clouds = Clouds(3000, Clouds.Coverage.SCATTERED)
        assertEquals("SCT030", clouds.code)
        assertEquals("Scattered at 3,000ft", clouds.description)
    }

    @Test
    fun testCloudsTensOfThousandsOfFeet() {
        val clouds = Clouds(30000, Clouds.Coverage.BROKEN)
        assertEquals("BKN300", clouds.code)
        assertEquals("Broken at 30,000ft", clouds.description)
    }

    @Test
    fun testCloudsWithTensAndSingleThousands() {
        val clouds = Clouds(31000, Clouds.Coverage.OVERCAST)
        assertEquals("OVC310", clouds.code)
        assertEquals("Overcast at 31,000ft", clouds.description)
    }

    // test unknown cloud type
    @Test
    fun testCloudsUnknownType() {
        val clouds = Clouds(100, Clouds.Coverage.UNKNOWN)
        assertEquals("Unknown at 100ft", clouds.description)
    }

    // test 0 altitude throws exception
    @Test(expected = IllegalArgumentException::class)
    fun testCloudsZeroAltitude() {
        Clouds(0, Clouds.Coverage.FEW)
    }

    // test negative altitude throws exception
    @Test(expected = IllegalArgumentException::class)
    fun testCloudsNegativeAltitude() {
        Clouds(-100, Clouds.Coverage.FEW)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAltitudeLargerThanMax() {
        Clouds(100000, Clouds.Coverage.FEW)
    }

    // test cumulonimbus cloud type
    @Test
    fun testCloudsCumulonimbus() {
        val clouds = Clouds(5000, Clouds.Coverage.FEW, Clouds.Type.CUMULONIMBUS)
        assertEquals("FEW050CB", clouds.code)
        assertEquals("Few Cumulonimbus at 5,000ft", clouds.description)
    }

    // test towering cumulus cloud type
    @Test
    fun testCloudsToweringCumulus() {
        val clouds = Clouds(15000, Clouds.Coverage.FEW, Clouds.Type.TOWERING_CUMULUS)
        assertEquals("FEW150TCU", clouds.code)
        assertEquals("Few Towering Cumulus at 15,000ft", clouds.description)
    }

    // test vertical visibility
    @Test
    fun testCloudsVerticalVisibility() {
        val clouds = Clouds(100, Clouds.Coverage.VERTICAL_VISIBILITY)
        assertEquals("VV001", clouds.code)
        assertEquals("Vertical visibility 100ft", clouds.description)
    }

    @Test
    fun testCloudsMeters() {
        val clouds = Clouds(100, Clouds.Coverage.FEW, units = AltitudeUnits.METERS)
        assertEquals("FEW001", clouds.code)
        assertEquals("Few at 100m", clouds.description)
    }
}