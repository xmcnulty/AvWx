package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock

data class Weather(
    override val code: String,
    override val description: String
) : MetarBlock {
    override fun hashCode(): Int = code.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other !is Weather) return false
        return code == other.code
    }
}