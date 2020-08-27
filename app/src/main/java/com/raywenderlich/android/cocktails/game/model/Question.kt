package com.raywenderlich.android.cocktails.game.model

import java.lang.IllegalArgumentException

class Question(
         val correctOption: String,
         val inCorrectOption: String,
         val imageUrl: String? = null
) {
    var answeredOption: String? = null
        private set

    private val isAnsweredCorrectly: Boolean
        get() = answeredOption == correctOption

    fun answer(option: String): Boolean {
        if (option != correctOption && option != inCorrectOption) {
            throw IllegalArgumentException("Not a valid optional")
        }
        answeredOption = option
        return isAnsweredCorrectly
    }

    fun getOptions(sort: (List<String>) -> List<String> = { it.shuffled() } ) =
            sort(listOf(correctOption,inCorrectOption))
}