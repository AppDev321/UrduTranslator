package com.core.domain.usecase


import com.core.domain.callback.OptimizedCallbackWrapper
import com.core.domain.remote.BaseError
import com.core.domain.remote.HTTPBadRequest
import com.core.domain.remote.HTTPNotFoundException
import com.core.domain.remote.NetworkException
import com.core.domain.remote.ResponseErrors
import com.core.domain.remote.ServerNotAvailableException
import com.core.extensions.TAG
import com.core.extensions.safeGet
import com.core.utils.AppLogger

import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_INTERNAL_ERROR
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLPeerUnverifiedException

abstract class NetworkUseCaseHelper<T, Params> {

    companion object {
        private const val SOMETHING_WENT_WRONG = "Something went wrong , please try again later"
    }


    private val useCaseListener: UseCaseListener? = null
    abstract suspend fun buildUseCase(params: Params?): Response<T>

    fun execute(
        callbackWrapper: OptimizedCallbackWrapper<T>?,
        params: Params? = null,
        coroutineScope: CoroutineScope = GlobalScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Job {
        useCaseListener?.onPreExecute()
        return coroutineScope.launch(dispatcher) {
            try {
                val response = buildUseCase(params)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        useCaseListener?.onPostExecute()
                        callbackWrapper?.onApiSuccess(it)
                    }
                } else {
                    handleNetworkRequestError(callbackWrapper, HttpException(response))
                }
            } catch (e: Exception) {
                handleNetworkRequestError(callbackWrapper, e)
            }
        }
    }

    fun executeForJava(
        callbackWrapper: OptimizedCallbackWrapper<T>?,
        params: Params? = null
    ): Job {
        useCaseListener?.onPreExecute()
        return GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = buildUseCase(params)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        useCaseListener?.onPostExecute()
                        callbackWrapper?.onApiSuccess(it)
                    }
                } else {
                    handleNetworkRequestError(callbackWrapper, HttpException(response))
                }
            } catch (e: Exception) {
                handleNetworkRequestError(callbackWrapper, e)
            }
        }
    }

    private fun handleNetworkRequestError(
        callbackWrapper: OptimizedCallbackWrapper<T>?,
        exception: Throwable,
    ) {
        useCaseListener?.onPostExecute()
        val baseError: BaseError
        when (exception) {
            is HttpException -> {
                exception.response()?.errorBody().run {
                    val error = this?.string().safeGet()
                    AppLogger.e(
                        TAG,
                        "Retrofit exception $error with error code ${exception.code()}"
                    )

                    when (exception.response()?.code()) {
                        429 -> handleResponseError(
                            error,
                            ResponseErrors.HTTP_TOO_MANY_REQUEST,
                            callbackWrapper
                        )

                        HTTP_INTERNAL_ERROR -> handleResponseError(
                            error,
                            ResponseErrors.INTERNAL_SERVER_ERROR,
                            callbackWrapper
                        )

                        HTTP_UNAUTHORIZED -> handleResponseError(
                            error,
                            ResponseErrors.HTTP_UNAUTHORIZED,
                            callbackWrapper
                        )

                        else -> handleResponseError(
                            error = error,
                            callbackWrapper = callbackWrapper
                        )
                    }
                }
            }

            is ServerNotAvailableException -> {
                baseError = BaseError(
                    errorMessage = "Server not available",
                    errorCode = ResponseErrors.HTTP_UNAVAILABLE
                )
                callbackWrapper?.onApiError(baseError)
            }

            is HTTPNotFoundException -> {
                baseError = BaseError(
                    errorMessage = "Server not available",
                    errorCode = ResponseErrors.HTTP_NOT_FOUND
                )
                callbackWrapper?.onApiError(baseError)
            }

            is UnknownHostException,
            is NetworkException,
            is ConnectException,
            -> {
                callbackWrapper?.onApiError(
                    BaseError(
                        errorMessage = "Internet not available",
                        errorCode = ResponseErrors.CONNECTIVITY_EXCEPTION
                    )
                )
            }

            is SSLPeerUnverifiedException -> {
                callbackWrapper?.onApiError(
                    BaseError(
                        errorMessage = SOMETHING_WENT_WRONG,
                        errorCode = ResponseErrors.UNKNOWN_EXCEPTION
                    )
                )
            }

            is IOException,
            is TimeoutException,
            -> {
                baseError = if (exception.localizedMessage != null) {
                    BaseError(
                        errorMessage = exception.localizedMessage!!,
                        errorCode = ResponseErrors.UNKNOWN_EXCEPTION
                    )
                } else {
                    BaseError(
                        errorMessage = SOMETHING_WENT_WRONG,
                        errorCode = ResponseErrors.UNKNOWN_EXCEPTION
                    )
                }
                callbackWrapper?.onApiError(baseError)
            }

            is HTTPBadRequest -> {
                callbackWrapper?.onApiError(
                    BaseError(
                        errorMessage = SOMETHING_WENT_WRONG,
                        errorCode = ResponseErrors.HTTP_BAD_REQUEST
                    )
                )
            }
        }
    }

    private fun getErrorMessageFromResponse(error: String): String {
        var errorMessage: String
        try {
            val baseError = Gson().fromJson(
                error,
                BaseError::class.java
            )
            if (baseError != null) {
                baseError.apply {
                    errorMessage = this.message.ifBlank {
                        this.errorMessage
                    }
                }
            } else {
                errorMessage = SOMETHING_WENT_WRONG
            }

        } catch (e: Exception) {
            errorMessage = SOMETHING_WENT_WRONG
        }
        return errorMessage
    }

    private fun handleResponseError(
        error: String,
        errorCode: ResponseErrors = ResponseErrors.RESPONSE_ERROR,
        callbackWrapper: OptimizedCallbackWrapper<T>?,
    ) {

        callbackWrapper?.onApiError(
            error = BaseError(
                errorBody = error,
                errorMessage = getErrorMessageFromResponse(error),
                errorCode = errorCode
            )
        )
    }
}