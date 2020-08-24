package com.raywenderlich.android.cocktails.game.model

import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class GameUnitTests {


    @Test
    fun whenGettingNextQuestion_shouldReturnIt() {
        val questions = ArrayList<Question>()
        questions.add(Question("CORRECT", "INCORRECT"))
        val game = Game(questions)
        val nextQuestion = game.nextQuestion()
        assertEquals(nextQuestion, questions.first())

    }

    @Test
    fun whenGettingNextQuestion_withoutMoreQuestion_shouldReturnNull() {
        val questions = ArrayList<Question>()
        questions.add(Question("CORRECT", "INCORRECT"))
        val game = Game(questions)
        game.nextQuestion()
        val nextQuestion = game.nextQuestion()
        assertNull(nextQuestion)
    }

    @Test
    fun whenAnswering_shouldDelegateQuestion(){
        val question = mock<Question>()
        val game = Game(listOf(question))

        game.answer(question,"OPTION")

        verify(question, times(1)).answer("OPTION")
    }

    @Test
    fun whenAnsweringCorrectly_shouldIncrementCurrentScore(){
        val question = mock<Question>()
        whenever(question.answer(anyString())).thenReturn(true)
        val score = mock<Score>()

        val game = Game(listOf(question),score)
        game.answer(question,"OPTION")

        verify(score).increment()

    }

    @Test
    fun whenAnsweringInCorrectly_shouldNotIncrementCurrentScore(){
        val question = mock<Question>()
        whenever(question.answer(anyString())).thenReturn(false)
        val score = mock<Score>()

        val game = Game(listOf(question),score)
        game.answer(question,"QUESTION")

        verify(score, never()).increment()
    }

}