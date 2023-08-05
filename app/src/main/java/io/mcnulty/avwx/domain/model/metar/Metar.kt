package io.mcnulty.avwx.domain.model.metar

import io.mcnulty.avwx.domain.model.metar.fields.AtmosphericPressure
import io.mcnulty.avwx.domain.model.metar.fields.Clouds
import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime
import io.mcnulty.avwx.domain.model.metar.fields.RunwayVisualRange
import io.mcnulty.avwx.domain.model.metar.fields.Temperature
import io.mcnulty.avwx.domain.model.metar.fields.Visibility
import io.mcnulty.avwx.domain.model.metar.fields.Weather
import io.mcnulty.avwx.domain.model.metar.fields.Winds

/**
 * Data class representation of a METAR report.
 * @author xmcnulty
 *
 * @property raw The raw METAR report string
 * @property stationIdentifier The ICAO station identifier
 * @property type The type of METAR report
 * @property time The time the METAR was issued
 * @property wind The wind conditions
 * @property visibility The visibility conditions
 * @property runwayVisualRange The runway visual range conditions
 * @property weather The weather conditions
 * @property clouds The cloud conditions
 * @property temperature The temperature conditions
 * @property dewPoint The dew point conditions
 * @property altimeter The altimeter conditions
 * @property remarks The remarks section of the METAR report
 * @property relativeHumidity The relative humidity conditions
 * @property flightRules The flight rules conditions
 */
data class Metar(
    val raw: String,
    val stationIdentifier: String,
    val type: MetarType = MetarType.METAR,
    val time: MetarTime,
    val wind: Winds,
    val visibility: Visibility,
    val runwayVisualRange: List<RunwayVisualRange>,
    val weather: List<Weather>,
    val clouds: List<Clouds>,
    val temperature: Temperature,
    val dewPoint: Number,
    val altimeter: AtmosphericPressure,
    val remarks: String? = null,
    val relativeHumidity: Number,
    val flightRules: FlightRules
) {
    override fun equals(other: Any?): Boolean = other?.let {

        // check if the other object is a Metar
        if (it !is Metar) {
            false
        } else {
            it.raw == this.raw
        }
    } ?: false
    override fun hashCode(): Int {
        var result = raw.hashCode()
        result = 31 * result + stationIdentifier.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + wind.hashCode()
        result = 31 * result + visibility.hashCode()
        result = 31 * result + runwayVisualRange.hashCode()
        result = 31 * result + weather.hashCode()
        result = 31 * result + clouds.hashCode()
        result = 31 * result + temperature.hashCode()
        result = 31 * result + dewPoint.hashCode()
        result = 31 * result + altimeter.hashCode()
        result = 31 * result + (remarks?.hashCode() ?: 0)
        result = 31 * result + relativeHumidity.hashCode()
        result = 31 * result + flightRules.hashCode()
        return result
    }
}
