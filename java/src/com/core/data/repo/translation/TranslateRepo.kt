package com.core.data.repo.translation

import retrofit2.Response

interface TranslateRepo {
    suspend fun getTranslation(from:String,to:String,text:String): Response<String>
}