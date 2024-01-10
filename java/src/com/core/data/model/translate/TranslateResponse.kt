package com.core.data.model.translate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TranslateResponse(
    val translation: String
): Parcelable