package com.core.domain.remote

import com.core.extensions.empty
import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("status")
    val status: Boolean = false

    @SerializedName("version")
    val version: String = String.empty

    @SerializedName("message")
    var message: String = String.empty
}