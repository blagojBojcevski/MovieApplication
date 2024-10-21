package com.test.movieapplication.data.repository

import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse

interface MovieRepositoryInterface {
    suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse

    suspend fun searchMovies(apiKey: String, query: String, page: Int): MovieResponse

    suspend fun getMovieById(movieId: Int, apiKey: String): MovieDetail
}
