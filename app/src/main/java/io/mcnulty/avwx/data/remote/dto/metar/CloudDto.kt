package io.mcnulty.avwx.data.remote.dto.metar

data class CloudDto(
    val altitude: Int,
    val modifier: String,
    val repr: String,
    val type: String
)