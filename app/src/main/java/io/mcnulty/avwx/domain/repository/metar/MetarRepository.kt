package io.mcnulty.avwx.domain.repository.metar

import io.mcnulty.avwx.data.remote.ApiResponse
import io.mcnulty.avwx.domain.model.metar.Metar

interface MetarRepository {
    suspend fun getMetar(fieldCode: String): ApiResponse<Metar>
}