package io.mcnulty.avwx.data.remote

data class NetworkResponse<T>(
    val body: T? = null,
    val errorMessage: String? = null,
)
