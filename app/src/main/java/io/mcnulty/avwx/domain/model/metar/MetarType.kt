package io.mcnulty.avwx.domain.model.metar

enum class MetarType(
    override val code: String,
    override val description: String
) : MetarBlock {
    METAR("METAR", "Routine report (scheduled hourly)"),
    SPECI("SPECI", "Special report (unscheduled)"),
    AUTO("AUTO", "Automatic report (unscheduled)");

    companion object {
        fun fromString(value: String): MetarType {
            return values().firstOrNull() { it.code == value } ?: METAR
        }
    }
}