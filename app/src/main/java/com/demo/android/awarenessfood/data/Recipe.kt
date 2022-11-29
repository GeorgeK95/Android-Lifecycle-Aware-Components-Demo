package com.demo.android.awarenessfood.data

import com.google.gson.annotations.SerializedName

data class Recipe(
  @SerializedName("spoonacularScore")
  val score: Double,
  val pricePerServing: Double,
  val id: Int,
  val title: String,
  val readyInMinutes: Int,
  val servings: Int,
  val image: String,
  val summary: String,
  val instructions: String,
  @SerializedName("spoonacularSourceUrl")
  val url: String,
  @SerializedName("extendedIngredients")
  val ingredients: List<Ingredient>
)