
package com.dictionary.model


object LanguagesList {
    var allLanguageDataList: MutableList<LanguageModel> = arrayListOf()
    fun getAllLanguagesList(): MutableList<LanguageModel> {
        allLanguageDataList = ArrayList()
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/za.png",
                "Afrikaans",
                "af",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/al.png",
                "Albanian",
                "sq",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/sa.png",
                "Arabic",
                "ar",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/by.png",
                "Belarusian",
                "be",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/bg.png",
                "Bulgarian",
                "bg",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ca.png",
                "Catalan",
                "ca",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/cz.png",
                "Czech",
                "cs",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/cn.png",
                "Chinese",
                "zh",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/hr.png",
                "Croatian",
                "hr",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/dk.png",
                "Danish",
                "da",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/nl.png",
                "Dutch",
                "nl",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/us.png",
                "English",
                "en",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        //  languageDataList.add(new LanguageModel("file:///android_asset/flags/eo.png", "Esperanto", "eo", false, false));
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ee.png",
                "Estonian",
                "et",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/fi.png",
                "Finnish",
                "fi",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/fr.png",
                "French",
                "fr",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ga.png",
                "Galician",
                "gl",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ge.png",
                "Georgian",
                "ka",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/de.png",
                "German",
                "de",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/eu.png",
                "Greek",
                "el",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/in.png",
                "Gujarati",
                "gu",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/gt.png",
                "Haitian",
                "ht",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/il.png",
                "Hebrew",
                "he",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/in.png",
                "Hindi",
                "hi",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/hu.png",
                "Hungarian",
                "hu",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/is.png",
                "Icelandic",
                "is",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/id.png",
                "Indonesian",
                "id",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ie.png",
                "Irish",
                "ga",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/it.png",
                "Italian",
                "it",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/jp.png",
                "Japanese",
                "ja",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        //    languageDataList.add(new LanguageModel("file:///android_asset/flags/kn.png", "Kannada", "kn", false, false));
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/kr.png",
                "Korean",
                "ko",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/lv.png",
                "Latvian",
                "lv",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/lt.png",
                "Lithuanian",
                "lt",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/mk.png",
                "Macedonian",
                "mk",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/my.png",
                "Malay",
                "ms",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/mt.png",
                "Maltese",
                "mt",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/in.png",
                "Marathi",
                "mr",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/no.png",
                "Norwegian",
                "no",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ir.png",
                "Persian",
                "fa",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/pl.png",
                "Polish",
                "pl",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/pt.png",
                "Portuguese",
                "pt",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ro.png",
                "Romanian",
                "ro",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ru.png",
                "Russian",
                "ru",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/sk.png",
                "Slovak",
                "sk",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/si.png",
                "Slovenian",
                "sl",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/es.png",
                "Spanish",
                "es",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ke.png",
                "Swahili",
                "sw",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/se.png",
                "Swedish",
                "sv",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ph.png",
                "Tagalog",
                "tl",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/in.png",
                "Tamil",
                "ta",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/in.png",
                "Telugu",
                "te",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/th.png",
                "Thai",
                "th",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/tr.png",
                "Turkish",
                "tr",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/ua.png",
                "Ukrainian",
                "uk",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/pk.png",
                "Urdu",
                "ur",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/vn.png",
                "Vietnamese",
                "vi",
                isTextToSpeechSupporting = true,
                isSpeechToTextSupporting = true
            )
        )
        allLanguageDataList.add(
            LanguageModel(
                "file:///android_asset/flags/gb-wls.png",
                "Welsh",
                "cy",
                isTextToSpeechSupporting = false,
                isSpeechToTextSupporting = false
            )
        )
        return allLanguageDataList
    }
}