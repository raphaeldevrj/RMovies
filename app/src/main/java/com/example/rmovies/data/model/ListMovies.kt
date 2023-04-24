package com.example.rmovies.data.model

data class ListMovies (
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)