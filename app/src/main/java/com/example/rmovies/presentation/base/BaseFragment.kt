package com.example.rmovies.presentation.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.rmovies.utils.UiState

abstract class BaseFragment: Fragment() {
    abstract var uiState: MutableLiveData<UiState>
}