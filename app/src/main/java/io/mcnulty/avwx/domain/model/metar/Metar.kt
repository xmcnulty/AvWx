package io.mcnulty.avwx.domain.model.metar

import io.mcnulty.avwx.domain.model.metar.fields.Clouds

data class Metar(
    val clouds: List<Clouds>
)
