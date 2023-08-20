package io.mcnulty.avwx.data.remote.dto.metar

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
import io.mcnulty.avwx.domain.model.metar.fields.Clouds
import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime
import io.mcnulty.avwx.domain.model.metar.fields.RunwayVisualRange
import io.mcnulty.avwx.domain.model.metar.fields.Visibility
import io.mcnulty.avwx.domain.model.metar.fields.Winds
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits
import io.mcnulty.avwx.domain.model.metar.measurement.WindSpeedUnits
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Tests for converting [MetarDto] to Metar object.
 */
class MetarDtoTest {

    @Test
    fun `test KJFK`() {
        val inputStream = InputStreamReader(javaClass.classLoader!!.getResourceAsStream("json/SampleKJFK.json"))
        val json = BufferedReader(inputStream).use { it.readText() }

        val dto = Gson().fromJson(json, MetarDto::class.java)

        inputStream.close()

        val metar = dto.toMetar()

        // validate station
        assertThat(metar.stationIdentifier).isEqualTo("KJFK")

        // validate time
        val expectedTime = MetarTime(
            day = 3,
            hour = 15,
            minute = 51
        )

        assertThat(metar.time).isEqualTo(expectedTime)

        // validate wind
        val expectedWind = Winds(
            direction = 350,
            speed = 21,
            gust = 29
        )

        assertThat(metar.wind).isEqualTo(expectedWind)
        assertThat(metar.wind.units).isEqualTo(WindSpeedUnits.KNOTS)

        // validate visibility
        val expectedVisibility = Visibility(
            code = "10SM",
            value = 10.0,
            units = VisibilityUnits.STATUTE_MILES
        )

        assertThat(metar.visibility).isEqualTo(expectedVisibility)

        // validate weather conditions
        assertThat(metar.weather).hasSize(1)
        assertThat(metar.weather[0].code).isEqualTo("-RA")

        // validate cloud conditions
        val expectedClouds = listOf(
            Clouds(
                altitude = 2400,
                coverage = Clouds.Coverage.FEW
            ),
            Clouds(
                altitude = 3600,
                coverage = Clouds.Coverage.BROKEN
            ),
            Clouds(
                altitude = 4600,
                coverage = Clouds.Coverage.OVERCAST
            )
        )

        assertThat(metar.clouds).hasSize(3)
        assertThat(metar.clouds).isEqualTo(expectedClouds)

        // validate temperature
        assertThat(metar.temperature.temp).isEqualTo(10)

        // validate altimeter
        val expectedAltimeter = AtmosphericPressure.Altimeter(29.66)

        assertThat(metar.altimeter).isEqualTo(expectedAltimeter)

        // validate flight rules
        assertThat(metar.flightRules).isEqualTo(FlightRules.VFR)

        // validate the summary
        val metarSummary = metar.summary

        assertThat(metarSummary.icao).isEqualTo("KJFK")
        assertThat(metarSummary.time).isEqualTo(expectedTime)
        assertThat(metarSummary.rules).isEqualTo(FlightRules.VFR)
        assertThat(metarSummary.wind).isEqualTo(expectedWind)
        assertThat(metarSummary.temp).isEqualTo(metar.temperature)

        // validate airport summary
        assertThat(metar.airportSummary).isNotNull()
        assertThat(metar.airportSummary!!.name).isEqualTo("John F Kennedy International Airport")
        assertThat(metar.airportSummary!!.city).isEqualTo("New York")
        assertThat(metar.airportSummary!!.country).isEqualTo("US")
    }

    @Test
    fun `test european metar`() {
        val inputStream = InputStreamReader(javaClass.classLoader!!.getResourceAsStream("json/SampleEGLL.json"))
        val json = BufferedReader(inputStream).use { it.readText() }

        val dto = Gson().fromJson(json, MetarDto::class.java)

        inputStream.close()

        val metar = dto.toMetar()

        // validate QNH
        val expectedAltimeter = AtmosphericPressure.Qnh(1018)

        assertThat(metar.altimeter).isEqualTo(expectedAltimeter)

        // validate visibility
        val expectedVisibility = Visibility(
            code = "9999",
            value = 9999.0,
            units = VisibilityUnits.METERS
        )

        assertThat(metar.visibility).isEqualTo(expectedVisibility)

        // validate no clouds
        assertThat(metar.clouds).isEmpty()

        // valideate no weather conditions
        assertThat(metar.weather).isEmpty()
    }

    @Test
    fun `test parsing metar with wind variation`() {
        val inputStream = InputStreamReader(javaClass.classLoader!!.getResourceAsStream("json/LOWG_WindVariation.json"))
        val json = BufferedReader(inputStream).use { it.readText() }

        val dto = Gson().fromJson(json, MetarDto::class.java)

        inputStream.close()

        val metar = dto.toMetar()

        // validate winds
        val expectedWinds = Winds(
            direction = 300,
            speed = 5,
            variation = Winds.Variation(260, 320)
        )

        assertThat(metar.wind).isEqualTo(expectedWinds)
    }

    @Test
    fun `test metar with runway visibility conditions`() {
        val inputStream = InputStreamReader(javaClass.classLoader!!.getResourceAsStream("json/KBOS_RVR.json"))
        val json = BufferedReader(inputStream).use { it.readText() }

        val dto = Gson().fromJson(json, MetarDto::class.java)

        inputStream.close()

        val metar = dto.toMetar()

        // validate winds with gusts
        val expectedWinds = Winds(
            direction = 270,
            speed = 18,
            gust = 25,
            variation = Winds.Variation(240, 300)
        )

        assertThat(metar.wind).isEqualTo(expectedWinds)

        // validate runway visual range
        assertThat(metar.runwayVisualRange).hasSize(1)

        val expectedRunway = Runway.build("04R")

        val expectedRvr = RunwayVisualRange(
            runway = expectedRunway,
            minVisibility = RunwayVisualRange.RvRVisibility.build("6000"),
            maxVisibility = RunwayVisualRange.RvRVisibility.build("P6000")
        )

        assertThat(metar.runwayVisualRange[0]).isEqualTo(expectedRvr)
    }
}