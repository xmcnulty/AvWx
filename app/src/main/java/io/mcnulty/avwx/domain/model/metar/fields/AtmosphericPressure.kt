package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.MetarUnits
import io.mcnulty.avwx.domain.model.metar.measurement.PressureUnits

sealed class AtmosphericPressure(
    val value: Number,
    override val units: MetarUnits
) : MetarBlock, Measurable {
    class Altimeter(value: Double) : AtmosphericPressure(value, PressureUnits.INCHES_OF_MERCURY) {
        override val code: String = "A${(value * 100).toInt()}"

        override val description: String = "${String.format("%.2f", value)} ${units.abbreviation}"
    }

    class Qnh(value: Int) : AtmosphericPressure(value, PressureUnits.HECTOPASCALS) {
        override val code: String = "QNH$value"

        override val description: String = "$value ${units.abbreviation}"
    }
}
