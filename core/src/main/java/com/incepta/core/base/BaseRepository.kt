package com.incepta.core.base

import com.incepta.core.network.AppException
import com.incepta.core.network.exception.NetworkExceptionMapper
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Abdullah on 18/5/25.
 */

open class BaseRepository @Inject constructor() {

    protected suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T> ): Result<T> {
        return try {
            val response = apiCall()
            if (response.code() == 200 && response.body().apply {
                this != null
                } != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(Exception(response.message().takeIf { it.isNotEmpty() } ?: "Invalid response"))
            }
        } catch (e: Exception) {
            Result.Error(mapException(e))
        }
    }

    private fun mapException(e: Exception): Exception {
        return when (e) {
            is HttpException -> NetworkExceptionMapper.codeToException(e.code())
            is IOException -> IOException("No internet connection")
            else -> AppException("Unknown error: ${e.message}")
        }
    }
}

// Extension to map success data
inline fun <T, R> Result<T>.mapSuccess(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Error -> Result.Error(exception)
    }
}