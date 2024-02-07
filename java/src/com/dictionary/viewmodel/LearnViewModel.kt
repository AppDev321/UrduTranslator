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
import java.util.Locale.Category
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val learnRepo: LearnRepo,
    private val preferenceManager: PreferenceManager,
) : BaseViewModel<LearnNavigator>() {

    @Synchronized
    fun getDetailListBySubCategory(subCategory: String) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val list = async {
                learnRepo.getDetailBySubCategory(subCategory)
            }.await()
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
                getNavigator()?.displayLearnDetailByCategory(list)
            }
        }
    }

    @Synchronized
    fun getDetailListByCategory(category: String) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val list = async {
                learnRepo.getCategoryDetailList(category)
            }.await()
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
                getNavigator()?.displayLearnDetailByCategory(list)
            }
        }
    }

    @Synchronized
    fun getLearnDetailByCategory(pos: Int) {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
            val dataList = when (pos) {
                0 -> {
                    learnRepo.getWords()
                }

                1 -> {
                    learnRepo.getPhrases()
                }

                2 -> {
                    learnRepo.getConversations()
                }

                3 -> {
                    learnRepo.getGrammars()
                }

                else -> learnRepo.getTenses()
            }

            val list = dataList.map {
                SettingItem(
                    img = 0,
                    name =
                    if (pos == 3 || pos ==4) {
                        it.categoryName.safeGet()
                    } else
                        it.subCategory.safeGet(),

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