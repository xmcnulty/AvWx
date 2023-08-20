package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName
import io.mcnulty.avwx.common.toFlightRules
import io.mcnulty.avwx.data.remote.dto.Dto
import io.mcnulty.avwx.domain.exceptions.MetarParsingException
import io.mcnulty.avwx.domain.model.airport.AirportSummary
import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
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

private const val ALTITUDE_KEY = "altitude"
private const val PRESSURE_KEY = "pressure"
private const val TEMPERATURE_KEY = "temperature"
private const val VISIBILITY_KEY = "visibility"
private const val WIND_SPEED_KEY = "wind_speed"

data class MetarDto(
    @SerializedName("altimeter")
    val altimeterDto: AltimeterDto,
    @SerializedName("clouds")
    val cloudDtos: List<CloudDto>,
    @SerializedName("density_altitude")
    val densityAltitude: Int,
    @SerializedName("dewpoint")
    val dewpointDto: DewpointDto,
    @SerializedName("flight_rules")
    val flightRules: String,
    @SerializedName("pressure_altitude")
    val pressureAltitude: Int,
    val raw: String,
    @SerializedName("relative_humidity")
    val relativeHumidity: Double,
    val remarks: String,
    @SerializedName("runway_visibility")
    val runwayVisibility: List<RunwayVisibilityDto>,
    val station: String,
    @SerializedName("temperature")
    val temperatureDto: TemperatureDto,
    @SerializedName("time")
    val timeDto: TimeDto,
    @SerializedName("units")
    val unitsDto: UnitsDto,
    @SerializedName("visibility")
    val visibilityDto: VisibilityDto,
    @SerializedName("wind_direction")
    val windDirection: WindDirection,
    @SerializedName("wind_gust")
    val windGust: WindGust?,
    @SerializedName("wind_speed")
    val windSpeed: WindSpeed,
    @SerializedName("wind_variable_direction")
    val windVariableDirection: List<WindVariableDirection>,
    @SerializedName("wx_codes")
    val weatherCodeDtos: List<WeatherCodeDto>,
    val info: AirportSummary?
) : Dto {
    private val metarUnitsMap: Map<String, MetarUnits>
        get() = parseUnits()

    fun toMetar(): Metar = Metar(
        raw = this.raw,
        airportSummary = this.info,
        stationIdentifier = this.station,
        time = timeDto.toMetarTime(),
        wind = convertWinds(),
        visibility = Visibility(
            code = visibilityDto.repr,
            value = visibilityDto.value.toDouble(),
            units = metarUnitsMap[VISIBILITY_KEY] as VisibilityUnits
        ),
        altimeter = AtmosphericPressure.build(altimeterDto.repr),
        runwayVisualRange = runwayVisibility.map { rvrDto ->
            rvrDto.toRunwayVisualRange(metarUnitsMap[ALTITUDE_KEY] as AltitudeUnits)
        },
        weather = weatherCodeDtos.map { dto ->
            Weather(code = dto.repr, description = dto.value)
        },
        clouds = cloudDtos.map { dto -> dto.toCloud() },
        temperature = Temperature(
            temp = temperatureDto.value,
            units = metarUnitsMap[TEMPERATURE_KEY] as TemperatureUnits
        ),
        dewpoint = dewpointDto.value,
        remarks = remarks,
        relativeHumidity = relativeHumidity,
        flightRules = flightRules.toFlightRules()
    )

    override fun description(): String = "Metar: $raw"

    /**
     * Parses the units from a [MetarDto] to a [Map] of [String] to [MetarUnits] to be used
     * throughout the conversion process.
     *
     * @return The parsed [Map] of [String] to [MetarUnits]
     */
    private fun parseUnits(): Map<String, MetarUnits> {
        return try {
            mapOf(
                ALTITUDE_KEY to AltitudeUnits.fromString(unitsDto.altitude),
                TEMPERATURE_KEY to TemperatureUnits.fromString(unitsDto.temperature),
                PRESSURE_KEY to PressureUnits.fromString(unitsDto.altimeter),
                VISIBILITY_KEY to VisibilityUnits.fromString(unitsDto.visibility),
                WIND_SPEED_KEY to WindSpeedUnits.fromString(unitsDto.windSpeed)
            )
        } catch (e: Exception) {
            throw MetarParsingException(unitsDto)
        }
    }

    private fun convertWinds(): Winds {
        val direction = try { windDirection.value } catch (e: Exception) { throw MetarParsingException(windDirection) }
        val speed = try { windSpeed.value } catch (e: Exception) { throw MetarParsingException(windSpeed) }
        val gust = try { windGust?.value } catch (e: Exception) { throw MetarParsingException(windGust!!) }
        val speedUnits = try { metarUnitsMap[WIND_SPEED_KEY] as WindSpeedUnits } catch (e: Exception) { throw MetarParsingException(unitsDto) }

        val variation = when(windVariableDirection.size) {
            2 -> Winds.Variation(windVariableDirection[0].value, windVariableDirection[1].value)
            0 -> null
            else -> throw MetarParsingException(this)
        }

        return Winds(
            direction = direction,
            speed = speed,
            gust = gust,
            variation = variation,
            units = speedUnits
        )
    }
}