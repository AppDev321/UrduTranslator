package com.core.database.repo.conversation

import com.core.database.entity.ConversationEntity

interface ConversationRepo {
    suspend fun getConversationList(): List<ConversationEntity>
    suspend fun insertConversationData(entity: ConversationEntity)
    suspend fun getMaxRowID(): Long
    suspend fun getLastRecord(): ConversationEntity
}