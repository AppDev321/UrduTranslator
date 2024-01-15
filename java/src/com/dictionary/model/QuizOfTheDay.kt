package com.dictionary.model

data class QuizOfTheDay(
    val question: String = "What is the meaning of",
    val word: String = "quizWord",
    val answer: String = "answer",
    val optionList: List<String> = arrayListOf("a", "b", "c")

){
    fun getQuestion(): QuizOfTheDay {
        return QuizOfTheDay(
            question = "$question $word?",
            word = word,
            answer = this.answer,
            optionList = optionList
        )
    }

}