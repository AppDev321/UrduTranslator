package com.dictionary.navigator

import com.core.database.entity.LearnEntity
import com.core.interfaces.BaseNavigator
import com.dictionary.model.SettingItem

interface LearnNavigator : BaseNavigator {

    fun displaySubCategoryList(item:List<SettingItem>) = Unit
    fun displayLearnDetailByCategory(item:List<LearnEntity>) = Unit
}