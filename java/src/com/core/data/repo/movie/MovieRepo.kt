package com.core.data.repo.movie

import com.core.data.model.MovieResponse
import retrofit2.Response

interface MovieRepo {
    suspend fun getMoviesList(): Response<MovieResponse>
    //suspend fun getMovies(request:MovieRequest): Response<MovieResponse>
}