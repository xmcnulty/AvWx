package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.metar.CloudDto
import io.mcnulty.avwx.domain.model.metar.fields.Clouds

/**
 * Converts cloud from a [CloudDto] to a [Clouds] domain object.
 */
internal object CloudParser {
    /**
     * Parses cloud from a [CloudDto] to a [Clouds] domain object.
     *
     * @param cloudDtos The [CloudDto] to parse
     * @return The parsed [Clouds]
     */
    fun parse(cloudDtos: List<CloudDto>): List<Clouds> {
        return cloudDtos.map { cloudDto ->
            Clouds(
                coverage = Clouds.Coverage.fromCode(cloudDto.type),
                altitude = cloudDto.altitude * 100,
                type = Clouds.Type.fromCode(cloudDto.modifier)
            )
        }
    }
}