package com.example.app_movie_gs.model

data class MovieResponse (
    val total_pages: Int?,
    val results: List<MovieModel>?
)