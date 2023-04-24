package com.example.rmovies.domain.usecase

import com.example.rmovies.data.model.Movie
import com.example.rmovies.domain.repository.MoviesDetailsRepository
import com.example.rmovies.utils.ResponseApi
import com.example.rmovies.utils.getFullImageUrl

class DetailsUseCase
constructor(
    private val repository : MoviesDetailsRepository
){
    suspend fun getMovieById(movieId: Int): ResponseApi {
        return when(val responseApi = repository.getMovieById(movieId)) {
            is ResponseApi.Success -> {
                val movie = responseApi.data as? Movie
                movie?.backdrop_path = movie?.backdrop_path?.getFullImageUrl().toString()
                ResponseApi.Success(movie)
            }
            is ResponseApi.Error -> {
                responseApi
            }
        }
    }

}