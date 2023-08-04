package io.mcnulty.avwx.data.remote.dto.metar

/**
 * Data class representing cloud JSON object returned from AVWX API.
 *
 * Sample JSON:
 *     {
 *       "repr": "SCT022CB",
 *       "type": "SCT",
 *       "altitude": 22,
 *       "modifier": "CB"
 *     }
 *
 * @property altitude The cloud base altitude in hundreds of feet
 * @property modifier The cloud modifier. Typically cloud type.
 * @property repr The raw cloud string
 * @property type The cloud type
 */
data class CloudDto(
    val altitude: Int,
    val modifier: String,
    val repr: String,
    val type: String
)