package com.core.module

import com.core.data.repo.movie.MovieRepo
import com.core.data.repo.movie.MovieRepoImpl
import com.core.data.repo.translation.TranslateRepo
import com.core.data.repo.translation.TranslateRepoImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideMovieRepo(contactMng: MovieRepoImpl): MovieRepo

    @Binds
    abstract fun provideTranslateRepo(repo: TranslateRepoImp): TranslateRepo
}
