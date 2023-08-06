package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName

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
    val other: List<Any>,
    @SerializedName("pressure_altitude")
    val pressureAltitude: Int,
    val raw: String,
    @SerializedName("relative_humidity")
    val relativeHumidity: Double,
    val remarks: String,
    @SerializedName("runway_visibility")
    val runwayVisibility: List<RunwayVisibilityDto>,
    val sanitized: String,
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
    val weatherCodeDtos: List<WeatherCodeDto>
)