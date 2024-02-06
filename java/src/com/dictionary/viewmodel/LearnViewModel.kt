package com.dictionary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.database.repo.learn.LearnRepo
import com.core.extensions.safeGet
import com.core.utils.PreferenceManager
import com.dictionary.model.SettingItem
import com.dictionary.model.SettingsDataFactory
import com.dictionary.navigator.LearnNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val learnRepo: LearnRepo,
    private val preferenceManager: PreferenceManager,
) : BaseViewModel<LearnNavigator>() {


    @Synchronized
    fun getCategoryList() {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val list = async {
                learnRepo.getCategoryList()
            }.await()
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)

            }
        }


    }

    @Synchronized
    fun getLearnDetailByCategory(pos: Int) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val dataList = when (pos) {
                2 -> {
                    learnRepo.getConversations()
                }
                else -> learnRepo.getCategoryList()
            }

           val list =  dataList.map {
               SettingItem(
                   img = 0,
                   name = it.subCategory.safeGet(),
                   viewType = SettingsDataFactory.ITEM_TYPE.LEARNSUBCATEGORY
               )
           }

            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
                getNavigator()?.displaySubCategoryList(list)
            }
        }

    }


}