package com.dictionary.navigator

import com.core.database.entity.HistoryEntity
import com.core.interfaces.BaseNavigator

interface TranslateNavigator : BaseNavigator {
    fun onTextSpeechReceived(text: String)
    fun onLanguageChanged(from: String, to: String)
    fun onTranslatedTextReceived(data: HistoryEntity)
}