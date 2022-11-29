package com.demo.android.awarenessfood.network

import retrofit2.Response
import retrofit2.http.GET

interface RecipesService {

  @GET("recipes/random?number=1")
  suspend fun getRandomRecipe(): Response<RecipeResponse>

  @GET("food/trivia/random")
  suspend fun getFoodTrivia(): Response<TriviaResponse>
}