package com.example.rmovies.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.rmovies.data.model.Movie
import com.example.rmovies.domain.usecase.SearchUseCase
import com.example.rmovies.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel constructor(private val searchUseCase: SearchUseCase) : BaseViewModel() {

    private val _onSuccessSearch: MutableLiveData<List<Movie>> =
        MutableLiveData()
    val onSuccessSearch: LiveData<List<Movie>>
        get() = _onSuccessSearch

    fun searchMovie(query: String) {
        viewModelScope.launch {
            callApi(
                suspend { searchUseCase.searchMovie(query) },
                onSuccess = {
                    val result = it as List<*>
                    _onSuccessSearch.postValue(
                        result.filterIsInstance<Movie>()
                    )
                }
            )
        }
    }
}