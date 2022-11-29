package com.demo.android.awarenessfood.repositories.models

sealed class FoodTriviaApiState {
  object Error : FoodTriviaApiState()
  data class Result(val trivia: String) : FoodTriviaApiState()
}