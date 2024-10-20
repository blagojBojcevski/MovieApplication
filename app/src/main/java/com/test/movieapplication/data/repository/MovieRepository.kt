package com.test.movieapplication.data.repository

import android.util.Log
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService
) {

    suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse {
        val response = apiService.getPopularMovies("en-US",apiKey, page)
        return response
    }

    suspend fun searchMovies(apiKey: String, query: String, page: Int): MovieResponse {
        val response = apiService.searchMovieCollection(query,"en-US",apiKey, page)
        return response
    }

    suspend fun getMovieById(movieId: Int, apiKey: String): MovieDetail {
        return apiService.getMovieById(
            movieId = movieId, apiKey = apiKey
        )
    }
}