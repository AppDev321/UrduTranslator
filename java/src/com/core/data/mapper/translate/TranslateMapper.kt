package com.core.data.mapper.translate

import com.core.data.mapper.Mapper
import com.core.data.model.translate.TranslateResponse
import com.core.extensions.empty
import javax.inject.Inject


class TranslateMapper @Inject constructor() :
    Mapper<TranslateResponse?, String?> {

    override fun mapFrom(from: TranslateResponse?): String {
        return from?.translation ?: String.empty
    }
}

