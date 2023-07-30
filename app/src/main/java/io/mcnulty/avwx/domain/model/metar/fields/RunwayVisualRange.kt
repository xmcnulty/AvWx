package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits

data class RunwayVisualRange(
    val runway: Runway,
    val maxVisibility: Int,
    val minVisibility: Int? = null,
    val marker: Marker? = null,
    override val units: VisibilityUnits = VisibilityUnits.FEET
) : MetarBlock, Measurable {

    init {
        minVisibility?.let {
            require(it < maxVisibility) { "Minimum visibility must be less than maximum visibility" }
        }
    }

    enum class Marker {
        LESS_THAN,
        GREATER_THAN
    }

    override val code: String
        get() {
            return when(minVisibility) {
                null -> {
                    val markerString = marker?.let {
                        when(it) {
                            Marker.LESS_THAN -> "M"
                            Marker.GREATER_THAN -> "P"
                        }
                    } ?: ""

                    "${runway.code}/$markerString$maxVisibility${units.abbreviation.uppercase()}"
                }

                else -> {
                    "${runway.code}/${minVisibility}V$maxVisibility${units.abbreviation.uppercase()}"
                }
            }
        }

    override val description: String
        get() {
            val prefix = "Runway ${runway.code} visual range: "

            return prefix + when(minVisibility) {
                null -> {
                    val markerString = marker?.let {
                        when(it) {
                            Marker.LESS_THAN -> "less than"
                            Marker.GREATER_THAN -> "greater than"
                        }
                    } ?: ""

                    "$markerString $maxVisibility ${units.description}"
                }

                else -> {
                    "between $minVisibility and $maxVisibility ${units.description}"
                }
            }
        }
}
