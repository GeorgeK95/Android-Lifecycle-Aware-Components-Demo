package com.demo.android.awarenessfood.viewmodels

sealed class UiLoadingState {
  object Loading : UiLoadingState()
  object NotLoading : UiLoadingState()
}