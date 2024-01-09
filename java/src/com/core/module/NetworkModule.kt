package com.core.module

import com.core.data.service.ApiServiceInterface
import com.core.domain.executor.NetworkProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Because i use single ApiServiceInterface for all thats i use this if you have any interffaces then use same like below
    @Provides
    fun provideApiService(networkProvider: NetworkProvider) =
        networkProvider.create(ApiServiceInterface::class.java)

}
