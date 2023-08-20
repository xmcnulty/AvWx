package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName
import io.mcnulty.avwx.data.remote.dto.Dto

data class UnitsDto(
    val altimeter: String,
    val altitude: String,
    val temperature: String,
    val visibility: String,
    @SerializedName("wind_speed")
    val windSpeed: String
) : Dto {
    override fun description() = "Units - altimeter: $altimeter, altitude: $altitude, temperature: " +
            "$temperature, visibility: $visibility, windSpeed: $windSpeed"
}