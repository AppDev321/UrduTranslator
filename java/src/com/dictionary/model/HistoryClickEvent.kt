package com.dictionary.model

import com.core.database.entity.HistoryEntity

sealed class HistoryClickEvent {

    data class ItemClick (var data: HistoryEntity) : HistoryClickEvent()
    data class FavClick (var data: HistoryEntity) : HistoryClickEvent()
    data class CopyClick (var data: HistoryEntity) : HistoryClickEvent()

    data class SpeakerClick (var data: HistoryEntity) : HistoryClickEvent()

    data class ZoomClick (var data: HistoryEntity) : HistoryClickEvent()

}