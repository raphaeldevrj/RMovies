package com.example.rmovies.domain.repository

import com.example.rmovies.data.retrofit.ApiService
import com.example.rmovies.presentation.base.BaseRepository
import com.example.rmovies.utils.ResponseApi

class MoviesDetailsRepository : BaseRepository() {
    suspend fun getMovieById(id: Int): ResponseApi {
        return safeApiCall {
            ApiService.tmdbApi.getMovieById(id)
        }
    }
}