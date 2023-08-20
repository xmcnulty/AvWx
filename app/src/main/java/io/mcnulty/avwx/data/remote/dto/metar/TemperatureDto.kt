package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto

data class TemperatureDto(
    val repr: String,
    val spoken: String,
    val value: Int
) : Dto {
    override fun description() = "Temperature - $repr"
}
