package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName

data class RunwayVisibilityDto(
    val repr: String,
    val runway: String,
    val visibility: RunwayVisibility?,
    @SerializedName("variable_visibility")
    val variableVisibility: List<RunwayVisibility>,
    val trend: Any
) {
    data class RunwayVisibility(
        val repr: String,
        val spoken: String,
        val value: Int
    )
}
