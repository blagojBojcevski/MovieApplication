package com.test.movieapplication.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    val id: Int,
    val original_title: String,
    val overview: String,
    val poster_path: String?,
    val original_language: String,
    val original_name: String,
    val vote_count: String?,
    val vote_average: Double?,
    @SerializedName("production_companies")
    val authors: List<ProductionCompany>
)