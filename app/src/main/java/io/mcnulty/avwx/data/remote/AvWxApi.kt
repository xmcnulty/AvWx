package io.mcnulty.avwx.data.remote

import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

val removeQuery = listOf(
        "spoken",
        "other",
        "sanitized",
        "remarks_info",
        "dt",
        "accumulation",
    ).joinToString("%2C")

interface AvWxApi {

    @Headers("Authorization: DUMMY_TOKEN")
    @GET("api/metar/{fieldCode}")
    suspend fun getMetar(
        @Path("fieldCode") fieldCode: String,
        @Query("remove") remove: String = removeQuery
    ): MetarDto
}