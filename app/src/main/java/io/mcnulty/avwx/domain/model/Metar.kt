package io.mcnulty.avwx.domain.model

data class Metar(
    val station: String,
    val timeZulu: Int,
    val dayZulu: Int,
    val monthZulu: Int,
    val wind: Wind,
    val temperature: Int,
    val dewPoint: Int,
    val atmosphericPressure: AtmosphericPressure,
    val skyConditions: List<SkyCondition>,
    val visibility: Int? // null if greater than 10nm
)
