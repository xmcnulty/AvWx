package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName
import io.mcnulty.avwx.data.remote.dto.Dto
import io.mcnulty.avwx.domain.model.airport.runway.Runway
import io.mcnulty.avwx.domain.model.metar.fields.RunwayVisualRange
import io.mcnulty.avwx.domain.model.metar.measurement.AltitudeUnits

data class RunwayVisibilityDto(
    val repr: String,
    val runway: String,
    val visibility: RunwayVisibility?,
    @SerializedName("variable_visibility")
    val variableVisibility: List<RunwayVisibility>,
    val trend: Any?
) : Dto {
    data class RunwayVisibility(
        val repr: String,
        val value: Any? // API returns an Int or empty json object
    )

    override fun description(): String {
        return "RVR - repr: $repr runway: $runway visibility: $visibility variableVisibility: $variableVisibility"
    }

    fun toRunwayVisualRange(
        units: AltitudeUnits = AltitudeUnits.FEET
    ): RunwayVisualRange {
        val visibilities = variableVisibility.map {  visDto ->
            RunwayVisualRange.RvRVisibility.build(visDto.repr)
        }.sortedBy { vis -> vis.value }

        return if(visibilities.size == 2) {
            RunwayVisualRange(
                runway = Runway.build(runway),
                minVisibility = visibilities[0],
                maxVisibility = visibilities[1],
                units = units
            )
        } else {
            RunwayVisualRange(
                runway = Runway.build(runway),
                RunwayVisualRange.RvRVisibility.build(visibility!!.repr),
                units = units
            )
        }
    }
}
