package com.example.rmovies.domain.usecase

import com.example.rmovies.data.model.ListMovies
import com.example.rmovies.data.model.Movie
import com.example.rmovies.domain.repository.MoviesHomeRepository
import com.example.rmovies.utils.Constants.FIRST_PAGE
import com.example.rmovies.utils.ResponseApi
import com.example.rmovies.utils.dateFormat
import com.example.rmovies.utils.getFullImageUrl

class HomeUseCase
constructor(
    private val repository : MoviesHomeRepository
) {
    suspend fun getPopularMovies(): ResponseApi {
        return when (val responseApi = repository.getPopularMovies(FIRST_PAGE)) {
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

    fun setupMoviesList(list: ListMovies?): List<Movie> {
        val movies = list?.results
        movies?.forEach { movie ->
            movie.poster_path = movie.poster_path?.getFullImageUrl()
            movie.backdrop_path = movie.backdrop_path?.getFullImageUrl()
            movie.release_date = movie.release_date?.dateFormat()
        }
        return movies ?: listOf()
    }

}