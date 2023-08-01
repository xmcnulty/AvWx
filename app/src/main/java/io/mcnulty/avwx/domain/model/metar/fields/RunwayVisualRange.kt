package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits

data class RunwayVisualRange(
    val runway: Runway,
    val maxVisibility: RvRVisibility,
    val minVisibility: RvRVisibility? = null,
    override val units: VisibilityUnits = VisibilityUnits.FEET
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
            Marker.LESS_THAN -> "M$value"
            Marker.GREATER_THAN -> "P$value"
            else -> value.toString()
        }

        companion object {
            fun build(value: String): RvRVisibility {
                val marker = when(value[0]) {
                    'M' -> Marker.LESS_THAN
                    'P' -> Marker.GREATER_THAN
                    else -> null
                }

                val visibility = value.substring(1).toInt()

                return RvRVisibility(visibility, marker)
            }
        }
    }

    override val code: String
        get() {
            return when(minVisibility) {
                null -> "${runway.code}/$maxVisibility${units.abbreviation.uppercase()}"
                else -> "${runway.code}/${minVisibility}V$maxVisibility${units.abbreviation}"
            }
        }

    override val description: String
        get() {
            val prefix = "Runway ${runway.code} visual range: "

            return prefix + when(minVisibility) {
                null -> "$maxVisibility ${units.description}"
                else -> {
                    "varying between $minVisibility and $maxVisibility ${units.description}"
                }
            }
        }
}
