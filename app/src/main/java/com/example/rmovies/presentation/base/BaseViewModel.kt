package com.example.rmovies.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rmovies.utils.ResponseApi
import com.example.rmovies.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {

    lateinit var uiState: MutableLiveData<UiState>

    suspend fun <T> T.callApi(
        call: suspend () -> ResponseApi,
        onSuccess: (Any?) -> Unit,
        onError: (() -> Unit?)? = null
    ) {
        uiState.postValue(UiState.Loading)

        when (val response = call.invoke()) {
            is ResponseApi.Success -> {
                uiState.postValue(UiState.Loading)
                onSuccess(response.data)
            }
            is ResponseApi.Error -> {
                uiState.postValue(UiState.Loading)
                onError?.let {
                    withContext(Dispatchers.Main) { onError.invoke() }
                } ?: run {
                    uiState.postValue(UiState.Error)
                }
            }
        }
    }
}