package io.mcnulty.avwx.domain.use_case

import io.mcnulty.avwx.data.remote.dto.MetarDto
import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
import io.mcnulty.avwx.domain.model.metar.fields.Clouds
import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime
import io.mcnulty.avwx.domain.model.metar.fields.RunwayVisualRange
import io.mcnulty.avwx.domain.model.metar.fields.Temperature
import io.mcnulty.avwx.domain.model.metar.fields.Visibility
import io.mcnulty.avwx.domain.model.metar.fields.Weather
import io.mcnulty.avwx.domain.model.metar.fields.Winds
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
object MetarConverter {
    /**
     * Converts a [MetarDto] to a [Metar] domain object. This is the main entry point for the
     * converter.
     *
     * @param dto The [MetarDto] to convert
     * @return The converted [Metar]
     */
    fun toMetar(dto: MetarDto): Metar {
        val units: Map<String, MetarUnits> = parseUnits(dto.units)

        return Metar(
            raw = dto.raw,
            time = parseTime(dto.time.repr),
            stationIdentifier = dto.station,
            wind = toWinds(
                direction = dto.windDirection.value,
                speed = dto.windSpeed.value,
                gust = dto.windGust?.value,
                variableDirections = dto.windVariableDirection,
                units = units["windSpeed"] as WindSpeedUnits
            ),
            visibility = Visibility(
                code = dto.visibility.repr,
                value = dto.visibility.value.toDouble(),
                units = units["visibility"] as VisibilityUnits
            ),
            runwayVisualRange = parseRunwayVisualRange(
                dto.runwayVisibility,
                units["altitude"] as VisibilityUnits
            ),
            altimeter = parseAltimeter(dto.altimeter, units["pressure"] as PressureUnits),
            clouds = fromClouds(dto.cloudDtos),
            dewPoint = dto.dewpoint.value,
            temperature = parseTemperature(dto, units[TEMPERATURE_KEY] as TemperatureUnits),
            remarks = dto.remarks,
            flightRules = FlightRules.fromString(dto.flightRules),
            relativeHumidity = dto.relativeHumidity,
            weather = dto.weatherCodes.map { wxCode ->
                Weather(code = wxCode.repr, description = wxCode.value)
            }
        )
    }

    private fun parseTemperature(
        dto: MetarDto,
        units: TemperatureUnits
    ): Temperature {
        return dto.remarksInfo.temperatureDecimal?.let { tempDecimal ->
            Temperature(tempDecimal.value, units)
        } ?: Temperature(dto.temperature.value.toDouble(), units)
    }

    private fun parseAltimeter(
        altimeterDto: MetarDto.Altimeter,
        units: PressureUnits
    ): AtmosphericPressure = when(units) {
        PressureUnits.INCHES_OF_MERCURY -> AtmosphericPressure.Altimeter(altimeterDto.value.toDouble())
        PressureUnits.HECTOPASCALS -> AtmosphericPressure.Qnh(altimeterDto.value.toInt())
    }

    private fun fromClouds(cloudDtos: List<MetarDto.CloudDto>): List<Clouds> {
        return cloudDtos.map { cloudDto ->
            Clouds(
                coverage = Clouds.Coverage.fromCode(cloudDto.type),
                altitude = cloudDto.altitude * 100,
                type = Clouds.Type.fromCode(cloudDto.modifier)
            )
        }
    }

    private fun parseTime(time: String): MetarTime {
        val day = time.substring(0, 2).toInt()
        val hour = time.substring(2, 4).toInt()
        val minute = time.substring(4, 6).toInt()

        return MetarTime(day, hour, minute)
    }

    private fun toWinds(
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

    private fun parseUnits(units: MetarDto.Units): Map<String, MetarUnits> {
        return mapOf(
            ALTITUDE_KEY to AltitudeUnits.fromString(units.altitude),
            TEMPERATURE_KEY to TemperatureUnits.fromString(units.temperature),
            PRESSURE_KEY to PressureUnits.fromString(units.altimeter),
            VISIBILITY_KEY to VisibilityUnits.fromString(units.visibility),
            WIND_SPEED_KEY to WindSpeedUnits.fromString(units.windSpeed)
        )
    }

    private fun parseRunwayVisualRange(
        rvrDto: List<MetarDto.RunwayVisibilityDto>,
        units: VisibilityUnits
    ): List<RunwayVisualRange> = rvrDto.map { dto ->
        val runway = Runway.build(dto.runway)

        if(dto.variableVisibility.isNotEmpty()) {
            val visibilities = dto.variableVisibility.map { visRange ->
                RunwayVisualRange.RvRVisibility.build(visRange.repr)
            }

            RunwayVisualRange(runway, visibilities[0], visibilities[1], units)
        }

        RunwayVisualRange(
            runway,
            RunwayVisualRange.RvRVisibility.build(dto.visibility!!.repr),
            units = units
        )
    }
}