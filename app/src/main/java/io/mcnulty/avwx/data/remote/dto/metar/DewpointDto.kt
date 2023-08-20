package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto

data class DewpointDto(
    val value: Int
) : Dto {
    override fun description(): String {
        return "Dewpoint - value: $value"
    }
}
