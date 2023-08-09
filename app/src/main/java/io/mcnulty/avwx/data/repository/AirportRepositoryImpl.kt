package io.mcnulty.avwx.data.repository

import io.mcnulty.avwx.data.remote.AvWxApi
import io.mcnulty.avwx.data.remote.NetworkResponse
import io.mcnulty.avwx.domain.model.airport.AirportSummary
import io.mcnulty.avwx.domain.repository.airport.AirportRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AirportRepositoryImpl @Inject constructor(
    private val api: AvWxApi
) : AirportRepository {

    override suspend fun searchStation(
        query: String
    ): NetworkResponse<List<AirportSummary>> = try {
        NetworkResponse(
            body = api.searchStation(query = query)
        )
    } catch (e: HttpException) {
        when(e.code()) {
            400 -> NetworkResponse(
                errorMessage = "invalid search"
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
            errorMessage = "unknown error"
        )
    }
}