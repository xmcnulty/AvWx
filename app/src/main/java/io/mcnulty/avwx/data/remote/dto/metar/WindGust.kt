package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto

data class WindGust(
    val value: Int
) : Dto {
    override fun description(): String {
        return "WindGust - value: $value"
    }
}