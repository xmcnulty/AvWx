package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.MetarDto
import io.mcnulty.avwx.domain.model.metar.fields.Winds
import io.mcnulty.avwx.domain.model.metar.measurement.WindSpeedUnits

/**
 * Converts wind from a [MetarDto] to a [Winds] domain object.
 */
internal object WindParser {
    /**
     * Parses wind from a [MetarDto] to a [Winds] domain object.
     *
     * @param direction The wind direction
     * @param speed The wind speed
     * @param gust The wind gust
     * @param variableDirections The variable [MetarDto.WindVariableDirection] contained in the [MetarDto]
     * @param units The [WindSpeedUnits] to use
     * @return The parsed [Winds]
     */
    fun parse(
        direction: Int,
        speed: Int,
        gust: Int?,
        variableDirections: List<MetarDto.WindVariableDirection>,
        units: WindSpeedUnits
    ): Winds {
        val variation = if(variableDirections.size == 2) {
            Winds.Variation(variableDirections[0].value, variableDirections[1].value)
        } else {
            null
        }

        return Winds(
            direction = direction,
            speed = speed,
            gust = gust,
            variation = variation,
            units = units
        )
    }
}