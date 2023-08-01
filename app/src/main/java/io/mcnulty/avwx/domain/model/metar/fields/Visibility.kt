package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits

class Visibility(
    override val code: String,
    val value: Double,
    override val units: VisibilityUnits
) : MetarBlock, Measurable {

    override val description: String
        get() {
            if (units == VisibilityUnits.METERS && value == 9999.0) {
                return "Greater than 10km"
            }

            if (units == VisibilityUnits.STATUTE_MILES && value == 10.0) {
                return "At least 10SM statute miles"
            }

            return "Visibility: ${value}${units.abbreviation}"
        }
}
