package io.mcnulty.avwx.domain.model.airport.runway

enum class RunwayPosition(
    val code: String,
    val description: String
) {
    LEFT("L", "Left"),
    RIGHT("R", "Right"),
    CENTER("C", "Center")
}