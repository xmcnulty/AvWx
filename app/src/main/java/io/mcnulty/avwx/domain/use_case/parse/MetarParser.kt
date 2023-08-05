package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import io.mcnulty.avwx.data.remote.dto.metar.UnitsDto
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
/**
 * Converts a [MetarDto] to a [Metar] domain object.
 *
 */
object MetarParser {
    private const val ALTITUDE_KEY = "altitude"
    private const val PRESSURE_KEY = "pressure"
    private const val TEMPERATURE_KEY = "temperature"
    private const val VISIBILITY_KEY = "visibility"
    private const val WIND_SPEED_KEY = "windSpeed"


    /**
     * Converts a [MetarDto] to a [Metar] domain object. This is the main entry point for the
     * converter.
     *
     * @param dto The [MetarDto] to convert
     * @return The converted [Metar]
     */
    fun toMetar(dto: MetarDto): Metar {
        val unitsMap: Map<String, MetarUnits> = parseUnits(dto.unitsDto)

        return Metar(
            raw = dto.raw,
            time = MetarTimeParser.parse(dto.timeDto.repr),
            stationIdentifier = dto.station,
            wind = WindParser.parse(
                direction = dto.windDirection.value,
                speed = dto.windSpeed.value,
                gust = dto.windGust?.value,
                variableDirections = dto.windVariableDirection,
                units = unitsMap[WIND_SPEED_KEY] as WindSpeedUnits
            ),
            visibility = Visibility(
                code = dto.visibilityDto.repr,
                value = dto.visibilityDto.value.toDouble(),
                units = unitsMap[VISIBILITY_KEY] as VisibilityUnits
            ),
            runwayVisualRange = RVRParser.parse(
                dto.runwayVisibility,
                unitsMap[ALTITUDE_KEY] as AltitudeUnits
            ),
            altimeter = AltimeterParser.parse(dto.altimeterDto, unitsMap[PRESSURE_KEY] as PressureUnits),
            clouds = CloudParser.parse(dto.cloudDtos),
            dewPoint = dto.dewpointDto.value,
            temperature = TemperatureParser.parse(
                dto,
                unitsMap[TEMPERATURE_KEY] as TemperatureUnits
            ),
            remarks = dto.remarks,
            flightRules = FlightRules.fromString(dto.flightRules),
            relativeHumidity = dto.relativeHumidity,
            weather = dto.weatherCodeDtos.map { wxCode ->
                Weather(code = wxCode.repr, description = wxCode.value)
            }
        )
    }

    /**
     * Parses the units from a [MetarDto] to a [Map] of [String] to [MetarUnits] to be used
     * throughout the conversion process.
     *
     * @param unitsDto The [UnitsDto] to parse
     * @return The parsed [Map] of [String] to [MetarUnits]
     */
    private fun parseUnits(unitsDto: UnitsDto): Map<String, MetarUnits> {
        return mapOf(
            ALTITUDE_KEY to AltitudeUnits.fromString(unitsDto.altitude),
            TEMPERATURE_KEY to TemperatureUnits.fromString(unitsDto.temperature),
            PRESSURE_KEY to PressureUnits.fromString(unitsDto.altimeter),
            VISIBILITY_KEY to VisibilityUnits.fromString(unitsDto.visibility),
            WIND_SPEED_KEY to WindSpeedUnits.fromString(unitsDto.windSpeed)
        )
    }
}