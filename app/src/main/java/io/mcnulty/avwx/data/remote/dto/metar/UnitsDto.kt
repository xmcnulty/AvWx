package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName

data class UnitsDto(
    val accumulation: String,
    val altimeter: String,
    val altitude: String,
    val temperature: String,
    val visibility: String,
    @SerializedName("wind_speed")
    val windSpeed: String
)