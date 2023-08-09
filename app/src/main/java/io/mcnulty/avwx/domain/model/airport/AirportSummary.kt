package io.mcnulty.avwx.domain.model.airport

data class AirportSummary(
    val icao: String,
    val iata: String?,
    val name: String?,
    val city: String,
    val country: String
)
