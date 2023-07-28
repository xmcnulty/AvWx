package io.mcnulty.avwx.domain.model.metar.measurement

interface MetarUnits {
    val description: String
    val abbreviation: String

    fun fromString(value: String): MetarUnits
}