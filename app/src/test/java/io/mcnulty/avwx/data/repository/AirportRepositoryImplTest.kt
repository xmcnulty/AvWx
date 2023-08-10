package io.mcnulty.avwx.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mcnulty.avwx.data.remote.AvWxApi
import io.mcnulty.avwx.domain.model.airport.AirportSummary
import io.mcnulty.avwx.domain.repository.airport.AirportRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

class AirportRepositoryImplTest {
    private lateinit var repository: AirportRepository
    private lateinit var testApi: AvWxApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        testApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AvWxApi::class.java)
        repository = AirportRepositoryImpl(testApi)
    }

    @Test
    fun `test server error`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody("Not Found")

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.searchStation("KJFK")
        }

        assertThat(actualResponse.errorMessage).isEqualTo("server error")
    }

    @Test
    fun `test no results - empty list`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("[]")

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.searchStation("NO MATCH")
        }

        assertThat(actualResponse.body).isEmpty()
    }

    @Test
    fun `test invalid search query - less than three characters`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(
                """{
                      "error": "length of value must be at least 3",
                      "param": "text",
                      "help": "Station search string. Ex: orlando%20kmco",
                      "timestamp": "2023-08-09T15:38:50.967455Z"
                    }""".trimIndent()
            )

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.searchStation("K")
        }

        assertThat(actualResponse.body).isNull()
        assertThat(actualResponse.errorMessage)
            .isEqualTo("invalid search")
    }

    @Test
    fun `test connection error`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("[]")
            .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.searchStation("KJFK")
        }

        assertThat(actualResponse.errorMessage).isEqualTo("connection error")
    }

    @Test
    fun `test search city and state`() {
        val inputStream = InputStreamReader(
            javaClass.classLoader!!.getResourceAsStream(
                "json/airport_search/lihue_hi_search.json"
            )
        )
        val json = BufferedReader(inputStream).use { it.readText() }

        val expectedList = Gson().fromJson(
            json, Array<AirportSummary>::class.java
        ).toList()

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(json)

        mockWebServer.enqueue(expectedResponse)

        inputStream.close()

        val actualResponse = runBlocking {
            repository.searchStation("lihue hi")
        }

        assertThat(actualResponse.body).isNotNull()
        assertThat(actualResponse.body).isEqualTo(expectedList)
    }
}