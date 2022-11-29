package com.demo.android.awarenessfood.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.android.awarenessfood.repositories.FoodTriviaRepository
import com.demo.android.awarenessfood.repositories.models.FoodTriviaApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodTriviaViewModel @Inject constructor(
    private val foodTriviaRepository: FoodTriviaRepository
) : ViewModel() {

  private val _foodTriviaState = MutableLiveData<FoodTriviaApiState>()
  val foodTriviaState: LiveData<FoodTriviaApiState>
    get() {
      return _foodTriviaState
    }

  fun getRandomFoodTrivia() {
    viewModelScope.launch {
      foodTriviaRepository.getRandomFoodTrivia().collect {
        _foodTriviaState.value = it
      }
    }
  }
}