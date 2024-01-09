package com.core.interfaces

import android.view.MenuItem
import com.core.extensions.empty

interface BaseNavigator {
    fun prepareAlert(title: Int, messageResourceId: Int = 0, message: String = String.empty) = Unit
    fun setProgressVisibility(visibility: Int) = Unit

}