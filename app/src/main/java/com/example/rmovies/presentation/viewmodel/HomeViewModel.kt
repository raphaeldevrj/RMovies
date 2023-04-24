package com.example.rmovies.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rmovies.MoviePagingSource
import com.example.rmovies.data.model.Movie
import com.example.rmovies.domain.repository.MoviesHomeRepository
import com.example.rmovies.domain.usecase.HomeUseCase
import com.example.rmovies.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val homeRepository: MoviesHomeRepository,
    private val homeUseCase: HomeUseCase
) : BaseViewModel() {

    private var mPagingData : Flow<PagingData<Movie>>? = null;

    fun getList(): Flow<PagingData<Movie>> {
        if(mPagingData != null) return mPagingData as Flow<PagingData<Movie>>
        else
            mPagingData = Pager(config = PagingConfig(pageSize = 20),
                pagingSourceFactory = { MoviePagingSource(homeRepository, homeUseCase) }).flow.cachedIn(
                viewModelScope)
        return mPagingData as Flow<PagingData<Movie>>
    }
}