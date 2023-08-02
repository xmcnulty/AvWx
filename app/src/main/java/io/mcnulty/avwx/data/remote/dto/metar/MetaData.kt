package io.mcnulty.avwx.data.remote.dto.metar

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("cache_timestamp")
    val cacheTimestamp: String,
    @SerializedName("stations_updated")
    val stationsUpdated: String,
    val timestamp: String
)
