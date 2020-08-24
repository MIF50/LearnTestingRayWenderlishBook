package com.raywenderlich.android.cocktails.game.factory

import com.raywenderlich.android.cocktails.game.model.Game

interface CocktailsGameFactory {
    fun buildGame(callBack: CallBack)

    interface CallBack {
        fun onSuccess(game: Game)
        fun onError()
    }
}