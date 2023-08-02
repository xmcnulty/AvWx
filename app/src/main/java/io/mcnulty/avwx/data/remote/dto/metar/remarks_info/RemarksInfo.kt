package io.mcnulty.avwx.data.remote.dto.metar.remarks_info

import com.google.gson.annotations.SerializedName

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
    val temperatureDecimal: TemperatureDecimal?
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
