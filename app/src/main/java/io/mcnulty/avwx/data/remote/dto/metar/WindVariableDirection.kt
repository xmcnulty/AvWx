package io.mcnulty.avwx.data.remote.dto.metar

data class WindVariableDirection(
    val repr: String,
    val spoken: String,
    val value: Int
)