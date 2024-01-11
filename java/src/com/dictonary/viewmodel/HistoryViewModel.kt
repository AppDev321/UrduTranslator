package com.dictonary.viewmodel

import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.database.entity.HistoryEntity
import com.core.database.repo.history.HistoryRepo
import com.core.utils.PreferenceManager
import com.dictonary.navigator.HistoryNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepo: HistoryRepo,
    private val preferenceManager: PreferenceManager
) : BaseViewModel<HistoryNavigator>() {

    private val _historyDataList = MutableSharedFlow<List<HistoryEntity>>()
    val historyDataListObserver = _historyDataList.asSharedFlow()


    @Synchronized
    fun getHistoryDataList() {
        viewModelScope.launch(ioDispatcher) {
            val getDataList = historyRepo.getHistoryDataList()
            _historyDataList.emit( getDataList)
        }
    }



}