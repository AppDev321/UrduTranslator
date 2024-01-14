package com.core.database.repo.conversation

import com.core.database.dao.ConversationDao
import com.core.database.entity.ConversationEntity
import javax.inject.Inject

class ConversationRepoImpl @Inject constructor(
    private val conversationDao: ConversationDao
) : ConversationRepo {
    override suspend fun getConversationList(): List<ConversationEntity> {
        return conversationDao.getConversationList()
    }

    override suspend fun insertConversationData(entity: ConversationEntity) {
        conversationDao.insertConversation(entity)
    }

    override suspend fun getMaxRowID(): Long {
        val id = conversationDao.getMaxRowId() ?: 0
        return id + 1
    }

    override suspend fun getLastRecord(): ConversationEntity {
        return conversationDao.getLatestRecord()
    }


}