package com.raywenderlich.android.cocktails.game.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.game.factory.CocktailsGameFactory
import com.raywenderlich.android.cocktails.game.model.Game
import com.raywenderlich.android.cocktails.game.model.Question
import com.raywenderlich.android.cocktails.game.model.Score
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InOrder
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CocktailsGameViewModelUnitTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CocktailsGameViewModel

    @Mock
    private lateinit var repository: CocktailsRepository
    @Mock
    private lateinit var factory: CocktailsGameFactory
    @Mock
    private lateinit var game: Game
    @Mock
    private lateinit var loadingObserver: Observer<Boolean>
    @Mock
    private lateinit var errorObserver: Observer<Boolean>
    @Mock
    private lateinit var scoreObserver: Observer<Score>
    @Mock
    private lateinit var questionObserver: Observer<Question>


    @Before
    fun setup() {
        viewModel = CocktailsGameViewModel(repository, factory)
        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getScore().observeForever(scoreObserver)
        viewModel.getQuestion().observeForever(questionObserver)
        viewModel.getError().observeForever(errorObserver)
    }

    @Test
    fun init_shouldBuildGame(){
        viewModel.initGame()
        verify(factory).buildGame(any())
    }

    @Test
    fun init_shouldShowLoading(){
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun init_shouldHideError(){
        viewModel.initGame()
        verify(errorObserver).onChanged(eq(false))
    }

    @Test
    fun init_shouldShowError_whenFactoryReturnError(){
        setupFactoryWithError()
        viewModel.initGame()
        verify(errorObserver).onChanged(eq(true))
    }

    @Test
    fun init_showHideLoading_whenFactoryReturnError(){
        setupFactoryWithError()
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun init_showHideLoading_whenFactoryReturnSuccess(){
        setupFactoryWithSuccessGame(game)
        viewModel.initGame()
        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun init_shouldShowScore_whenFactoryReturnSuccess(){
        val score = mock<Score>()
        whenever(game.score).thenReturn(score)
        setupFactoryWithSuccessGame(game)

        viewModel.initGame()

        verify(scoreObserver).onChanged(eq(score))
    }

    @Test
    fun init_shouldShowFirstQuestion_whenFactoryReturnSuccess(){
        val question = mock<Question>()
        whenever(game.nextQuestion()).thenReturn(question)
        setupFactoryWithSuccessGame(game)

        viewModel.initGame()

        verify(questionObserver).onChanged(eq(question))

    }

    @Test
    fun nextQuestion_shouldShowQuestion(){
        val question1 = mock<Question>()
        val question2 = mock<Question>()
        whenever(game.nextQuestion())
                .thenReturn(question1)
                .thenReturn(question2)
        setupFactoryWithSuccessGame(game)

        viewModel.initGame()
        viewModel.nextQuestion()

        verify(questionObserver).onChanged(eq(question2))
    }

    @Test
    fun answerQuestion_shouldDelegateToGame_showHighScore_showQuestionAndScore(){
        val score = mock<Score>()
        val question = mock<Question>()
        whenever(game.score).thenReturn(score)
        setupFactoryWithSuccessGame(game)
        viewModel.initGame()

        viewModel.answerQuestion(question,"VALUE")

        inOrder(game,repository,questionObserver,scoreObserver){
            verify(game).answer(eq(question), eq("VALUE"))
            verify(repository).saveHighScore(any())
            verify(scoreObserver).onChanged(eq(score))
            verify(questionObserver).onChanged(eq(question))

        }

    }


    // MARK:- Helper Methods

    private fun setupFactoryWithSuccessGame(game: Game){
        doAnswer {
            val callback = it.getArgument<CocktailsGameFactory.CallBack>(0)
            callback.onSuccess(game)
        }.whenever(factory).buildGame(any())
    }

    private fun setupFactoryWithError(){
        doAnswer {
            val callback = it.getArgument<CocktailsGameFactory.CallBack>(0)
            callback.onError()
        }.whenever(factory).buildGame(any())
    }
}