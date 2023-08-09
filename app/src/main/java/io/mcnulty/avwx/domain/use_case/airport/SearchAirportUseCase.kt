package io.mcnulty.avwx.domain.use_case.airport

import io.mcnulty.avwx.domain.model.airport.AirportSummary
import io.mcnulty.avwx.domain.repository.airport.AirportRepository
import io.mcnulty.avwx.shared.resource.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchAirportUseCase @Inject constructor(
    private val repository: AirportRepository
) {
    suspend operator fun invoke(
        query: String,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Flow<Resource<List<AirportSummary>>> = flow {
        emit(Resource.Loading())

        val response = repository.searchStation(query)

        response.body?.let { airports ->
            emit(Resource.Success(airports))
            return@flow
        } ?: run {
            emit(Resource.Error(response.errorMessage ?: "unknown error"))
            return@flow
        }
    }.flowOn(dispatcher)
}