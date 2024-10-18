package com.test.movieapplication.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val name: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val original_language: String,
    val original_name: String
)
