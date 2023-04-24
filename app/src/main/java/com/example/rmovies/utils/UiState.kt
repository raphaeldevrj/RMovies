package com.example.rmovies.utils

sealed class UiState {
    object Loading : UiState()
    object Error : UiState()
}