package com.core.domain.executor

import com.android.inputmethod.latin.BuildConfig
import com.core.data.service.ApiServiceInterface.Companion.BASE_URL
import com.core.domain.NetworkConstants.CONNECT_TIME_OUT
import com.core.domain.NetworkConstants.HEADER_KEY_CONTENT_TYPE
import com.core.domain.NetworkConstants.HEADER_KEY_CONTENT_TYPE_VALUE
import com.core.domain.NetworkConstants.READ_TIME_OUT
import com.core.domain.remote.HTTPNotFoundException
import com.core.domain.remote.NetworkException
import com.core.domain.remote.ServerNotAvailableException
import com.core.domain.remote.SessionExpiredException
import com.core.utils.DeviceInfoUtilClass
import com.core.utils.PreferenceManager
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.EOFException
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.HttpURLConnection.HTTP_UNAVAILABLE
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkProvider @Inject constructor(gson: Gson) {
    private var retrofit: Retrofit

    @Inject
    lateinit var deviceInfoUtilClass: DeviceInfoUtilClass

    @Inject
    lateinit var preferenceManager: PreferenceManager


    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //endregion

    //region OK_HTTP
    private fun getOkHttpClient(): OkHttpClient {
        val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpBuilder.addNetworkInterceptor(networkInterceptor())
            .addInterceptor(responseHandlerInterceptor())
            .addInterceptor(headerInterceptor())
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            okHttpBuilder.eventListenerFactory(LoggingEventListener.Factory())
        }
        return okHttpBuilder.build()
    }

    private fun networkInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        return loggingInterceptor
    }


    private fun headerInterceptor(): Interceptor {
        return Interceptor {
            val requestBuilder = it.request()
                .newBuilder()
                .addHeader(HEADER_KEY_CONTENT_TYPE, HEADER_KEY_CONTENT_TYPE_VALUE)
            /*.addHeader(HEADER_KEY_DEVICE_ID, deviceInfoUtilClass.getAndroidId())
            .addHeader(HEADER_KEY_BRAND, deviceInfoUtilClass.brand)
            .addHeader(HEADER_KEY_MODEL, deviceInfoUtilClass.model)
            .addHeader(HEADER_KEY_OS, deviceInfoUtilClass.osVersion)
            .addHeader(HEADER_KEY_APP_VERSION, BuildConfig.APP_VERSION_NAME)
            preferenceManager.getToken().let { token ->
            requestBuilder.addHeader(HEADER_KEY_CONTENT_AUTHORIZATION, "Bearer $token")
        }*/

            it.proceed(requestBuilder.build())
        }
    }


    private fun responseHandlerInterceptor(): Interceptor {
        return Interceptor {
            var response: Response? = null
            try {
                response = it.proceed(it.request()) as Response
                when (response.code) {
                    HTTP_UNAVAILABLE -> {
                        throw ServerNotAvailableException("Server not available , please try again later")
                    }

                    HTTP_UNAUTHORIZED -> {
                        throw SessionExpiredException("Session is expired")
                    }

                    HTTP_NOT_FOUND -> {
                        throw HTTPNotFoundException("Http not found")
                    }
                }
                response
            } catch (e: Exception) {
                response?.let { responseEnclosed ->
                    try {
                        responseEnclosed.close()
                    } catch (e: Exception) {
                        // don't crash
                    }
                }
                e.printStackTrace()
                when (e) {
                    is UnknownHostException -> throw NetworkException(e)
                    is HttpException,
                    is EOFException,
                    is HTTPNotFoundException,
                    is ConnectException,
                    is IOException -> throw e

                    else -> throw NetworkException(e)
                }
            }
        }
    }
    //endregion
}