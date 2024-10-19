package com.test.movieapplication.data.model

data class MovieDetail(
    val id: Int,
    val original_title: String,
    val overview: String,
    val poster_path: String?,
    val original_language: String,
    val original_name: String,
    val vote_count: String?,
    val vote_average: Double?,
    val authors: List<ProductionCompany>
)