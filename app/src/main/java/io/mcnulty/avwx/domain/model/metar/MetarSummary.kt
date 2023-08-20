package io.mcnulty.avwx.domain.model.metar

import io.mcnulty.avwx.domain.model.metar.fields.FlightRules
import io.mcnulty.avwx.domain.model.metar.fields.MetarTime
import io.mcnulty.avwx.domain.model.metar.fields.Temperature
import io.mcnulty.avwx.domain.model.metar.fields.Visibility
import io.mcnulty.avwx.domain.model.metar.fields.Winds

data class MetarSummary(
    val icao: String,
    val time: MetarTime,
    val wind: Winds,
    val temp: Temperature,
    val rules: FlightRules,
    val visbility: Visibility
)
