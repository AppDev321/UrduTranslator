package com.core.module

import com.core.database.repo.conversation.ConversationRepo
import com.core.database.repo.conversation.ConversationRepoImpl
import com.core.database.repo.dictionary.DictionaryRepo
import com.core.database.repo.dictionary.DictionaryRepoImpl
import com.core.database.repo.history.HistoryRepo
import com.core.database.repo.history.HistoryRepoImpl
import com.core.database.repo.learn.LearnRepo
import com.core.database.repo.learn.LearnRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule {
    @Binds
    abstract fun bindDictionaryDbRepository(dictionaryRepoImpl: DictionaryRepoImpl): DictionaryRepo

    @Binds
    abstract fun bindHistoryDbRepository(historyRepoImpl: HistoryRepoImpl): HistoryRepo

    @Binds
    abstract fun bindConversationDbRepository(conversationRepoImpl: ConversationRepoImpl): ConversationRepo


    @Binds
    abstract fun bindLearnDbRepository(learnRepoImpl: LearnRepoImpl): LearnRepo
}
