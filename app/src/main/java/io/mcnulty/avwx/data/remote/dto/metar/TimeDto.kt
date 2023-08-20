package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime

data class TimeDto(
    val repr: String
) : Dto {
    fun toMetarTime(): MetarTime {
        val day = repr.substring(0, 2).toInt()
        val hour = repr.substring(2, 4).toInt()
        val minute = repr.substring(4, 6).toInt()

        return MetarTime(day, hour, minute)
    }

    override fun description() = "Metar time - $repr"
}