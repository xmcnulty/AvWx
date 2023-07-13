package io.mcnulty.avwx.domain.model

/**
 * Represents wind data from a METAR (Meteorological Aerodrome Report) report.
 *
 * @property variable A boolean value indicating whether the wind direction is variable.
 *                    By default, it is set to `false`.
 * @property direction An integer representing the wind direction in degrees.
 * @property speed An integer representing the wind speed in knots.
 * @property gusts An optional integer representing the wind gusts in knots.
 *                 It can be nullable (`Int?`).
 */
data class Wind(
    val variable: Boolean = false,
    val direction: Int,
    val speed: Int,
    val gusts: Int?
) {
    /**
     * Returns a string representation of the Wind object in METAR format.
     *
     * The returned string consists of the wind direction, speed, and gusts (if present),
     * formatted according to the METAR specification.
     *
     * @return A string representation of the Wind object in METAR format.
     */
    override fun toString(): String {
        val directionString = if (variable) "VRB" else direction.toString().padStart(3, '0')
        val gustString = gusts?.let { "G$it" } ?: ""
        val speedString = "${speed}KT"

        return "$directionString$speedString$gustString"
    }
}