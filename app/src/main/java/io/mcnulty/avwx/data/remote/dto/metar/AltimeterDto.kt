package io.mcnulty.avwx.data.remote.dto.metar

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
)