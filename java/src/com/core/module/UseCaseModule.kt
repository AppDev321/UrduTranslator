package com.core.module

import com.core.data.repo.movie.MovieRepo
import com.core.data.repo.translation.TranslateRepo
import com.core.data.usecase.MovieUseCase
import com.core.data.usecase.TranslateUseCase
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

    @Provides
    @Singleton
    fun provideTranslateUseCase(translateRepo: TranslateRepo) = TranslateUseCase(translateRepo)

}
