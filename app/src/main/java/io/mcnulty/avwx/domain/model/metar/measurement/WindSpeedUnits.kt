package io.mcnulty.avwx.domain.model.metar.measurement

enum class WindSpeedUnits(
    override val abbreviation: String,
    override val description: String
) : MetarUnits {
    KNOTS("kt", "knots"),
    METERS_PER_SECOND("mps", "meters per second"),
    KILOMETERS_PER_HOUR("kph", "kilometers per hour"),
    MILES_PER_HOUR("mph", "miles per hour");

    companion object {
        fun fromString(value: String): MetarUnits {
            return values().first { it.abbreviation == value }
        }
    }
}