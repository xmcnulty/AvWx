package io.mcnulty.avwx.domain.model.metar.measurement

enum class AltitudeUnits(
    override val description: String,
    override val abbreviation: String
) : MetarUnits {
    FEET("feet", "ft"),
    METERS("meters", "m");


    companion object {
        fun fromString(value: String): MetarUnits {

            // returns the associated value, or feet by default
            return values().firstOrNull { it.abbreviation == value } ?: FEET
        }
    }
}