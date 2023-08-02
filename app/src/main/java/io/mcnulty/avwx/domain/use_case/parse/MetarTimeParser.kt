package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.domain.model.metar.fields.MetarTime

/**
 * Converts raw string time from a METAR to a [MetarTime] domain object.
 */
internal object MetarTimeParser {
    /**
     * Parses time from a String to a [MetarTime] domain object.
     *
     * @param time The raw string to parse
     * @return The parsed [MetarTime]
     */
    fun parse(time: String): MetarTime {
        val day = time.substring(0, 2).toInt()
        val hour = time.substring(2, 4).toInt()
        val minute = time.substring(4, 6).toInt()

        return MetarTime(day, hour, minute)
    }
}