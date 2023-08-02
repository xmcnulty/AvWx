package io.mcnulty.avwx.data.remote

import io.mcnulty.avwx.data.remote.dto.metar.MetarDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface AvWxApi {
    @Headers("Authorization: ")
    @GET("api/metar/{fieldCode}")
    suspend fun getMetar(
        @Path("fieldCode") fieldCode: String,
        @Query("options") options: String = "info",
        @Query("onfail") onfail: String = "cache"
    ): MetarDto
}