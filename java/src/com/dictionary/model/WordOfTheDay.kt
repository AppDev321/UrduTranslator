package com.dictionary.model

data class WordOfTheDay(
    val question: String = "Do you know the meaning of",
    val word: String = "quizWord",
    val meaning:String = "meaing"
){

    fun getWordOfTheDay():WordOfTheDay{
        return WordOfTheDay(
            question = "$question $word?",
            word = this.word,
            meaning =this.meaning
        )
    }
}