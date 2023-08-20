package io.mcnulty.avwx.common

import io.mcnulty.avwx.domain.model.metar.fields.FlightRules

fun String.toFlightRules(): FlightRules {
    return when (this) {
        "VFR" -> FlightRules.VFR
        "MVFR" -> FlightRules.MVFR
        "IFR" -> FlightRules.IFR
        "LIFR" -> FlightRules.LIFR
        else -> throw IllegalArgumentException("Invalid flight rules: $this")
    }
}