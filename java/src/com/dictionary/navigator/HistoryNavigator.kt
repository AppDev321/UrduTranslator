package com.dictionary.navigator

import com.core.database.entity.HistoryEntity
import com.core.interfaces.BaseNavigator

interface HistoryNavigator : BaseNavigator {

    fun favouriteItemUpdated(item:HistoryEntity)

}