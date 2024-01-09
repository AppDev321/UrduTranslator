package com.core.utils

import android.content.Context
import com.android.inputmethod.latin.BuildConfig
import java.util.prefs.Preferences


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


}