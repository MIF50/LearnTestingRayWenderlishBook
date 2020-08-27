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

class CocktailsGameViewModelUnitTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory
    private lateinit var viewModel: CocktailsGameViewModel
    private lateinit var game: Game
    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<Boolean>
    private lateinit var scoreObserver: Observer<Score>
    private lateinit var questionObserver: Observer<Question>


    @Before
    fun setup() {
        repository = mock()
        factory = mock()
        viewModel = CocktailsGameViewModel(repository, factory)

        game = mock()

        loadingObserver = mock()
        errorObserver = mock()
        scoreObserver = mock()
        questionObserver = mock()

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
    fun init_shouldShowScore_whenFactoryReturnSuccess(){
        val score = mock<Score>()
        whenever(game.score).thenReturn(score)
        setupFactoryWithSuccessGame(game)

        viewModel.initGame()

        verify(scoreObserver).onChanged(eq(score))
    }





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