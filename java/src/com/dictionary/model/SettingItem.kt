package com.dictionary.model

import android.content.Context
import com.android.inputmethod.latin.R
import javax.inject.Inject

data class SettingItem(
    val img: Int,
    val name: String,
    val viewType: SettingsDataFactory.ITEM_TYPE = SettingsDataFactory.ITEM_TYPE.TEXT
)


class SettingsDataFactory
@Inject constructor(
    private val context: Context,
) {

    enum class ITEM_TYPE {
        TEXT,
        DROPDOWN,
    }

    fun getMoreFragmentData(): ArrayList<SettingItem> {
        return arrayListOf(

            SettingItem(
                R.drawable.ic_keyboard,
                "Urdu Keyboard",
            ),

            SettingItem(
                R.drawable.ic_urdu_editor,
                "Image Editor",
            ),

            )
    }
}