package com.dictionary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.database.repo.dictionary.DictionaryRepo
import com.core.utils.PreferenceManager
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
    fun getDictionaryData() {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
         val list = async {
              dictionaryRepo.getDictionaryDataList()
          }.await()
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
                getNavigator()?.displayDictionaryDataList(list)
            }
        }

    }


}