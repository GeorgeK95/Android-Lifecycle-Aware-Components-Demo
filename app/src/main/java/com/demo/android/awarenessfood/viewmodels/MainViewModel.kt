package com.demo.android.awarenessfood.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.android.awarenessfood.repositories.RecipeRepository
import com.demo.android.awarenessfood.repositories.models.RecipeApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

  private val _loadingState = MutableLiveData<UiLoadingState>()
  val loadingState: LiveData<UiLoadingState>
    get() {
      return _loadingState
    }

  private val _recipeState = MutableLiveData<RecipeApiState>()
  val recipeState: LiveData<RecipeApiState>
    get() {
      return _recipeState
    }

  fun getRandomRecipe() {

    _loadingState.value = UiLoadingState.Loading

    viewModelScope.launch {
      recipeRepository.getRandomRecipe().collect { result ->
        _loadingState.value = UiLoadingState.NotLoading
        _recipeState.value = result
      }
    }
  }
}