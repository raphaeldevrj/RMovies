package com.example.rmovies.domain.usecase

import com.example.rmovies.data.model.ListMovies
import com.example.rmovies.domain.repository.MoviesSearchRepository
import com.example.rmovies.utils.ResponseApi
import com.example.rmovies.utils.getFullImageUrl

class SearchUseCase
constructor(
    private val repository : MoviesSearchRepository
){
    suspend fun searchMovie(query: String): ResponseApi {
        return when (val responseApi = repository.searchMovie(query)) {
            is ResponseApi.Success -> {
                val data = responseApi.data as? ListMovies
                val result = data?.results?.map {
                    it.backdrop_path = it.backdrop_path.getFullImageUrl()
                    it.poster_path = it.poster_path.getFullImageUrl()
                    it
                }
                ResponseApi.Success(result)
            }
            is ResponseApi.Error -> {
                responseApi
            }
        }
    }
}