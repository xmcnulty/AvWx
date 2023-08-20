package io.mcnulty.avwx.data.repository

import io.mcnulty.avwx.data.remote.AvWxApi
import io.mcnulty.avwx.data.remote.NetworkResponse
import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.repository.metar.MetarRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetarRepositoryImpl @Inject constructor(
    private val api: AvWxApi
) : MetarRepository {

    override suspend fun getMetar(fieldCode: String): NetworkResponse<Metar> = try {
        NetworkResponse(
            body = api.getMetar(fieldCode).toMetar()
        )
    } catch (e: HttpException) {
        when(e.code()) {
            400 -> NetworkResponse(
                errorMessage = "invalid ICAO, IATA, or GPS code"
            )
            else -> NetworkResponse(
                errorMessage = "server error"
            )
        }
    } catch (e: IOException) {
        NetworkResponse(
            errorMessage = "connection error"
        )
    } catch (e: Exception) {
        NetworkResponse(
            errorMessage = "parsing error"
        )
    }
}