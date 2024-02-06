package com.dictionary.model

import android.content.Context
import com.android.inputmethod.latin.R
import javax.inject.Inject

data class SettingItem(
    val img: Int,
    val name: String,
    val urduName: String ="",
    val viewType: SettingsDataFactory.ITEM_TYPE = SettingsDataFactory.ITEM_TYPE.TEXT
)


class SettingsDataFactory
@Inject constructor(
    private val context: Context,
) {

    enum class ITEM_TYPE {
        TEXT,
        DROPDOWN,
        LEARNSUBCATEGORY
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

    fun getLearningTypes(): ArrayList<SettingItem> {
        return arrayListOf(

            SettingItem(
                R.drawable.ic_words,
                "Words",
                urduName = "الفاظ"
            ),

            SettingItem(
                R.drawable.ic_phrase,
                "Phrases",
                urduName = "جملے"
            ),
            SettingItem(
                R.drawable.ic_chat_translator,
                "Chat",
                urduName = "گپ شپ"
            ),

            SettingItem(
                R.drawable.ic_grammar,
                "Grammar",
                urduName = "گرائمر"
            ),
            SettingItem(
                R.drawable.ic_tenses,
                "Tenses",
                urduName = "زمانہ"
            ),
            )
    }
}