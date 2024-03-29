package com.dictionary.model

data class LanguageModel(
    var flag: String = "",
    var language: String?,
    var code: String?,
    var isTextToSpeechSupporting: Boolean = true,
    var isSpeechToTextSupporting: Boolean = true
)