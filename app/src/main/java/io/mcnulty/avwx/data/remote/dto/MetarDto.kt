package io.mcnulty.avwx.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MetarDto(
    val altimeter: Altimeter,
    @SerializedName("clouds")
    val cloudDtos: List<CloudDto>,
    @SerializedName("density_altitude")
    val densityAltitude: Int,
    val dewpoint: Dewpoint,
    @SerializedName("flight_rules")
    val flightRules: String,
    val info: FieldInfoDto,
    val meta: Meta,
    val other: List<Any>,
    @SerializedName("pressure_altitude")
    val pressureAltitude: Int,
    val raw: String,
    @SerializedName("relative_humidity")
    val relativeHumidity: Double,
    val remarks: String,
    @SerializedName("remarks_info")
    val remarksInfo: RemarksInfo,
    @SerializedName("runway_visibility")
    val runwayVisibility: List<RunwayVisibilityDto>,
    val sanitized: String,
    val station: String,
    val temperature: Temperature,
    val time: Time,
    val units: Units,
    val visibility: Visibility,
    @SerializedName("wind_direction")
    val windDirection: WindDirection,
    @SerializedName("wind_gust")
    val windGust: WindGust?,
    @SerializedName("wind_speed")
    val windSpeed: WindSpeed,
    @SerializedName("wind_variable_direction")
    val windVariableDirection: List<WindVariableDirection>,
    @SerializedName("wx_codes")
    val weatherCodes: List<Any>
) {
    data class Altimeter(
        val repr: String,
        val spoken: String,
        val value: Number
    )

    data class CloudDto(
        val altitude: Int,
        val modifier: String,
        val repr: String,
        val type: String
    )

    data class Dewpoint(
        val repr: String,
        val spoken: String,
        val value: Int
    )

    data class Meta(
        @SerializedName("cache_timestamp")
        val cacheTimestamp: String,
        @SerializedName("stations_updated")
        val stationsUpdated: String,
        val timestamp: String
    )

    data class RemarksInfo(
        val codes: List<Code>,
        @SerializedName("dewpoint_decimal")
        val dewpointDecimal: DewpointDecimal,
        @SerializedName("maximum_temperature_24")
        val maximumTemperature24: Any,
        @SerializedName("maximum_temperature_6")
        val maximumTemperature6: Any,
        @SerializedName("minimum_temperature_24")
        val minimumTemperature24: Any,
        @SerializedName("minimum_temperature_6")
        val minimumTemperature6: Any,
        @SerializedName("precip_24_hours")
        val precip24Hours: Any,
        @SerializedName("precip_36_hours")
        val precip36Hours: Any,
        @SerializedName("precip_hourly")
        val precipHourly: Any,
        @SerializedName("pressure_tendency")
        val pressureTendency: PressureTendency,
        @SerializedName("sea_level_pressure")
        val seaLevelPressure: SeaLevelPressure,
        @SerializedName("snow_depth")
        val snowDepth: Any,
        @SerializedName("sunshine_minutes")
        val sunshineMinutes: Any,
        @SerializedName("temperature_decimal")
        val temperatureDecimal: TemperatureDecimal
    ) {
        data class Code(
            val repr: String,
            val value: String
        )

        data class DewpointDecimal(
            val repr: String,
            val spoken: String,
            val value: Double
        )

        data class PressureTendency(
            val change: Double,
            val repr: String,
            val tendency: String
        )

        data class SeaLevelPressure(
            val repr: String,
            val spoken: String,
            val value: Int
        )

        data class TemperatureDecimal(
            val repr: String,
            val spoken: String,
            val value: Double
        )
    }

    data class RunwayVisibilityDto(
        val repr: String,
        val runway: String,
        val visibility: RunwayVisibility?,
        @SerializedName("variable_visibility")
        val variableVisibility: List<RunwayVisibility>,
        val trend: Any
    ) {
        data class RunwayVisibility(
            val repr: String,
            val spoken: String,
            val value: Int
        )
    }

    data class Temperature(
        val repr: String,
        val spoken: String,
        val value: Int
    )

    data class Time(
        val dt: String,
        val repr: String
    )

    data class Units(
        val accumulation: String,
        val altimeter: String,
        val altitude: String,
        val temperature: String,
        val visibility: String,
        @SerializedName("wind_speed")
        val windSpeed: String
    )

    data class Visibility(
        val repr: String,
        val spoken: String,
        val value: Int
    )

    data class WindDirection(
        val repr: String,
        val spoken: String,
        val value: Int
    )

    data class WindGust(
        val repr: String,
        val spoken: String,
        val value: Int
    )

    data class WindSpeed(
        val repr: String,
        val spoken: String,
        val value: Int
    )

    data class WindVariableDirection(
        val repr: String,
        val spoken: String,
        val value: Int
    )
}