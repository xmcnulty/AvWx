package io.mcnulty.avwx.data.remote.dto.metar

data class MetarDtoComponent(
    val repr: String,
    val spoken: String,
    val value: Number
)