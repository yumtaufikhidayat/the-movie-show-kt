package com.taufik.themovieshow.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

abstract class BaseApiResponse {
    fun <T : Any> safeApiCall(apiCall: suspend () -> Response<T>): Flow<NetworkResult<T>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    emit(NetworkResult.Success(body))
                } else {
                    emit(error("Response body is null"))
                }
            } else {
                emit(error("${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(error(e.message ?: e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    private fun <T> error(errorMessage: String): NetworkResult<T> = NetworkResult.Error(errorMessage)
}