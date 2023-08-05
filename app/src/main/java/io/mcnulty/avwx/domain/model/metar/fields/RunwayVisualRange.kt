package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits

data class RunwayVisualRange(
    val runway: Runway,
    val maxVisibility: RvRVisibility,
    val minVisibility: RvRVisibility? = null,
    override val units: AltitudeUnits = AltitudeUnits.FEET
) : MetarBlock, Measurable {

    data class RvRVisibility internal constructor(
        val value: Int,
        val marker: Marker? = null
    ) {
        enum class Marker {
            LESS_THAN,
            GREATER_THAN
        }

        override fun toString(): String = when(marker) {
            Marker.LESS_THAN -> "less than $value"
            Marker.GREATER_THAN -> "greater than $value"
            else -> value.toString()
        }

        val code: String
            get() {
                return when(marker) {
                    Marker.LESS_THAN -> "M$value"
                    Marker.GREATER_THAN -> "P$value"
                    else -> value.toString()
                }
            }

        companion object {
            fun build(value: String): RvRVisibility {
                val visibility: Int
                val marker = when(value[0]) {
                    'M' -> {
                        visibility = value.substring(1).toInt()
                        Marker.LESS_THAN
                    }
                    'P' -> {
                        visibility = value.substring(1).toInt()
                        Marker.GREATER_THAN
                    }
                    else -> {
                        visibility = value.toInt()
                        null
                    }
                }

                return RvRVisibility(visibility, marker)
            }
        }
    }

    override val code: String
        get() {
            return when(minVisibility) {
                null -> "R${runway.code}/${maxVisibility.code}${units.abbreviation}"
                else -> "R${runway.code}/${minVisibility.code}V${maxVisibility.code}${units.abbreviation}"
            }
        }

    override val description: String
        get() {
            val prefix = "Runway ${runway.code} visual range: "

            return prefix + when(minVisibility) {
                null -> "$maxVisibility${units.abbreviation}"
                else -> {
                    "varying between $minVisibility and $maxVisibility${units.abbreviation}"
                }
            }
        }
}
