package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.MetarDto
import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.Visibility
import io.mcnulty.avwx.domain.model.metar.fields.Weather
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits
import io.mcnulty.avwx.domain.model.metar.measurement.MetarUnits
import io.mcnulty.avwx.domain.model.metar.measurement.PressureUnits
import io.mcnulty.avwx.domain.model.metar.measurement.TemperatureUnits
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits
import io.mcnulty.avwx.domain.model.metar.measurement.WindSpeedUnits

internal const val ALTITUDE_KEY = "altitude"
internal const val PRESSURE_KEY = "pressure"
internal const val TEMPERATURE_KEY = "temperature"
internal const val VISIBILITY_KEY = "visibility"
internal const val WIND_SPEED_KEY = "windSpeed"

/**
 * Converts a [MetarDto] to a [Metar] domain object.
 *
 */
object MetarParser {
    /**
     * Converts a [MetarDto] to a [Metar] domain object. This is the main entry point for the
     * converter.
     *
     * @param dto The [MetarDto] to convert
     * @return The converted [Metar]
     */
    fun toMetar(dto: MetarDto): Metar {
        val unitsMap: Map<String, MetarUnits> = parseUnits(dto.units)

        return Metar(
            raw = dto.raw,
            time = MetarTimeParser.parse(dto.time.repr),
            stationIdentifier = dto.station,
            wind = WindParser.parse(
                direction = dto.windDirection.value,
                speed = dto.windSpeed.value,
                gust = dto.windGust?.value,
                variableDirections = dto.windVariableDirection,
                units = unitsMap["windSpeed"] as WindSpeedUnits
            ),
            visibility = Visibility(
                code = dto.visibility.repr,
                value = dto.visibility.value.toDouble(),
                units = unitsMap["visibility"] as VisibilityUnits
            ),
            runwayVisualRange = RVRParser.parse(
                dto.runwayVisibility,
                unitsMap["altitude"] as VisibilityUnits
            ),
            altimeter = AltimeterParser.parse(dto.altimeter, unitsMap["pressure"] as PressureUnits),
            clouds = CloudParser.parse(dto.cloudDtos),
            dewPoint = dto.dewpoint.value,
            temperature = TemperatureParser.parse(
                dto,
                unitsMap[TEMPERATURE_KEY] as TemperatureUnits
            ),
            remarks = dto.remarks,
            flightRules = FlightRules.fromString(dto.flightRules),
            relativeHumidity = dto.relativeHumidity,
            weather = dto.weatherCodes.map { wxCode ->
                Weather(code = wxCode.repr, description = wxCode.value)
            }
        )
    }

    /**
     * Parses the units from a [MetarDto] to a [Map] of [String] to [MetarUnits] to be used
     * throughout the conversion process.
     *
     * @param units The [MetarDto.Units] to parse
     * @return The parsed [Map] of [String] to [MetarUnits]
     */
    private fun parseUnits(units: MetarDto.Units): Map<String, MetarUnits> {
        return mapOf(
            ALTITUDE_KEY to AltitudeUnits.fromString(units.altitude),
            TEMPERATURE_KEY to TemperatureUnits.fromString(units.temperature),
            PRESSURE_KEY to PressureUnits.fromString(units.altimeter),
            VISIBILITY_KEY to VisibilityUnits.fromString(units.visibility),
            WIND_SPEED_KEY to WindSpeedUnits.fromString(units.windSpeed)
        )
    }
}