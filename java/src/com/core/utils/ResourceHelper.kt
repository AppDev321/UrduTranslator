package com.core.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourceHelper @Inject constructor(private val context: Context) {

    fun getString(@StringRes resourceId: Int): String = context.getString(resourceId)
    fun getString(@StringRes resourceId: Int, stringToAppendIfAny: String): String =
        context.getString(resourceId, stringToAppendIfAny)

}