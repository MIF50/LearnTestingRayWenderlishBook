package com.raywenderlich.android.cocktails.common.repository

interface CocktailsRepository {

    fun saveHighScore(score: Int)
    fun getHighScore(): Int
}