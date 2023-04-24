package com.example.rmovies.data.api

import android.app.SearchManager
import com.example.rmovies.data.model.ListMovies
import com.example.rmovies.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBapi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): Response<ListMovies>

    @GET("search/movie")
    suspend fun searchMovie(
        @Query(SearchManager.QUERY) query: String
    ): Response<ListMovies>

    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int
    ): Response<Movie>
}