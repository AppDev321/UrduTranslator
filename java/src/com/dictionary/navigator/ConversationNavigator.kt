package com.dictionary.navigator

import com.core.database.entity.ConversationEntity
import com.core.interfaces.BaseNavigator

interface ConversationNavigator : BaseNavigator {

    fun displayConversationList(item: List<ConversationEntity>)
    fun addConversation(item: ConversationEntity)
}