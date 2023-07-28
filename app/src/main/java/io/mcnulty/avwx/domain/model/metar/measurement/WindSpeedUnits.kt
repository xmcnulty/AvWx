package io.mcnulty.avwx.domain.model.metar.measurement

enum class WindSpeedUnits(
    override val abbreviation: String,
    override val description: String
) : MetarUnits {
    KNOTS("KT", "knots"),
    METERS_PER_SECOND("MPS", "meters per second"),
    KILOMETERS_PER_HOUR("KPH", "kilometers per hour"),
    MILES_PER_HOUR("MPH", "miles per hour");

    override fun fromString(value: String): MetarUnits {
        return values().first { it.abbreviation == value }
    }
}