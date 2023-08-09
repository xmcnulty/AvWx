package io.mcnulty.avwx.domain.use_case.airport

import com.google.common.truth.Truth.assertThat
import io.mcnulty.avwx.data.remote.NetworkResponse
import io.mcnulty.avwx.domain.model.airport.AirportSummary
import io.mcnulty.avwx.domain.repository.airport.AirportRepository
import io.mcnulty.avwx.shared.resource.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchAirportUseCaseTest {
    private lateinit var mockAirportList: List<AirportSummary>

    private lateinit var mockAirportRepository: AirportRepository

    private lateinit var searchAirportUseCase: SearchAirportUseCase

    @Before
    fun setup() {
        mockAirportList = listOf(
            AirportSummary(
                icao = "KBOS",
                iata = "BOS",
                name ="Logan International Airport",
                city ="Boston",
                country = "US"
            ),
            AirportSummary(
                "PAFS",
                "NIG",
                "Nikolai Airport",
                "Nikolai",
                "US"
            )
        )

        mockAirportRepository = mockk()

        searchAirportUseCase = SearchAirportUseCase(
            mockAirportRepository
        )
    }

    @Test
    fun `test emits resource with mock list`() {
        coEvery {
            mockAirportRepository.searchStation("KBOS")
        } returns NetworkResponse(mockAirportList)

        runBlocking {
            val emissions = searchAirportUseCase("KBOS").toList()

            assertThat(emissions).hasSize(2)

            assertThat(emissions[0]).isInstanceOf(Resource.Loading::class.java)

            assertThat(emissions[1]).isInstanceOf(Resource.Success::class.java)
            val success = emissions[1] as Resource.Success

            assertThat(success.data).isNotNull()
            val airportList = success.data!!

            assertThat(airportList).hasSize(2)

            assertThat(airportList.first()).isEqualTo(mockAirportList.first())
        }
    }

    @Test
    fun `test error`() {
        coEvery {
            mockAirportRepository.searchStation("k")
        } returns NetworkResponse(
            errorMessage = "error"
        )

        runBlocking {
            val emissions = searchAirportUseCase("k").toList()

            assertThat(emissions).hasSize(2)

            assertThat(emissions[0]).isInstanceOf(Resource.Loading::class.java)

            assertThat(emissions[1]).isInstanceOf(Resource.Error::class.java)
            val error = emissions[1] as Resource.Error

            assertThat(error.message).isEqualTo("error")
        }
    }
}