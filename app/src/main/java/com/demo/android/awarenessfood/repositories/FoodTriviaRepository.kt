package com.demo.android.awarenessfood.repositories

import com.demo.android.awarenessfood.repositories.models.FoodTriviaApiState
import kotlinx.coroutines.flow.Flow

interface FoodTriviaRepository {

  fun getRandomFoodTrivia(): Flow<FoodTriviaApiState>
}