package com.dictionary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.android.inputmethod.latin.R
import com.core.base.BaseViewModel
import com.core.data.mapper.translate.TranslateMapper
import com.core.data.model.translate.TranslateReq
import com.core.data.model.translate.TranslateResponse
import com.core.data.usecase.TranslateUseCase
import com.core.database.entity.HistoryEntity
import com.core.database.repo.history.HistoryRepo
import com.core.domain.callback.OptimizedCallbackWrapper
import com.core.domain.remote.ApiErrorMessages
import com.core.domain.remote.BaseError
import com.core.extensions.TAG
import com.core.utils.AppLogger
import com.core.utils.PreferenceManager
import com.dictionary.navigator.TranslateNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val translateMapper: TranslateMapper,
    private val historyRepo: HistoryRepo,
    private val preferenceManager: PreferenceManager
) : BaseViewModel<TranslateNavigator>() {

     var isRightSideConversationMicOpen = false
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

    fun getLastRecord(callback: (HistoryEntity) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val data = async { historyRepo.getLastRecord() }.await()
            withContext(mainDispatcher) {
                callback.invoke(data)
            }
        }
    }

    fun getTranslation(request: TranslateReq, isConversationData: Boolean = false) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        translateUseCase.execute(
            TranslateSubscriber(request, isConversationData),
            TranslateUseCase.Params.create(request),
            coroutineScope = viewModelScope,
            dispatcher = ioDispatcher
        )
    }


    inner class TranslateSubscriber constructor(
        private val request: TranslateReq,
        private val isConversationData: Boolean = false
    ) :
        OptimizedCallbackWrapper<TranslateResponse>() {

        override fun onApiSuccess(response: TranslateResponse) {
            val translatedText = translateMapper.mapFrom(response)
            viewModelScope.launch {
                val historyEntity = HistoryEntity().apply {
                    val langCode = preferenceManager.getPrefToLangCode()
                    id = historyRepo.getMaxRowID()
                    this.translatedText = translatedText
                    textForTranslation = request.text
                    fromCode = preferenceManager.getPrefFromLangCode()
                    toCode = langCode
                    fromLang = preferenceManager.getPrefFromLangText()
                    toLang = preferenceManager.getPrefToLangText()
                }
                if (!isConversationData) {
                    historyRepo.insertHistoryData(historyEntity)
                }
                else
                {
                    if(isRightSideConversationMicOpen) {
                        historyEntity.toLang = preferenceManager.getPrefFromLangText()
                        historyEntity.toCode = preferenceManager.getPrefFromLangCode()
                    }
                    else
                    {
                        historyEntity.toLang = preferenceManager.getPrefToLangText()
                        historyEntity.toCode = preferenceManager.getPrefToLangCode()
                    }
                }
                withContext(mainDispatcher)
                {
                    getNavigator()?.onTranslatedTextReceived(historyEntity)
                    getNavigator()?.setProgressVisibility(View.GONE)
                }
            }
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