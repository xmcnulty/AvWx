package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock
import io.mcnulty.avwx.domain.model.metar.measurement.VisibilityUnits

sealed class Visibility(
    override val code: String,
    override val description: String,
    override val units: VisibilityUnits
) : MetarBlock, Measurable {

}
