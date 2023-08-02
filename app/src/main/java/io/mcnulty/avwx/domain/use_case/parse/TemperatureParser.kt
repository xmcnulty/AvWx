package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import io.mcnulty.avwx.domain.model.metar.fields.Temperature
import io.mcnulty.avwx.domain.model.metar.measurement.TemperatureUnits

/**
 * Converts temperature from a [MetarDto] to a [Temperature] domain object.
 */
internal object TemperatureParser {
    /**
     * Parses temperature from a [MetarDto] to a [Temperature] domain object.
     *
     * @param dto The [MetarDto] to parse
     * @param units The [TemperatureUnits] to use
     * @return The parsed [Temperature]
     */
    fun parse(
        dto: MetarDto,
        units: TemperatureUnits
    ): Temperature {
        return dto.remarksInfo.temperatureDecimal?.let { tempDecimal ->
            Temperature(tempDecimal.value, units)
        } ?: Temperature(dto.temperatureDto.value.toDouble(), units)
    }
}