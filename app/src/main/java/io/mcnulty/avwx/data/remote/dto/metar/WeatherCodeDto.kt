package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto

data class WeatherCodeDto(
    val repr: String,
    val value: String
) : Dto {
    override fun description() = "WeatherCode - $repr"
}