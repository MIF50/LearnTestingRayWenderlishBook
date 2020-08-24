package com.raywenderlich.android.cocktails.game.factory

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import org.junit.Before
import org.junit.Test

class CocktailsGameFactoryUnitTests {

    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory

    @Before
    fun setup(){
        repository = mock()
        factory = CocktailsGameFactoryImpl(repository)
    }

    @Test
    fun buildGame_shouldGetCocktailsFromReps(){
        factory.buildGame(mock())
        verify(repository).getAlcoholic(any())
    }
}