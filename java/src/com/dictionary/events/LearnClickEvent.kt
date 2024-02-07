package com.dictionary.events

import com.core.database.entity.LearnEntity

sealed class LearnClickEvent {
    data class ItemClick(var data: LearnEntity) : LearnClickEvent()
    data class CopyClick(var data: LearnEntity, var isEngClick: Boolean = true) : LearnClickEvent()
    data class SpeakerClick(var data: LearnEntity, var isEngClick: Boolean = true) :
        LearnClickEvent()
}