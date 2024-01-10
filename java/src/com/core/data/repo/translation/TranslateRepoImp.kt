package com.core.data.repo.translation

import com.core.data.service.ApiServiceInterface
import retrofit2.Response
import javax.inject.Inject

class TranslateRepoImp @Inject constructor(private var apiServiceInterface: ApiServiceInterface) :
    TranslateRepo {
    override suspend fun getTranslation(
        from: String, to: String, text: String
    ): Response<String> {
        return apiServiceInterface.getTranslatedDate(
            sourceLanguage = from, targetLanguage = to, textToTranslate = text
        )
    }
}