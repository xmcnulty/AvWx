package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.WindSpeedUnits

data class Winds(
    val direction: Int?,
    val speed: Int,
    val gust: Int? = null,
    val variation: Variation? = null,
    override val units: WindSpeedUnits
) : MetarBlock, Measurable {

    override val code: String
        get() {
            val speedString = speed.toString().padStart(2, '0')
            val gustCode = if (gust != null) "G${gust.toString().padStart(2, '0')}" else ""
            val variationString = when(variation) {
                null -> ""
                else -> " ${variation.code}"
            }

            return when(direction) {
                null -> "VRB$speedString$gustCode${units.abbreviation}"
                else -> "${direction.toString().padStart(3, '0')}$speedString$gustCode${units.abbreviation}$variationString"
            }
        }

    override val description: String
        get() {
            if(direction == 0 && speed == 0)
                return "Winds calm."

            val speedString = when(gust) {
                null -> "at $speed ${units.description}."
                else -> "at $speed ${units.description} gusting to $gust ${units.description}."
            }

            val directionString = when(variation) {
                null -> when(direction) {
                    null -> "Winds variable,"
                    else -> "Winds at $direction degrees,"
                }
                else -> return "${variation.description}, $speedString"
            }

            return "$directionString $speedString"
        }

    data class Variation(
        val from: Int,
        val to: Int
    ) : MetarBlock {
        override val code: String
            get() = "${from.toString().padStart(3, '0')}V${to.toString().padStart(3, '0')}"

        override val description: String = "Winds varying between $from and $to degrees"
    }
}
