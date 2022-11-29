package com.demo.android.awarenessfood.repositories.models

import com.demo.android.awarenessfood.data.Recipe

sealed class RecipeApiState {
  object Error : RecipeApiState()
  data class Result(val recipe: Recipe) : RecipeApiState()
}