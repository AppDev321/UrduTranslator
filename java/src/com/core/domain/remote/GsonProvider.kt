package com.core.domain.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class GsonProvider {

    companion object{
        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
    }

    val instance : Gson by lazy {
        GsonBuilder().setDateFormat(ISO_8601_DATE_FORMAT).create()
    }
}