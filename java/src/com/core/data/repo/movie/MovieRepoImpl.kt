package com.core.data.repo.movie

import com.core.data.model.MovieResponse
import com.core.data.service.ApiServiceInterface
import retrofit2.Response
import javax.inject.Inject

class MovieRepoImpl @Inject constructor(private var apiServiceInterface: ApiServiceInterface) :
    MovieRepo {
    override suspend fun getMoviesList(): Response<MovieResponse> {
        return  apiServiceInterface.getMoviesList()
    }
}