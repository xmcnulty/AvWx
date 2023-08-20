package io.mcnulty.avwx.domain.exceptions

import io.mcnulty.avwx.data.remote.dto.Dto

class MetarParsingException(
    val dto: Dto
) : Exception() {
    override val message: String
        get() = "Unable to parse ${dto.description()}"
}