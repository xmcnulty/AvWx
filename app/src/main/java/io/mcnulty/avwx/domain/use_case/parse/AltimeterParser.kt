package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.metar.AltimeterDto
import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
import io.mcnulty.avwx.domain.model.metar.measurement.PressureUnits

/**
 * Converts altimeter from a [MetarDto] to a [AtmosphericPressure] domain object.
 */
internal object AltimeterParser {
    /**
     * Parses altimeter from a [MetarDto] to a [AtmosphericPressure] domain object.
     *
     * @param altimeterDto The [AltimeterDto] to parse
     * @param units The [PressureUnits] to use
     * @return The parsed [AtmosphericPressure]
     */
    fun parse(
        altimeterDto: AltimeterDto,
        units: PressureUnits
    ): AtmosphericPressure = when(units) {
        PressureUnits.INCHES_OF_MERCURY -> AtmosphericPressure.Altimeter(altimeterDto.value.toDouble())
        PressureUnits.HECTOPASCALS -> AtmosphericPressure.Qnh(altimeterDto.value.toInt())
    }
}