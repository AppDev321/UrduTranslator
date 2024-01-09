package com.core.data.usecase

import com.core.data.model.MovieResponse
import com.core.data.repo.movie.MovieRepo
import com.core.domain.usecase.NetworkUseCaseHelper
import retrofit2.Response
import javax.inject.Inject

class MovieUseCase @Inject constructor(
    private val movieRepo: MovieRepo,
) : NetworkUseCaseHelper<MovieResponse, MovieUseCase.Params>() {

    override suspend fun buildUseCase(params: Params?): Response<MovieResponse> {
        return movieRepo.getMoviesList()
    }

    data class Params constructor(val params: String) {
        companion object {
            fun create(packId: String) = Params(packId)
        }
    }
}