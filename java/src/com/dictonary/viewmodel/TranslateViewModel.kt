package com.dictonary.viewmodel

import com.core.base.BaseViewModel
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.PreferenceManager
import com.dictonary.navigator.TranslateNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TranslateViewModel @Inject constructor() :
    BaseViewModel<TranslateNavigator>() {


    fun handleSpeechText(text: String) {
        getNavigator()?.onTextSpeechReceived(text)
    }

    fun transformLanguage(preferenceManager: PreferenceManager) {
        val toCode = preferenceManager.getPrefToLangCode()
        val fromCode = preferenceManager.getPrefFromLangCode()
        AppLogger.e(TAG,"start -- ${fromCode} -> ${toCode}")

        preferenceManager.setPrefFromLangCode(toCode)
        preferenceManager.setPrefToLangCode(fromCode)

        AppLogger.e(TAG,"${preferenceManager.getPrefFromLangCode()} -> ${preferenceManager.getPrefToLangCode()}")

        val toText = preferenceManager.getPrefToLangText()
        val fromText = preferenceManager.getPrefFromLangText()
        AppLogger.e(TAG,"start ${fromText} -> ${toText}")

        preferenceManager.setPrefFromLangText(toText)
        preferenceManager.setPrefToLangText(fromText)

        AppLogger.e(TAG,"${preferenceManager.getPrefFromLangText()} -> ${preferenceManager.getPrefToLangText()}")
        getNavigator()?.onLanguageChanged(
            preferenceManager.getPrefFromLangText(),
            preferenceManager.getPrefToLangText()
        )
    }

}