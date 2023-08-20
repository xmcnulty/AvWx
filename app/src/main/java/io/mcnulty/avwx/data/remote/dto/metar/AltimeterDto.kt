package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto

/**
 * Data class representing the JSON object returned by the AVWX API.
 * Example: {
 *      "repr": "A2966",
 *      "value": 29.66,
 *      "spoken": "two nine point six six"
 *    }
 *
 *
 * @property repr The raw METAR string
 * @property value The altimeter value
 */
data class AltimeterDto(
    val repr: String,
    val value: Number
) : Dto {
    override fun description(): String {
        return "Altimeter - repr: $repr value - $value"
    }
}