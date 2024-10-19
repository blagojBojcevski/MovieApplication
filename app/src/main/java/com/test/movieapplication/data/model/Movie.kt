package com.test.movieapplication.data.model

data class Movie(
    val id: Int,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val original_language: String,
    val original_name: String
)
