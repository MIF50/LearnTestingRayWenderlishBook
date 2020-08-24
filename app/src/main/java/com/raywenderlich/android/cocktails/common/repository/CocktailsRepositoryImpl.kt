package com.raywenderlich.android.cocktails.common.repository

import android.content.SharedPreferences
import com.raywenderlich.android.cocktails.common.network.CocktailsApi

private const val HIGH_SCORE_KEY = "high_score_key"
class CocktailsRepositoryImpl (
        private val api: CocktailsApi,
        private val sharedPreferences: SharedPreferences
): CocktailsRepository {

    override fun saveHighScore(score: Int) {
        val highScore = getHighScore()
        if (score > highScore) {
            val editor = sharedPreferences.edit()
            editor.putInt(HIGH_SCORE_KEY, score)
            editor.apply()
        }
    }

    override fun getHighScore(): Int = sharedPreferences.getInt(HIGH_SCORE_KEY,0)

}