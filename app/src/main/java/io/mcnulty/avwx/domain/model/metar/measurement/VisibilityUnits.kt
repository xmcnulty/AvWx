package io.mcnulty.avwx.domain.model.metar.measurement

enum class VisibilityUnits(
    override val description: String,
    override val abbreviation: String
) : MetarUnits {
    METERS("meters", "m"),
    KILOMETERS("kilometers", "km"),
    STATUTE_MILES("statute miles", "sm"),
    FEET("feet", "ft");

    companion object {
        fun fromString(value: String): MetarUnits {
            return values().firstOrNull() { it.abbreviation == value } ?: STATUTE_MILES
        }
    }
}