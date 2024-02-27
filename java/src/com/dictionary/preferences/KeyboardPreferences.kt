package com.dictionary.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class KeyboardPreferences constructor(context: Context) {
    var sharedPreferences: SharedPreferences

    var editor: Editor

    var language: Boolean
        get() = sharedPreferences.getBoolean(LANGUAGE, false)
        set(lang) {
            editor.putBoolean(LANGUAGE, lang)
            editor.apply()
        }

    companion object {
        const val PREFS_NAME = "translation_keyboard"
        const val LANGUAGE = "language"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
}