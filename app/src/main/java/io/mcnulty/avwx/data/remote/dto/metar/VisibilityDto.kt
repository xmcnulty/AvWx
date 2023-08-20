package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto

data class VisibilityDto(
    val repr: String,
    val value: Int
) : Dto {
    override fun description() = "Visibility - $repr"
}