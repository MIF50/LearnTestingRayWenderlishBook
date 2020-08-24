package com.raywenderlich.android.cocktails.game.model


class Game(
        private val questions: List<Question>,
        private val score: Score = Score(0)
) {


    private var questionIndex = -1



    fun nextQuestion(): Question? {
        return if (questionIndex + 1 < questions.count() ){
            questionIndex++
            questions[questionIndex]
        } else {
            null
        }
    }

    fun answer(question: Question, option: String){
        val result = question.answer(option)
        if (result) {
            score.increment()
        }
    }
}