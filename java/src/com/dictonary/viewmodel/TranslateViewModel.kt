package com.dictonary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.android.inputmethod.latin.R
import com.core.base.BaseViewModel
import com.core.data.mapper.translate.TranslateMapper
import com.core.data.model.translate.TranslateReq
import com.core.data.model.translate.TranslateResponse
import com.core.data.usecase.TranslateUseCase
import com.core.database.entity.HistoryEntity
import com.core.database.repo.dictionary.DictionaryRepo
import com.core.database.repo.history.HistoryRepo
import com.core.domain.callback.OptimizedCallbackWrapper
import com.core.domain.remote.ApiErrorMessages
import com.core.domain.remote.BaseError
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.PreferenceManager
import com.dictonary.navigator.TranslateNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val translateMapper: TranslateMapper,
    private val dictionaryRepo: DictionaryRepo,
    private val historyRepo: HistoryRepo,
    private val preferenceManager: PreferenceManager
) :
    BaseViewModel<TranslateNavigator>() {


    fun handleSpeechText(text: String) {
        getNavigator()?.onTextSpeechReceived(text)
    }

    fun transformLanguage() {
        val toCode = preferenceManager.getPrefToLangCode()
        val fromCode = preferenceManager.getPrefFromLangCode()
        AppLogger.e(TAG, "start -- ${fromCode} -> ${toCode}")

        preferenceManager.setPrefFromLangCode(toCode)
        preferenceManager.setPrefToLangCode(fromCode)

        AppLogger.e(
            TAG,
            "${preferenceManager.getPrefFromLangCode()} -> ${preferenceManager.getPrefToLangCode()}"
        )

        val toText = preferenceManager.getPrefToLangText()
        val fromText = preferenceManager.getPrefFromLangText()
        AppLogger.e(TAG, "start ${fromText} -> ${toText}")

        preferenceManager.setPrefFromLangText(toText)
        preferenceManager.setPrefToLangText(fromText)

        AppLogger.e(
            TAG,
            "${preferenceManager.getPrefFromLangText()} -> ${preferenceManager.getPrefToLangText()}"
        )
        getNavigator()?.onLanguageChanged(
            preferenceManager.getPrefFromLangText(),
            preferenceManager.getPrefToLangText()
        )
    }

    fun getData() {
        viewModelScope.launch(ioDispatcher) {
            val list = dictionaryRepo.getDictionaryDataList()
            AppLogger.e(TAG, "${list.toString()}")

        }
    }

    fun getTranslation(request: TranslateReq) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        translateUseCase.execute(
            TranslateSubscriber(request),
            TranslateUseCase.Params.create(request),
            coroutineScope = viewModelScope,
            dispatcher = ioDispatcher
        )
    }


    inner class TranslateSubscriber constructor(private val request: TranslateReq) :
        OptimizedCallbackWrapper<TranslateResponse>() {

        override fun onApiSuccess(response: TranslateResponse) {
            val translatedText = translateMapper.mapFrom(response)

            viewModelScope.launch {
                val historyEntity = HistoryEntity().apply {
                    val langCode = preferenceManager.getPrefToLangCode()
                    id = historyRepo.getMaxRowID()
                    this.translatedText = if (langCode == "ur") translatedText else request.text
                    textForTranslation = if (langCode == "en") translatedText else request.text
                    isUrdu = langCode != "en"
                }
                historyRepo.insertHistoryData(historyEntity)
            }

            getNavigator()?.onTranslatedTextReceived(translatedText)
            getNavigator()?.setProgressVisibility(View.GONE)

        }

        override fun onApiError(error: BaseError) {
            getNavigator()?.setProgressVisibility(View.GONE)
            getNavigator()?.prepareAlert(
                title = R.string.error,
                message = ApiErrorMessages.getErrorMessage(error)
            )
        }
    }

}