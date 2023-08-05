package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits
import java.text.NumberFormat

/**
 * Data class for cloud layers contained in a METAR report.
 *
 * @property altitude The altitude of the cloud layer in hundreds of feet.
 * @property coverage The type of cloud layer.
 */
data class Clouds(
    val altitude: Int,
    val coverage: Coverage,
    val type: Type? = null,
    override val units: AltitudeUnits = AltitudeUnits.FEET
) : MetarBlock, Measurable {

    init {
        require(altitude >= 100) { "Altitude must be at least" }

        require(altitude < 100000) { "Altitude must be less than 100,000" }

        if (coverage == Coverage.VERTICAL_VISIBILITY) {
            require(type == null) { "Cloud type must be null for vertical visibility coverage" }
        }
    }

    override val code: String
        get() {
            val altString = (altitude / 100).toString().padStart(3, '0')
            return "${coverage.code}$altString${type?.code ?: ""}"
        }

    override val description: String
        get() {
            if (coverage == Coverage.VERTICAL_VISIBILITY) {
                return "Vertical visibility ${
                    NumberFormat.getIntegerInstance().format(altitude)
                }${units.abbreviation}"
            }

            val cloudType = type?.let {
                " ${it.description}"
            } ?: ""

           return "${coverage.description}$cloudType at ${
                NumberFormat.getIntegerInstance().format(altitude)
            }${units.abbreviation}"
        }

    /**
     * Cloud types.
     * Valid values are FEW, SCT, BKN, OVC, and UNKN.
     *
     * @property code The code used in METAR reports.
     * @property description The description of the cloud type.
     */
    enum class Coverage(
        val code: String,
        val description: String
    ) {
        FEW("FEW", "Few"),
        SCATTERED("SCT", "Scattered"),
        BROKEN("BKN", "Broken"),
        OVERCAST("OVC", "Overcast"),
        VERTICAL_VISIBILITY("VV", "Vertical Visibility"),
        UNKNOWN("UNKN", "Unknown");

        companion object {
            fun fromCode(code: String): Coverage {
                return values().firstOrNull { it.code == code } ?: UNKNOWN
            }
        }
    }

    /**
     * Represents significant convective clouds at this cloud layer, if any.
     * Possible values are CB and TCU.
     */
    enum class Type(
        val code: String,
        val description: String
    ) {
        CUMULONIMBUS("CB", "Cumulonimbus"),
        TOWERING_CUMULUS("TCU", "Towering Cumulus");

        companion object {
            fun fromCode(code: String?): Type? = code?.let {
                values().firstOrNull { it.code == code }
            }
    }
    }
}
