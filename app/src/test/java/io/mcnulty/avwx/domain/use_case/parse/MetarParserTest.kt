package io.mcnulty.avwx.domain.use_case.parse

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.airport.runway.RunwayPosition
import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
import io.mcnulty.avwx.domain.model.metar.fields.Clouds
import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime
import io.mcnulty.avwx.domain.model.metar.fields.RunwayVisualRange
import io.mcnulty.avwx.domain.model.metar.fields.Temperature
import io.mcnulty.avwx.domain.model.metar.fields.Winds
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits
import io.mcnulty.avwx.domain.model.metar.measurement.PressureUnits
import org.junit.After
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader

class MetarParserTest {
    private lateinit var inputStream: InputStreamReader

    private fun getMetarFromJsonFile(fileName: String): MetarDto {
        inputStream = InputStreamReader(javaClass.classLoader.getResourceAsStream("json/$fileName"))
        val json = BufferedReader(inputStream).use { it.readText() }


        return Gson().fromJson(json, MetarDto::class.java)
    }

    @Test
    fun `test sample KJFK - has weather, clouds, and gusts`() {

        val metarDto = getMetarFromJsonFile("SampleKJFK.json")

        val metar = MetarParser.toMetar(metarDto)

        assertThat(metar.stationIdentifier).isEqualTo("KJFK")

        // check altimeter
        assertThat(metar.altimeter).isInstanceOf(AtmosphericPressure.Altimeter::class.java)
        val altimeter = metar.altimeter as AtmosphericPressure.Altimeter
        assertThat(altimeter.value).isEqualTo(29.66)

        // check clouds
        assertThat(metar.clouds).hasSize(3)
        assertThat(metar.clouds[0].coverage).isEqualTo(Clouds.Coverage.FEW)
        assertThat(metar.clouds[0].altitude).isEqualTo(2400)
        assertThat(metar.clouds[1].coverage).isEqualTo(Clouds.Coverage.BROKEN)
        assertThat(metar.clouds[1].altitude).isEqualTo(3600)
        assertThat(metar.clouds[2].coverage).isEqualTo(Clouds.Coverage.OVERCAST)
        assertThat(metar.clouds[2].altitude).isEqualTo(4600)
        metar.clouds.forEach {
            assertThat(it.type).isNull()
            assertThat(it.units).isEqualTo(AltitudeUnits.FEET)
        }

        // check weather
        assertThat(metar.weather).hasSize(1)
        assertThat(metar.weather[0].code).isEqualTo("-RA")
        assertThat(metar.weather[0].description).isEqualTo("Light Rain")

        // check flight rules
        assertThat(metar.flightRules).isEqualTo(FlightRules.VFR)

        // check temp
        val expectedTemp = Temperature(10)
        assertThat(metar.temperature).isEqualTo(expectedTemp)

        // runway visibility
        assertThat(metar.runwayVisualRange).isEmpty()

        // check winds
        val expectedWinds = Winds(
            direction = 350,
            speed = 21,
            gust = 29
        )
        assertThat(metar.wind).isEqualTo(expectedWinds)

        // check time
        val expectedTime = MetarTime(
            day = 3,
            hour = 15,
            minute = 51
        )
        assertThat(metar.time).isEqualTo(expectedTime)
    }

    @Test
    fun `test sample EGLL - no clouds, no weather, no gusts`() {

            val metarDto = getMetarFromJsonFile("SampleEGLL.json")

            val metar = MetarParser.toMetar(metarDto)

            assertThat(metar.stationIdentifier).isEqualTo("EGLL")

            // check altimeter
            assertThat(metar.altimeter).isInstanceOf(AtmosphericPressure.Qnh::class.java)
            val altimeter = metar.altimeter as AtmosphericPressure.Qnh
            assertThat(altimeter.value).isEqualTo(1018)
            assertThat(altimeter.units).isEqualTo(PressureUnits.HECTOPASCALS)

            // check clouds
            assertThat(metar.clouds).isEmpty()

            // check weather
            assertThat(metar.weather).isEmpty()

            // check flight rules
            assertThat(metar.flightRules).isEqualTo(FlightRules.VFR)

            // check temp
            val expectedTemp = Temperature(17)
            assertThat(metar.temperature).isEqualTo(expectedTemp)

            // runway visibility
            assertThat(metar.runwayVisualRange).isEmpty()

            // check winds
            val expectedWinds = Winds(
                direction = 310,
                speed = 3
            )
            assertThat(metar.wind).isEqualTo(expectedWinds)

            // check time
            val expectedTime = MetarTime(
                day = 4,
                hour = 20,
                minute = 50
            )
            assertThat(metar.time).isEqualTo(expectedTime)

        // visibility
        assertThat(metar.visibility.description).isEqualTo("Greater than 10km")
    }

    @Test
    fun `test LOWG With Varying Winds`() {

            val metarDto = getMetarFromJsonFile("LOWG_WindVariation.json")

            val metar = MetarParser.toMetar(metarDto)

            assertThat(metar.stationIdentifier).isEqualTo("LOWG")

            // check altimeter
            assertThat(metar.altimeter).isInstanceOf(AtmosphericPressure.Qnh::class.java)
            val altimeter = metar.altimeter as AtmosphericPressure.Qnh
            assertThat(altimeter.value).isEqualTo(1011)
            assertThat(altimeter.units).isEqualTo(PressureUnits.HECTOPASCALS)

            // check clouds
            assertThat(metar.clouds).hasSize(1)
            assertThat(metar.clouds[0].code).isEqualTo("FEW002")

            // check weather
            assertThat(metar.weather).isEmpty()

            // check flight rules
            assertThat(metar.flightRules).isEqualTo(FlightRules.VFR)

            // check temp
            val expectedTemp = Temperature(16)
            assertThat(metar.temperature).isEqualTo(expectedTemp)

            // runway visibility
            assertThat(metar.runwayVisualRange).isEmpty()

            // check winds
            assertThat(metar.wind.code).isEqualTo("30005kt 260V320")

            // check time
            val expectedTime = MetarTime(
                day = 4,
                hour = 21,
                minute = 50
            )
            assertThat(metar.time).isEqualTo(expectedTime)

            // visibility
            assertThat(metar.visibility.description).isEqualTo("Greater than 10km")
    }

    @Test
    fun `test metar with runway visual range`() {

            val metarDto = getMetarFromJsonFile("KBOS_RVR.json")

            val metar = MetarParser.toMetar(metarDto)

            assertThat(metar.stationIdentifier).isEqualTo("KBOS")

            // check altimeter
            assertThat(metar.altimeter).isInstanceOf(AtmosphericPressure.Altimeter::class.java)
            val altimeter = metar.altimeter as AtmosphericPressure.Altimeter
            assertThat(altimeter.value).isEqualTo(29.41)

            // check clouds
            assertThat(metar.clouds).hasSize(2)
            assertThat(metar.clouds[0].coverage).isEqualTo(Clouds.Coverage.SCATTERED)
            assertThat(metar.clouds[0].altitude).isEqualTo(3000)
            assertThat(metar.clouds[1].coverage).isEqualTo(Clouds.Coverage.BROKEN)
            assertThat(metar.clouds[1].altitude).isEqualTo(8000)
            metar.clouds.forEach {
                assertThat(it.type).isNull()
                assertThat(it.units).isEqualTo(AltitudeUnits.FEET)
            }

            // check weather
            assertThat(metar.weather).hasSize(2)
            assertThat(metar.weather[0].code).isEqualTo("+RA")
            assertThat(metar.weather[0].description).isEqualTo("Heavy Rain")
            assertThat(metar.weather[1].code).isEqualTo("-TSRA")
            assertThat(metar.weather[1].description).isEqualTo("Light Thunderstorm Rain")

            // check flight rules
            assertThat(metar.flightRules).isEqualTo(FlightRules.VFR)

            // check temp
            val expectedTemp = Temperature(12)
            assertThat(metar.temperature).isEqualTo(expectedTemp)

            // runway visibility
            val expectedRvr = RunwayVisualRange(
                runway = Runway(4, RunwayPosition.RIGHT),
                minVisibility = RunwayVisualRange.RvRVisibility.build("6000"),
                maxVisibility = RunwayVisualRange.RvRVisibility.build("P6000")
            )

        assertThat(metar.runwayVisualRange).hasSize(1)
        assertThat(metar.runwayVisualRange[0]).isEqualTo(expectedRvr)

        assertThat(metar.wind.code).isEqualTo("27018G25kt 240V300")

        // check time
        val expectedTime = MetarTime(
            day = 27,
            hour = 14,
            minute = 54
        )
        assertThat(metar.time).isEqualTo(expectedTime)
    }

    @Test
    fun `test metar with CB cloud type`() {
        val metarDto = getMetarFromJsonFile("KMIA_CB_Clouds.json")

        val metar = MetarParser.toMetar(metarDto)

        assertThat(metar.stationIdentifier).isEqualTo("KMIA")

        // check clouds
        assertThat(metar.clouds).hasSize(4)
        assertThat(metar.clouds[0].coverage).isEqualTo(Clouds.Coverage.BROKEN)
        assertThat(metar.clouds[0].altitude).isEqualTo(3300)
        assertThat(metar.clouds[0].type).isEqualTo(Clouds.Type.CUMULONIMBUS)
    }

    @After
    fun tearDown() {
        inputStream.close()
    }
}
