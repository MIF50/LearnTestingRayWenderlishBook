package com.raywenderlich.android.cocktails.game.factory

import com.raywenderlich.android.cocktails.common.network.Cocktail
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.common.repository.RepositoryCallback

class CocktailsGameFactoryImpl (
        private val repository: CocktailsRepository
): CocktailsGameFactory {

    override fun buildGame(callBack: CocktailsGameFactory.CallBack) {
        repository.getAlcoholic(object : RepositoryCallback<List<Cocktail>, String>{
            override fun onSuccess(t: List<Cocktail>) {
                TODO("Not yet implemented")
            }

            override fun onError(e: String) {
                TODO("Not yet implemented")
            }

        })
    }

}