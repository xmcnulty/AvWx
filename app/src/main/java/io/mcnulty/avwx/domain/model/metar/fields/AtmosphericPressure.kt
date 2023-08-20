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
        override val code: String = "Q$value"

        override val description: String = "$value ${units.abbreviation}"
    }

    override fun equals(other: Any?): Boolean {
        if (other !is AtmosphericPressure) return false

        return code == other.code
    }

    companion object {
        fun build(literal: String): AtmosphericPressure {
            val value = when {
                literal.startsWith("A") -> {
                    val inches = literal.substring(1).toDouble()
                    Altimeter(inches / 100)
                }
                literal.startsWith("Q") -> {
                    val hectopascals = literal.substring(1).toInt()
                    Qnh(hectopascals)
                }
                else -> throw IllegalArgumentException("Invalid atmospheric pressure literal: $literal")
            }

            return value
        }
    }
}
