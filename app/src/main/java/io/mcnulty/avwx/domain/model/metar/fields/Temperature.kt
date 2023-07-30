package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.TemperatureUnits

data class Temperature(
    val temp: Double,
    override val units: TemperatureUnits = TemperatureUnits.CELSIUS
) : MetarBlock, Measurable {
    override val code: String
        get() {
            val unitString = when(units) {
                TemperatureUnits.CELSIUS -> ""
                TemperatureUnits.FAHRENHEIT -> "F"
            }

            // return temp rounded to 1 decimal place
            return "${String.format("%.1f", temp)}$unitString"
        }

    override val description: String = "Temperature $temp degrees ${units.description}"
}
