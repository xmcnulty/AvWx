package io.mcnulty.avwx.domain.use_case.metar

import io.mcnulty.avwx.domain.model.metar.Metar
import io.mcnulty.avwx.domain.repository.metar.MetarRepository
import io.mcnulty.avwx.shared.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDetailedMetarUseCase @Inject constructor(
    private val repository: MetarRepository
) {
    operator fun invoke(
        code: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Resource<Metar>> = flow {
        emit(Resource.Loading())

        val response = repository.getMetar(code)

        response.body?.let { metar ->
            emit(Resource.Success(metar))
            return@flow
        } ?: run {
            emit(Resource.Error(response.errorMessage ?: "unknown error"))
            return@flow
        }
    }.flowOn(dispatcher)
}