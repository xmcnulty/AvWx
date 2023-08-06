package io.mcnulty.avwx.data.repository

import io.mcnulty.avwx.data.remote.ApiResponse
import io.mcnulty.avwx.data.remote.AvWxApi
import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.repository.metar.MetarRepository
import io.mcnulty.avwx.domain.use_case.parse.MetarParser
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetarRepositoryImpl @Inject constructor(
    private val api: AvWxApi
) : MetarRepository {

    override suspend fun getMetar(fieldCode: String): ApiResponse<Metar> = try {
        ApiResponse(
            body = MetarParser.toMetar(api.getMetar(fieldCode))
        )
    } catch (e: HttpException) {
        when(e.code()) {
            400 -> ApiResponse(
                httpCode = e.code(),
                errorMessage = "ICAO, IATA, or GPS code not found"
            )
            else -> ApiResponse(
                httpCode = e.code(),
                errorMessage = "server error"
            )
        }
    } catch (e: IOException) {
        ApiResponse(
            errorMessage = "connection error"
        )
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResponse(
            errorMessage = "parsing error"
        )
    }
}