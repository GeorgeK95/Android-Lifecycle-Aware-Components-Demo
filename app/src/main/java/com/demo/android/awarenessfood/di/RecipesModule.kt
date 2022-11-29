package com.demo.android.awarenessfood.di

import com.demo.android.awarenessfood.network.RecipesService
import com.demo.android.awarenessfood.repositories.FoodTriviaRepository
import com.demo.android.awarenessfood.repositories.FoodTriviaRepositoryImpl
import com.demo.android.awarenessfood.repositories.RecipeRepository
import com.demo.android.awarenessfood.repositories.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val API_KEY = "a416d41c8c6440e58db059d7e7f8d716"

@Module
@InstallIn(SingletonComponent::class)
object RecipesModule {

  @Provides
  @Singleton
  fun providesRetrofitService(): RecipesService {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
          val url = chain.request().url().newBuilder()
              .addQueryParameter("apiKey", API_KEY)
              .build()

          val requestBuilder = chain.request().newBuilder().url(url)
          chain.proceed(requestBuilder.build())
        }
        .build()

    val builder = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())

    return builder.build().create(RecipesService::class.java)
  }

  @Provides
  @Singleton
  fun providesRecipeRepository(recipesService: RecipesService): RecipeRepository {
      return RecipeRepositoryImpl(recipesService)
  }

  @Provides
  @Singleton
  fun providesTriviaRepository(recipesService: RecipesService): FoodTriviaRepository {
      return FoodTriviaRepositoryImpl(recipesService)
  }
}