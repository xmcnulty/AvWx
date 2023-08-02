package io.mcnulty.avwx.data.remote.dto.metar

data class TemperatureDto(
    val repr: String,
    val spoken: String,
    val value: Int
)
