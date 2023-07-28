package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.measurement.MetarUnits

interface Measurable {
    val units: MetarUnits
}