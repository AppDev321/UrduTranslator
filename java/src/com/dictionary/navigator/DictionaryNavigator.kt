package com.dictionary.navigator

import com.core.database.entity.DictionaryEntity
import com.core.interfaces.BaseNavigator

interface DictionaryNavigator : BaseNavigator {

    fun displayDictionaryDataList(item:List<DictionaryEntity>,isEnglish :Boolean)

}