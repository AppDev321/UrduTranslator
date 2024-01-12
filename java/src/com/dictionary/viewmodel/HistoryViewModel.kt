package com.dictionary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.database.entity.HistoryEntity
import com.core.database.repo.history.HistoryRepo
import com.core.utils.PreferenceManager
import com.dictionary.navigator.HistoryNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyRepo: HistoryRepo,
    private val preferenceManager: PreferenceManager
) : BaseViewModel<HistoryNavigator>() {

    private val _historyDataList = MutableSharedFlow<List<HistoryEntity>>()
    val historyDataListObserver = _historyDataList.asSharedFlow()


    @Synchronized
    fun getDataList(isFav: Boolean) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val getDataList = async {
                if (isFav)
                    historyRepo.getFavouriteDataList()
                else
                    historyRepo.getHistoryDataList()
            }.await()
            _historyDataList.emit(getDataList)
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
            }
        }
    }

    fun performFavAction(item:HistoryEntity)
    {
        viewModelScope.launch(ioDispatcher) {
            historyRepo.updateFav(!item.isFav!!,item.id!!.toInt())
            item.isFav = !item.isFav!!
            withContext(mainDispatcher){
                getNavigator()?.favouriteItemUpdated(item)
            }
        }
    }


}