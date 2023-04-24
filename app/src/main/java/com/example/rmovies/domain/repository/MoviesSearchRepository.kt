package com.example.rmovies.domain.repository

import com.example.rmovies.data.retrofit.ApiService
import com.example.rmovies.presentation.base.BaseRepository
import com.example.rmovies.utils.ResponseApi

class MoviesSearchRepository : BaseRepository() {
    suspend fun searchMovie(query: String): ResponseApi {
        return safeApiCall {
            ApiService.tmdbApi.searchMovie(query)
        }
    }
}