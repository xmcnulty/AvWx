package io.mcnulty.avwx.domain.model.airport.runway

import io.mcnulty.avwx.utils.addSpacePrefix

data class Runway(
    val number: Int,
    val position: RunwayPosition? = null
) {
    init {
        require(number in 1..36) { "Runway number must be between 1 and 36" }
    }

    val code: String
        get() {
            val positionString = position?.code ?: ""
            return "${number.toString().padStart(2, '0')}$positionString"
        }

    val description: String
        get() {
            val positionString = position?.description?.addSpacePrefix() ?: ""
            return "Runway $number$positionString"
        }

    companion object {
        fun build(code: String): Runway {
            val number = code.substring(0, 2).toInt()
            val position = when(code.length) {
                3 -> RunwayPosition.build(code[2].toString())
                else -> null
            }

            return Runway(number, position)
        }
    }
}
