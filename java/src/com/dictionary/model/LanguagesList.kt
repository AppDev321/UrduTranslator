
package com.dictionary.model

import com.google.gson.Gson

val jsonData = """
{
    "text": [
    {
        "language": "Afrikaans",
        "code": "af"
    },
    {
        "language": "Albanian",
        "code": "sq"
    },
    {
        "language": "Amharic",
        "code": "am"
    },
    {
        "language": "Arabic",
        "code": "ar"
    },
    {
        "language": "Armenian",
        "code": "hy"
    },
    {
        "language": "Azerbaijani",
        "code": "az"
    },
    {
        "language": "Basque",
        "code": "eu"
    },
    {
        "language": "Belarusian",
        "code": "be"
    },
    {
        "language": "Bengali",
        "code": "bn"
    },
    {
        "language": "Bosnian",
        "code": "bs"
    },
    {
        "language": "Bulgarian",
        "code": "bg"
    },
    {
        "language": "Catalan",
        "code": "ca"
    },
    {
        "language": "Cebuano",
        "code": "ceb"
    },
    {
        "language": "Chinese (Simplified)",
        "code": "zh-CN"
    },
    {
        "language": "Chinese (Traditional)",
        "code": "zh-TW"
    },
    {
        "language": "Corsican",
        "code": "co"
    },
    {
        "language": "Croatian",
        "code": "hr"
    },
    {
        "language": "Czech",
        "code": "cs"
    },
    {
        "language": "Danish",
        "code": "da"
    },
    {
        "language": "Dutch",
        "code": "nl"
    },
    {
        "language": "English",
        "code": "en"
    },
    {
        "language": "Esperanto",
        "code": "eo"
    },
    {
        "language": "Estonian",
        "code": "et"
    },
    {
        "language": "Finnish",
        "code": "fi"
    },
    {
        "language": "French",
        "code": "fr"
    },
    {
        "language": "Frisian",
        "code": "fy"
    },
    {
        "language": "Galician",
        "code": "gl"
    },
    {
        "language": "Georgian",
        "code": "ka"
    },
    {
        "language": "German",
        "code": "de"
    },
    {
        "language": "Greek",
        "code": "el"
    },
    {
        "language": "Gujarati",
        "code": "gu"
    },
    {
        "language": "Haitian Creole",
        "code": "ht"
    },
    {
        "language": "Hausa",
        "code": "ha"
    },
    {
        "language": "Hawaiian",
        "code": "haw"
    },
    {
        "language": "Hebrew",
        "code": "iw"
    },
    {
        "language": "Hindi",
        "code": "hi"
    },
    {
        "language": "Hmong",
        "code": "hmn"
    },
    {
        "language": "Hungarian",
        "code": "hu"
    },
    {
        "language": "Icelandic",
        "code": "is"
    },
    {
        "language": "Igbo",
        "code": "ig"
    },
    {
        "language": "Indonesian",
        "code": "id"
    },
    {
        "language": "Irish",
        "code": "ga"
    },
    {
        "language": "Italian",
        "code": "it"
    },
    {
        "language": "Japanese",
        "code": "ja"
    },
    {
        "language": "Javanese",
        "code": "jw"
    },
    {
        "language": "Kannada",
        "code": "kn"
    },
    {
        "language": "Kazakh",
        "code": "kk"
    },
    {
        "language": "Khmer",
        "code": "km"
    },
    {
        "language": "Korean",
        "code": "ko"
    },
    {
        "language": "Kurdish",
        "code": "ku"
    },
    {
        "language": "Kyrgyz",
        "code": "ky"
    },
    {
        "language": "Lao",
        "code": "lo"
    },
    {
        "language": "Latin",
        "code": "la"
    },
    {
        "language": "Latvian",
        "code": "lv"
    },
    {
        "language": "Lithuanian",
        "code": "lt"
    },
    {
        "language": "Luxembourgish",
        "code": "lb"
    },
    {
        "language": "Macedonian",
        "code": "mk"
    },
    {
        "language": "Malagasy",
        "code": "mg"
    },
    {
        "language": "Malay",
        "code": "ms"
    },
    {
        "language": "Malayalam",
        "code": "ml"
    },
    {
        "language": "Maltese",
        "code": "mt"
    },
    {
        "language": "Maori",
        "code": "mi"
    },
    {
        "language": "Marathi",
        "code": "mr"
    },
    {
        "language": "Mongolian",
        "code": "mn"
    },
    {
        "language": "Myanmar (Burmese)",
        "code": "my"
    },
    {
        "language": "Nepali",
        "code": "ne"
    },
    {
        "language": "Norwegian",
        "code": "no"
    },
    {
        "language": "Nyanja (Chichewa)",
        "code": "ny"
    },
    {
        "language": "Pashto",
        "code": "ps"
    },
    {
        "language": "Persian",
        "code": "fa"
    },
    {
        "language": "Polish",
        "code": "pl"
    },
    {
        "language": "Portuguese (Portugal, Brazil)",
        "code": "pt"
    },
    {
        "language": "Punjabi",
        "code": "pa"
    },
    {
        "language": "Romanian",
        "code": "ro"
    },
    {
        "language": "Russian",
        "code": "ru"
    },
    {
        "language": "Samoan",
        "code": "sm"
    },
    {
        "language": "Scots Gaelic",
        "code": "gd"
    },
    {
        "language": "Serbian",
        "code": "sr"
    },
    {
        "language": "Sesotho",
        "code": "st"
    },
    {
        "language": "Shona",
        "code": "sn"
    },
    {
        "language": "Sindhi",
        "code": "sd"
    },
    {
        "language": "Sinhala (Sinhalese)",
        "code": "si"
    },
    {
        "language": "Slovak",
        "code": "sk"
    },
    {
        "language": "Slovenian",
        "code": "sl"
    },
    {
        "language": "Somali",
        "code": "so"
    },
    {
        "language": "Spanish",
        "code": "es"
    },
    {
        "language": "Sundanese",
        "code": "su"
    },
    {
        "language": "Swahili",
        "code": "sw"
    },
    {
        "language": "Swedish",
        "code": "sv"
    },
    {
        "language": "Tagalog (Filipino)",
        "code": "tl"
    },
    {
        "language": "Tajik",
        "code": "tg"
    },
    {
        "language": "Tamil",
        "code": "ta"
    },
    {
        "language": "Telugu",
        "code": "te"
    },
    {
        "language": "Thai",
        "code": "th"
    },
    {
        "language": "Turkish",
        "code": "tr"
    },
    {
        "language": "Ukrainian",
        "code": "uk"
    },
    {
        "language": "Urdu",
        "code": "ur"
    },
    {
        "language": "Uzbek",
        "code": "uz"
    },
    {
        "language": "Vietnamese",
        "code": "vi"
    },
    {
        "language": "Welsh",
        "code": "cy"
    },
    {
        "language": "Xhosa",
        "code": "xh"
    },
    {
        "language": "Yiddish",
        "code": "yi"
    },
    {
        "language": "Yoruba",
        "code": "yo"
    },
    {
        "language": "Zulu",
        "code": "zu"
    }
    ]
}
"""






object LanguagesList {
    var allLanguageDataList: MutableList<LanguageModel> = arrayListOf()
    fun getAllLanguagesList():List<LanguageModel>{
    val languageDataMap = Gson().fromJson(jsonData, Map::class.java)
    val textLanguages = languageDataMap["text"] as? List<Map<String, String>> ?: emptyList()
    val dataList = textLanguages.map { languageMap ->
        LanguageModel(
            "file:///android_asset/flags/${languageMap["code"]}.png",
            languageMap["language"] ?: "",
            languageMap["code"] ?: "",
            // isTextToSpeechSupporting = ttsLanguages.any { it["code"] == languageMap["code"] },
            //  isSpeechToTextSupporting = textLanguages.any { it["code"] == languageMap["code"] }
        )
    }
        return dataList
    }
    fun getAllLanguagesList2(): MutableList<LanguageModel> {
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