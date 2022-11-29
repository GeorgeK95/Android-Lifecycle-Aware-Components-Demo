package com.demo.android.awarenessfood.repositories

import com.demo.android.awarenessfood.network.RecipeResponse
import com.demo.android.awarenessfood.network.RecipesService
import com.demo.android.awarenessfood.repositories.models.RecipeApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val service: RecipesService
) : RecipeRepository {

  override fun getRandomRecipe(): Flow<RecipeApiState> = flow {
    val response = service.getRandomRecipe()

    when {
      isResponseSuccess(response) -> emit(RecipeApiState.Result(response.body()!!.recipes[0]))
      else -> emit(RecipeApiState.Error)
    }
  }.catch {
    emit(RecipeApiState.Error)
  }.flowOn(Dispatchers.IO)

  private fun isResponseSuccess(response: Response<RecipeResponse>) =
      response.isSuccessful && response.body() != null && response.body()!!.recipes.isNotEmpty()
}