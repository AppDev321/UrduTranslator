package com.core.utils

import android.content.Context
import com.core.database.entity.DictionaryEntity
import com.dictionary.model.QuizOfTheDay
import com.dictionary.model.WordOfTheDay
import com.google.gson.Gson
import org.apache.commons.net.telnet.TelnetOption.getOption


class PreferenceManager constructor(val context: Context) {

    private val sharedPreferences by lazy {
        val preferences =
            context.getSharedPreferences(APP_PREFERENCES_FILE, Context.MODE_PRIVATE)
        preferences
    }

    // region COMPANION OBJECT
    companion object {
        private const val APP_PREFERENCES_FILE = "app_pref_file"
        private const val SETTINGS_APPLICATION_LOGGER = "app_log_status"
        private const val PREF_FROM_CODE = "from_code"
        private const val PREF_TO_CODE = "to_code"
        private const val PREF_FROM_LANG_TEXT = "from_code_lang_text"
        private const val PREF_TO_LANG_TEXT = "to_code_lang_text"
        private const val PREF_QUIZ_OF_THE_DAY = "quiz_of_the_day"
        private const val PREF_WORD_OF_THE_DAY = "word_of_the_day"
    }
    // endregion

    fun setApplicationLogsEnable(enable: Boolean) {
        setBooleanPolicy(SETTINGS_APPLICATION_LOGGER, enable)
    }

    fun isApplicationLogsEnabled() = (getBooleanPolicyWithDefaultTrue(SETTINGS_APPLICATION_LOGGER))


    private fun setBooleanPolicy(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun getBooleanPolicyWithDefaultTrue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }

    private fun getBooleanPolicy(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }


     fun setPrefToLangCode(code : String){
        sharedPreferences.edit().putString(PREF_TO_CODE, code).apply()
    }
     fun getPrefToLangCode(): String {
        return sharedPreferences.getString(PREF_TO_CODE,"ur") as String
    }

    fun setPrefFromLangCode(code : String){
        sharedPreferences.edit().putString(PREF_FROM_CODE, code).apply()
    }
    fun getPrefFromLangCode(): String {
        return sharedPreferences.getString(PREF_FROM_CODE,"en") as String
    }

    fun setPrefToLangText(code : String){
        sharedPreferences.edit().putString(PREF_TO_LANG_TEXT, code).apply()
    }
    fun getPrefToLangText(): String {
        return sharedPreferences.getString(PREF_TO_LANG_TEXT,"Urdu") as String
    }

    fun setPrefFromLangText(code : String){
        sharedPreferences.edit().putString(PREF_FROM_LANG_TEXT,code).apply()
    }
    fun getPrefFromLangText(): String {
        return sharedPreferences.getString(PREF_FROM_LANG_TEXT,"English") as String
    }

    fun saveQuizOfTheDay(data: QuizOfTheDay?) {
        val jsonString =  Gson().toJson(data)
        sharedPreferences.edit().putString(PREF_QUIZ_OF_THE_DAY, jsonString).apply()
    }

    fun getQuizOfTheDay(): QuizOfTheDay? {
        val jsonString = sharedPreferences.getString(PREF_QUIZ_OF_THE_DAY, null)
        return Gson().fromJson(jsonString, QuizOfTheDay::class.java)
    }

    fun checkIsQuizOfTheDayExits():Boolean
    {
        return getQuizOfTheDay() != null
    }



    fun saveWordOfTheDay(data: WordOfTheDay?) {
        val jsonString =  Gson().toJson(data)
        sharedPreferences.edit().putString(PREF_WORD_OF_THE_DAY, jsonString).apply()
    }

    fun getWordOfTheDay(): WordOfTheDay? {
        val jsonString = sharedPreferences.getString(PREF_WORD_OF_THE_DAY, null)
        return Gson().fromJson(jsonString, WordOfTheDay::class.java)
    }

    fun checkIsWordOfTheDayExits():Boolean
    {
        return getWordOfTheDay() != null
    }


}