package io.mcnulty.avwx.domain.model.metar.fields

import androidx.compose.ui.graphics.Color
import io.mcnulty.avwx.domain.model.metar.MetarBlock

/**
 * Flight rules are used to determine the minimum weather conditions required for a flight to take
 * off and land. They are determined by the lowest visibility or ceiling (BKN or OVC) in the
 * METAR. If the lowest visibility is greater than 5 SM and the lowest ceiling is greater than
 * 3000 ft AGL, the flight rules are VFR. If the lowest visibility is greater than or equal to 3 SM
 * and the lowest ceiling is greater than or equal to 1000 ft AGL, the flight rules are MVFR. If
 * the lowest visibility is greater than or equal to 1 SM and the lowest ceiling is greater than or
 * equal to 500 ft AGL, the flight rules are IFR. If the lowest visibility is less than 1 SM or the
 * lowest ceiling is less than 500 ft AGL, the flight rules are LIFR.
 *
 * @author xmcnulty
 *
 * @see [Flight Rules](https://www.aviationweather.gov/help?page=plot#fltcat)
 *
 * @property description The description of the flight rules
 * @property code The code of the flight rules
 * @property color The color of the flight rules
 *  Green: VFR
 *  Blue: MVFR
 *  Red: IFR
 *  Magenta: LIFR
 */
enum class FlightRules(
    override val description: String,
    override val code: String,
    val color: Color
) : MetarBlock {
    VFR("Visual Flight Rules", "VFR", Color.Green),
    MVFR("Marginal Visual Flight Rules", "MVFR", Color.Blue),
    IFR("Instrument Flight Rules", "IFR", Color.Red),
    LIFR("Low Instrument Flight Rules", "LIFR", Color.Magenta);

    companion object {
        fun fromString(value: String): FlightRules {
            return when (value) {
                "VFR" -> VFR // green
                "MVFR" -> MVFR // blue
                "IFR" ->  IFR // red
                "LIFR" -> LIFR // magenta
                else -> throw IllegalArgumentException("Invalid FlightRules value: $value")
            }
        }
    }
}