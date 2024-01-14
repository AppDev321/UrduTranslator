package com.dictionary.events

import com.core.database.entity.ConversationEntity

sealed class ConversationClickEvent {

    data class ItemClick (var data: ConversationEntity) : ConversationClickEvent()
    data class CopyClick (var data: ConversationEntity) : ConversationClickEvent()
    data class SpeakerClick (var data: ConversationEntity) : ConversationClickEvent()
    data class ShareClick (var data: ConversationEntity) : ConversationClickEvent()
    data class DeleteClick (var data: ConversationEntity) : ConversationClickEvent()

}