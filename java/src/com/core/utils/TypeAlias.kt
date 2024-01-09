package com.core.utils

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import java.util.ArrayList

typealias Inflate<T> = (LayoutInflater) -> T
typealias TextAndToneInputListener = (inputText: String?, tones: Triple<Int, Int, Int>?) -> Unit

inline fun <reified T : Parcelable> Bundle.getParcelableArrayListCompat(key: String): ArrayList<T>? =
    when {
        Utils.androidTIRAMISUAndAbove -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }

inline fun <reified T : Parcelable> Intent.getParcelableArrayListExtraCompat(key: String): ArrayList<T>? =
    when {
        Utils.androidTIRAMISUAndAbove -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }
