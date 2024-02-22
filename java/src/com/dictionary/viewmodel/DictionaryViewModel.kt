package com.dictionary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.database.repo.dictionary.DictionaryRepo
import com.core.extensions.safeGet
import com.core.utils.PreferenceManager
import com.dictionary.model.QuizOfTheDay
import com.dictionary.model.WordOfTheDay
import com.dictionary.navigator.DictionaryNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val dictionaryRepo: DictionaryRepo,
    private val preferenceManager: PreferenceManager
) : BaseViewModel<DictionaryNavigator>() {

    @Synchronized
    fun getDictionaryData( isEnglish:Boolean = true) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val list = async {
                dictionaryRepo.getDictionaryDataList()
            }.await()
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
                getNavigator()?.displayDictionaryDataList(list,isEnglish)
            }
        }

        if (preferenceManager.checkIsQuizOfTheDayExits().not()) {
            getQuizOfTheDay()
        }
        if (preferenceManager.checkIsWordOfTheDayExits().not()) {
            getWordOfTheDay()
        }
    }

     fun getQuizOfTheDay() {
        viewModelScope.launch(ioDispatcher) {
            val quizData = dictionaryRepo.getDictionaryRandomWord()
            val quizOptions = dictionaryRepo.getDictionaryRandomOptions()
            val quizQuestion = QuizOfTheDay(
                word = quizData[0],
                answer = quizData[1],
                optionList = quizOptions
            ).getQuestion()
            preferenceManager.saveQuizOfTheDay(quizQuestion)
        }

    }

     fun getWordOfTheDay()
    {
        viewModelScope.launch(ioDispatcher) {
            val wordData = dictionaryRepo.getDictionaryRandomDictionaryObject()
            val wordModel = WordOfTheDay(
                word = wordData.meaning.safeGet(),
                meaning = wordData.word.safeGet(),
            ).getWordOfTheDay()
            preferenceManager.saveWordOfTheDay(wordModel)
        }
    }


}