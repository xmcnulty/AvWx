package io.mcnulty.avwx.domain.model.metar.measurement

enum class TemperatureUnits(
    override val description: String,
    override val abbreviation: String
) : MetarUnits {
    CELSIUS("Celsius", "C"),
    FAHRENHEIT("Fahrenheit", "F");

    companion object {
        fun fromString(value: String): MetarUnits {
            return values().firstOrNull() { it.abbreviation == value } ?: CELSIUS
        }
    }
}