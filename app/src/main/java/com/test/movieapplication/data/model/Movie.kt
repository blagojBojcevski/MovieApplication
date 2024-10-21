package com.test.movieapplication.data.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
@Immutable
data class Movie(
    @PrimaryKey val id: Long,
    val original_title: String?,
    val overview: String?,
    val poster_path: String?,
    val backdrop_path: String?,
    val original_language: String,
    val vote_count: String?,
    val vote_average: Double?
): Serializable
