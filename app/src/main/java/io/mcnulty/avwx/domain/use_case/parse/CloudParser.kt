package io.mcnulty.avwx.domain.use_case.parse

import io.mcnulty.avwx.data.remote.dto.MetarDto
import io.mcnulty.avwx.domain.model.metar.fields.Clouds

internal object CloudParser {
    fun parse(cloudDtos: List<MetarDto.CloudDto>): List<Clouds> {
        return cloudDtos.map { cloudDto ->
            Clouds(
                coverage = Clouds.Coverage.fromCode(cloudDto.type),
                altitude = cloudDto.altitude * 100,
                type = Clouds.Type.fromCode(cloudDto.modifier)
            )
        }
    }
}