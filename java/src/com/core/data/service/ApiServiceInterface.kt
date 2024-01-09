package com.core.data.service

import com.core.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiServiceInterface {
    companion object{
        const val BASE_URL = "code"
    }

    @GET("3/discover/movie?api_key=c9856d0cb57c3f14bf75bdc6c063b8f3")
    suspend fun getMoviesList(): Response<MovieResponse>
}