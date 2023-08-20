package io.mcnulty.avwx.data.remote.dto.metar

import io.mcnulty.avwx.data.remote.dto.Dto
import io.mcnulty.avwx.domain.model.metar.fields.Clouds

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
) : Dto {
    fun toCloud(): Clouds {
        return Clouds(
            coverage = Clouds.Coverage.fromCode(type),
            altitude = altitude * 100,
            type = Clouds.Type.fromCode(modifier)
        )
    }

    override fun description(): String {
        return "Cloud - repr: $repr type: $type altitude: $altitude modifier: $modifier"
    }
}