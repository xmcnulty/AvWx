package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import io.mcnulty.avwx.data.remote.dto.metar.RunwayVisibilityDto
import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.fields.RunwayVisualRange
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits

/**
 * Converts runway visual range from a [MetarDto] to a [RunwayVisualRange] domain object.
 */
internal object RVRParser {

    /**
     * Parses runway visual range from a [MetarDto] to a [RunwayVisualRange] domain object.
     *
     * @param rvrDto The [RunwayVisibilityDto] to parse
     * @param units The [VisibilityUnits] to use
     * @return The parsed [RunwayVisualRange]
     */
    fun parse(
        rvrDto: List<RunwayVisibilityDto>,
        units: AltitudeUnits
    ): List<RunwayVisualRange> = rvrDto.map { dto ->
        val runway = Runway.build(dto.runway)

        if(dto.variableVisibility.isNotEmpty()) {
            val visibilities = dto.variableVisibility.map { visRange ->
                RunwayVisualRange.RvRVisibility.build(visRange.repr)
            }.sortedBy { vis -> vis.value }

            RunwayVisualRange(
                runway,
                minVisibility = visibilities[0],
                maxVisibility = visibilities[1],
                units = units
            )
        } else {
            RunwayVisualRange(
                runway,
                RunwayVisualRange.RvRVisibility.build(dto.visibility!!.repr),
                units = units
            )
        }
    }
}