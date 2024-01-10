package com.core.data.service

import com.core.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
    companion object{
        const val BASE_URL = "  https://translate.googleapis.com/"
    }

    @GET("3/discover/movie?api_key=c9856d0cb57c3f14bf75bdc6c063b8f3")
    suspend fun getMoviesList(): Response<MovieResponse>

    @GET("translate_a/single")
    suspend fun getTranslatedDate(
        @Query("client") client: String = "gtx",
        @Query("sl") sourceLanguage: String,
        @Query("tl") targetLanguage: String,
        @Query("dt") dataType: String = "t",
        @Query("q") textToTranslate: String
    ): Response<String>
}