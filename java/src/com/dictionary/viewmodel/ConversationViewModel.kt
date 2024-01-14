package com.dictionary.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.core.base.BaseViewModel
import com.core.database.entity.ConversationEntity
import com.core.database.repo.conversation.ConversationRepo
import com.core.utils.PreferenceManager
import com.dictionary.navigator.ConversationNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val conversationRepo: ConversationRepo,
    private val preferenceManager: PreferenceManager
) : BaseViewModel<ConversationNavigator>() {

    @Synchronized
    fun getConversationList() {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        viewModelScope.launch(ioDispatcher) {
         val list = async {
              conversationRepo.getConversationList()
          }.await()
            withContext(mainDispatcher)
            {
                getNavigator()?.setProgressVisibility(View.GONE)
                getNavigator()?.displayConversationList(list)
            }
        }

    }

    fun addConversationData(entity:ConversationEntity)
    {
        viewModelScope.launch {
            entity.id = conversationRepo.getMaxRowID()
            conversationRepo.insertConversationData(entity)
        }
        getNavigator()?.addConversation(entity)
    }


}