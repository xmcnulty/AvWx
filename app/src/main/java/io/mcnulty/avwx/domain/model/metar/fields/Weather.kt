package io.mcnulty.avwx.domain.model.metar.fields

import io.mcnulty.avwx.domain.model.metar.MetarBlock

data class Weather(
    override val code: String,
    override val description: String
) : MetarBlock