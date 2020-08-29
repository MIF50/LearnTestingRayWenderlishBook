package com.raywenderlich.android.cocktails.game.factory

import com.nhaarman.mockitokotlin2.*
import com.raywenderlich.android.cocktails.common.network.Cocktail
import com.raywenderlich.android.cocktails.common.repository.CocktailsRepository
import com.raywenderlich.android.cocktails.common.repository.RepositoryCallback
import com.raywenderlich.android.cocktails.game.model.Game
import com.raywenderlich.android.cocktails.game.model.Question
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CocktailsGameFactoryUnitTests {
    @Mock
    private lateinit var repository: CocktailsRepository
    private lateinit var factory: CocktailsGameFactory

    private val cocktails = listOf(
            Cocktail("1", "Drink1", "image1"),
            Cocktail("2", "Drink2", "image2"),
            Cocktail("3", "Drink3", "image3"),
            Cocktail("4", "Drink4", "image4")
    )

    @Before
    fun setup(){
        factory = CocktailsGameFactoryImpl(repository)
    }

    @Test
    fun buildGame_shouldGetCocktailsFromReps(){
        factory.buildGame(mock())
        verify(repository).getAlcoholic(any())
    }

    @Test
    fun buildGame_shouldCallOnSuccess(){
        val callback = mock<CocktailsGameFactory.CallBack>()
        setupRepositoryWithCocktails(repository)

        factory.buildGame(callback)

        verify(callback).onSuccess(any())
    }

    @Test
    fun buildGame_shouldCallOnError(){
        val callback = mock<CocktailsGameFactory.CallBack>()
        setupRepositoryWithError(repository)

        factory.buildGame(callback)

        verify(callback).onError()
    }

    @Test
    fun buildGame_shouldGetHighScoreFromRepo(){
        setupRepositoryWithCocktails(repository)

        factory.buildGame(mock())

        verify(repository).getHighScore()
    }

    @Test
    fun buildGame_shouldBuildGameWithHighScore(){
        setupRepositoryWithCocktails(repository)
        val highScore = 100
        whenever(repository.getHighScore()).thenReturn(highScore)

        factory.buildGame(object :CocktailsGameFactory.CallBack{
            override fun onSuccess(game: Game) {
                Assert.assertEquals(highScore,game.score.highest)
            }

            override fun onError() {
                Assert.fail()
            }

        })
    }

    @Test
    fun buildGame_shouldBuildGameWithQuestions(){
        setupRepositoryWithCocktails(repository)

        factory.buildGame(object :CocktailsGameFactory.CallBack{
            override fun onSuccess(game: Game) {
                cocktails.forEach{
                    assertQuestion(game.nextQuestion(),it.strDrink,it.strDrinkThumb)
                }
            }

            override fun onError() {
                Assert.fail()
            }

        })
    }

    private fun assertQuestion(question: Question?,correctAnswer: String ,imageUrl: String?){
      Assert.assertNotNull(question)
        Assert.assertEquals(imageUrl,question?.imageUrl)
        Assert.assertEquals(correctAnswer,question?.correctOption)
        Assert.assertNotEquals(correctAnswer,question?.inCorrectOption)
    }

    private fun setupRepositoryWithCocktails(repository: CocktailsRepository){
        doAnswer {
            val callback = it.getArgument(0) as RepositoryCallback<List<Cocktail>,String>
            callback.onSuccess(cocktails)
        }.whenever(repository).getAlcoholic(any())
    }

    private fun setupRepositoryWithError(repository: CocktailsRepository){
        doAnswer {
            val callback = it.getArgument(0)as RepositoryCallback<List<Cocktail>,String>
            callback.onError("Error")
        }.whenever(repository).getAlcoholic(any())
    }
}