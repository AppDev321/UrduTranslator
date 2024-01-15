package com.dictionary.module

import android.content.Context
import com.dictionary.model.SettingsDataFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UICommonModule {

    @Provides
    @Singleton
    fun provideFragmentDTODataFactory(
        @ApplicationContext appContext: Context,
    ) = SettingsDataFactory(appContext)

}