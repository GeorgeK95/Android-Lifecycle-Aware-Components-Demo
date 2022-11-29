package com.demo.android.awarenessfood.repositories

import com.demo.android.awarenessfood.network.RecipesService
import com.demo.android.awarenessfood.network.TriviaResponse
import com.demo.android.awarenessfood.repositories.models.FoodTriviaApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class FoodTriviaRepositoryImpl @Inject constructor(
    private val service: RecipesService
) : FoodTriviaRepository {

  override fun getRandomFoodTrivia(): Flow<FoodTriviaApiState> = flow {
    val response = service.getFoodTrivia()

    when {
      isResponseSuccess(response) -> emit(FoodTriviaApiState.Result(response.body()!!.text))
      else -> emit(FoodTriviaApiState.Error)
    }
  }.catch {
    emit(FoodTriviaApiState.Error)
  }.flowOn(Dispatchers.IO)

  private fun isResponseSuccess(response: Response<TriviaResponse>) =
      response.isSuccessful && response.body() != null && response.body()!!.text.isNotEmpty()
}