package io.mcnulty.avwx.domain.use_case.metar

import com.google.common.truth.Truth.assertThat
import io.mcnulty.avwx.data.remote.NetworkResponse
import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime
import io.mcnulty.avwx.domain.model.metar.fields.Temperature
import io.mcnulty.avwx.domain.model.metar.fields.Visibility
import io.mcnulty.avwx.domain.model.metar.fields.Winds
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits
import io.mcnulty.avwx.domain.model.metar.measurement.WindSpeedUnits
import io.mcnulty.avwx.domain.repository.metar.MetarRepository
import io.mcnulty.avwx.shared.resource.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetDetailedMetarUseCaseTest {

    private lateinit var mockMetar: Metar

    private lateinit var mockRepository: MetarRepository

    private lateinit var useCase: GetDetailedMetarUseCase

    @Before
    fun setup() {
        mockMetar = Metar(
            raw = "KJFK 031551Z 35021G29KT 10SM -RA FEW024 BKN036 OVC046 10/07 A2966 RMK AO2 PK WND 36029/1550 RAB10 SLP042 P0000 T01000067",
            stationIdentifier = "KJFK",
            time = MetarTime(3, 15, 51),
            wind = Winds(
                direction = 350,
                speed = 21,
                gust = 29,
                units = WindSpeedUnits.KNOTS,
            ),
            visibility = Visibility("10SM", 10.0, VisibilityUnits.STATUTE_MILES),
            runwayVisualRange = emptyList(),
            clouds = emptyList(),
            altimeter = AtmosphericPressure.Altimeter(29.66),
            temperature = Temperature(10),
            dewpoint = 9,
            flightRules = FlightRules.VFR,
            relativeHumidity = 0.78989,
            weather = emptyList(),
            airportSummary = null
        )

        mockRepository = mockk()

        useCase = GetDetailedMetarUseCase(
            mockRepository
        )
    }

    @Test
    fun `test emits resource success with metar`() {
        coEvery { mockRepository.getMetar("KJFK") } returns NetworkResponse(
            body = mockMetar
        )

        runBlocking {
            val emissions = useCase(
                "KJFK"
            ).toList()

            assertThat(emissions[0]).isInstanceOf(Resource.Loading::class.java)
            assertThat(emissions[1]).isInstanceOf(Resource.Success::class.java)
            assertThat((emissions[1] as Resource.Success).data).isEqualTo(mockMetar)
        }
    }

    @Test
    fun `fun test emits resource error`() {
        coEvery { mockRepository.getMetar("K") } returns NetworkResponse(
            errorMessage = "invalid ICAO, IATA, or GPS code"
        )

        runBlocking {
            val emissions = useCase(
                "K"
            ).toList()

            assertThat(emissions[0]).isInstanceOf(Resource.Loading::class.java)
            assertThat(emissions[1]).isInstanceOf(Resource.Error::class.java)
            assertThat((emissions[1] as Resource.Error).message)
                .isEqualTo("invalid ICAO, IATA, or GPS code")
        }
    }
}