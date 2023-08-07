package io.mcnulty.avwx.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mcnulty.avwx.data.remote.AvWxApi
import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import io.mcnulty.avwx.data.remote.removeQuery
import io.mcnulty.avwx.domain.repository.metar.MetarRepository
import io.mcnulty.avwx.domain.use_case.parse.MetarParser
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

class MetarRepositoryImplTest {

    private lateinit var repository: MetarRepository
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
        repository = MetarRepositoryImpl(testApi)
    }

    @Test
    fun `test api returns error 404`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            .setBody("Not Found")

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.getMetar("KJFK")
        }

        assertThat(actualResponse.errorMessage).isEqualTo("server error")
    }

    @Test
    fun `test empty json body`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{}")

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.getMetar("KJFK")
        }

        assertThat(actualResponse.errorMessage).isEqualTo("parsing error")
    }

    @Test
    fun `test valid response with json metar body`() {
        val inputStream = InputStreamReader(javaClass.classLoader!!.getResourceAsStream("json/SampleKJFK.json"))
        val json = BufferedReader(inputStream).use { it.readText() }

        val mockMetar = MetarParser.toMetar(
            Gson().fromJson(json, MetarDto::class.java)
        )

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(json)

        mockWebServer.enqueue(expectedResponse)

        inputStream.close()

        val actualResponse = runBlocking {
            repository.getMetar("KJFK")
        }

        assertThat(actualResponse.body).isNotNull()
        assertThat(actualResponse.body).isEqualTo(mockMetar)
    }

    @Test
    fun `test IOException thrown`() {
        val inputStream = InputStreamReader(javaClass.classLoader!!.getResourceAsStream("json/SampleKJFK.json"))
        val json = BufferedReader(inputStream).use { it.readText() }

        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(json)
            .setSocketPolicy(SocketPolicy.DISCONNECT_DURING_RESPONSE_BODY)

        mockWebServer.enqueue(expectedResponse)

        inputStream.close()

        val actualResponse = runBlocking {
            repository.getMetar("KJFK")
        }

        assertThat(actualResponse.errorMessage).isEqualTo("connection error")
    }

    @Test
    fun `check valid remove query`() {
        assertThat(removeQuery)
            .isEqualTo("spoken%2Cother%2Csanitized%2Cremarks_info%2Cdt%2Caccumulation")
    }

    @Test
    fun `test invalid search parameter`() {
        val expectedResponse = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(
                """{
                      "error": "K is not a valid ICAO, IATA, or GPS code",
                      "param": "station",
                      "help": "ICAO, IATA, GPS code, or coord pair. Ex: KJFK, LHR, or \"12.34,-12.34\"",
                      "timestamp": "2023-08-06T16:54:38.621146Z"
                    }""".trimIndent()
            )

        mockWebServer.enqueue(expectedResponse)

        val actualResponse = runBlocking {
            repository.getMetar("K")
        }

        assertThat(actualResponse.errorMessage)
            .isEqualTo("invalid ICAO, IATA, or GPS code")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}