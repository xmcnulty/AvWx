package io.mcnulty.avwx.domain.repository.airport

import io.mcnulty.avwx.data.remote.NetworkResponse
import io.mcnulty.avwx.domain.model.airport.AirportSummary

interface AirportRepository {
    suspend fun searchStation(query: String): NetworkResponse<List<AirportSummary>>
}