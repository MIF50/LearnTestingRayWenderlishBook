package com.raywenderlich.android.cocktails.game.factory

import com.raywenderlich.android.cocktails.common.network.Cocktail
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.common.repository.RepositoryCallback
import com.raywenderlich.android.cocktails.game.model.Game
import com.raywenderlich.android.cocktails.game.model.Question
import com.raywenderlich.android.cocktails.game.model.Score

class CocktailsGameFactoryImpl(
        private val repository: CocktailsRepository
) : CocktailsGameFactory {

    override fun buildGame(callBack: CocktailsGameFactory.CallBack) {
        repository.getAlcoholic(object : RepositoryCallback<List<Cocktail>, String> {
            override fun onSuccess(t: List<Cocktail>) {
                val questions = buildQuestions(t)
                val score = Score(repository.getHighScore())
                val game = Game(questions, score)
                callBack.onSuccess(game)
            }

            override fun onError(e: String) {
                callBack.onError()
            }

        })
    }

    private fun buildQuestions(cocktailList: List<Cocktail>) = cocktailList.map { cocktail ->
        val otherCocktail = cocktailList.shuffled().first { it != cocktail }
        Question(
                cocktail.strDrink,
                otherCocktail.strDrink,
                cocktail.strDrinkThumb
        )
    }

}