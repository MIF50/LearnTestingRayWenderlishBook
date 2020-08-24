package com.raywenderlich.android.cocktails.game.model

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class QuestionUnitTests {

    private lateinit var question: Question

    @Before
    fun setup(){
        question = Question("CORRECT","INCORRECT")
    }

    @Test
    fun whenCreateQuestion_shouldNotHaveAnsweredOption(){
        Assert.assertNull(question.answeredOption)
    }

    @Test
    fun whenAnswering_shouldHaveAnsweredOption(){
        question.answer("INCORRECT")
        Assert.assertEquals("INCORRECT",question.answeredOption)
    }

    @Test
    fun whenAnswering_withCorrectOption_shouldReturnTrue(){
        val result = question.answer("CORRECT")
        Assert.assertTrue(result)
    }

    @Test
    fun whenAnswering_withInCorrectOption_shouldReturnFalse(){
        val result = question.answer("INCORRECT")
        Assert.assertFalse(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun whenAnswering_withInValidOption_shouldThrowException(){
        question.answer("InValid")
    }

    @Test
    fun whenCreateQuestion_shouldReturnOptionsWithCustomSort(){
        val options = question.getOptions{ it.reversed() }
        Assert.assertEquals(listOf("INCORRECT","CORRECT"),options)
    }
}