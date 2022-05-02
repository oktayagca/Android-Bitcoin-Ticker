package com.example.bitcointicker.data.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun traceErrorException(throwable: Throwable?,code:String?): ApiError {
    if (throwable != null) {
        return when (throwable) {

            is HttpException -> {
                when (throwable.code()) {
                    400 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.BAD_REQUEST
                    )
                    401 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.UNAUTHORIZED
                    )
                    403 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.FORBIDDEN
                    )
                    404 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.NOT_FOUND
                    )
                    405 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.METHOD_NOT_ALLOWED
                    )
                    409 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.CONFLICT
                    )
                    500 -> ApiError(
                        throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.INTERNAL_SERVER_ERROR)
                    else -> ApiError(
                        UNKNOWN_ERROR_MESSAGE,
                        0,
                        ApiError.ErrorStatus.UNKNOWN_ERROR
                    )
                }
            }

            is SocketTimeoutException -> {
                ApiError(throwable.message,1001 ,ApiError.ErrorStatus.TIMEOUT)
            }

            is IOException -> {
                ApiError(throwable.message,1002, ApiError.ErrorStatus.NO_CONNECTION)
            }
            else -> ApiError(UNKNOWN_ERROR_MESSAGE, 0, ApiError.ErrorStatus.UNKNOWN_ERROR)
        }
    }

    return ApiError(UNKNOWN_ERROR_MESSAGE,0, ApiError.ErrorStatus.UNKNOWN_ERROR)
}