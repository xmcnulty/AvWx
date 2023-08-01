package io.mcnulty.avwx.domain.model.metar.measurement

enum class PressureUnits(
    override val description: String,
    override val abbreviation: String
) : MetarUnits {
    INCHES_OF_MERCURY("inches of mercury", "inHg"),
    HECTOPASCALS("hectopascals", "hPa");

    companion object {
        fun fromString(value: String): MetarUnits {

            // returns the associated value, or inches of mercury by default
            return values().firstOrNull { it.abbreviation == value } ?: INCHES_OF_MERCURY
        }
    }
}