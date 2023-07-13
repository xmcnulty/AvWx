package io.mcnulty.avwx.domain.model

/**
 * Represents atmospheric pressure in a METAR (Meteorological Aerodrome Report) report.
 *
 * @property pressure The integer value of the atmospheric pressure.
 * @property prefix The prefix associated with the atmospheric pressure measurement type.
 */
sealed class AtmosphericPressure(val pressure: Int, private val prefix: String) {
    /**
     * Represents the QNH (Qualified National Height) atmospheric pressure measurement type.
     *
     * @param pressure The integer value of the QNH atmospheric pressure.
     */
    class QNH(pressure: Int) : AtmosphericPressure(pressure, "QNH")

    /**
     * Represents the Altimeter atmospheric pressure measurement type.
     *
     * @param pressure The integer value of the Altimeter atmospheric pressure. Ex. 29.92 is 2992
     */
    class Altimeter(pressure: Int) : AtmosphericPressure(pressure, "A") {

        /**
         * Returns the decimal value of inHg pressure. Example 2992 will return 29.92
         */
        fun pressureDecimal(): Double = pressure / 100.0
    }

    /**
     * Returns a string representation of the AtmosphericPressure object.
     *
     * The returned string consists of the atmospheric pressure prefix followed by the pressure value.
     *
     * @return A string representation of the AtmosphericPressure object.
     */
    override fun toString(): String {
        return "$prefix$pressure"
    }
}
