package com.core.module

import android.content.Context
import com.core.domain.remote.GsonProvider
import com.core.utils.DeviceInfoUtilClass
import com.core.utils.PreferenceManager
import com.core.utils.ResourceHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object
AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setDateFormat(GsonProvider.ISO_8601_DATE_FORMAT).create()
    }

    @Provides
    @Singleton
    fun provideGsonProvider(): GsonProvider = GsonProvider()

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext appContext: Context) =
        PreferenceManager(appContext)


    @Provides
    @Singleton
    fun provideResourceHelper(@ApplicationContext appContext: Context) =
        ResourceHelper(appContext)

    @Provides
    @Singleton
    fun provideDeviceInfoUtil(@ApplicationContext appContext: Context) =
        DeviceInfoUtilClass(appContext)
}