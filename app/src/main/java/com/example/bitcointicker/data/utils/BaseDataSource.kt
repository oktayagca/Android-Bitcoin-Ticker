package com.example.bitcointicker.data.utils

import com.example.bitcointicker.utils.Resource
import retrofit2.Response
import java.util.concurrent.CancellationException

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            val body = response.body()
            val error = traceErrorException(null, response.code().toString())
            return makeError(
                message = error.getErrorMessage(),
                data = body,
                code = error.code.toString()
            )
        } catch (err: Exception) {
            val error = traceErrorException(err, null)
            return makeError(
                message = error.getErrorMessage(),
                data = null,
                code = error.code.toString()
            )
        } catch (e: CancellationException) {
            val error = traceErrorException(e, null)
            return makeError(
                message = error.getErrorMessage(),
                data = null,
                code = error.code.toString()
            )
        }
    }

    private fun <T> makeError(message: String, data: T?, code: String?): Resource<T> {
        return Resource.error(message, data, code)
    }
}