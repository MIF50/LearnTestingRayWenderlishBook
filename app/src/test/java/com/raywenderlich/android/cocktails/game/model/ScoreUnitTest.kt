package com.raywenderlich.android.cocktails.game.model

import org.junit.Assert
import org.junit.Test

class ScoreUnitTest {

    @Test
    fun whenIncrementScore_shouldIncrementCurrentScore(){
        val score = Score()
        score.increment()
        Assert.assertEquals(1,score.current)
    }

    @Test
    fun whenIncrementScore_aboveHighScore_shouldAlsoIncrementHighScore(){
        val score = Score()
        score.increment()
        Assert.assertEquals(1,score.highest)
    }

    @Test
    fun whenIncrementScore_belowHighScore_shouldNotIncrementHighScore(){
        val score = Score(10)
        score.increment()
        Assert.assertEquals(10,score.highest)
    }
}