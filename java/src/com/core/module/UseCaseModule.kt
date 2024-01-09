package com.core.module

import com.core.data.repo.movie.MovieRepo
import com.core.data.usecase.MovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideMovieUseCase(movieRepository: MovieRepo) = MovieUseCase(movieRepository)

}
