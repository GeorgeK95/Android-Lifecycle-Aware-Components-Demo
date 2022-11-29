package com.demo.android.awarenessfood.repositories

import com.demo.android.awarenessfood.repositories.models.RecipeApiState
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

  fun getRandomRecipe(): Flow<RecipeApiState>
}