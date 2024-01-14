package com.core.module

import android.content.Context
import com.core.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideDictionaryDAO(database: AppDatabase) = database.dictionaryDao()

    @Singleton
    @Provides
    fun provideHistoryDAO(database: AppDatabase) = database.historyDao()

    @Singleton
    @Provides
    fun provideConversationDAO(database: AppDatabase) = database.conversationDao()

}