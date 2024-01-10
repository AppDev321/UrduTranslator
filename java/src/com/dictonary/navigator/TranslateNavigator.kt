package com.dictonary.navigator

import com.core.interfaces.BaseNavigator

interface TranslateNavigator : BaseNavigator {
    fun onTextSpeechReceived(text: String)
    fun onLanguageChanged(from: String, to: String)
    fun onTranslatedTextReceived(text: String)
}