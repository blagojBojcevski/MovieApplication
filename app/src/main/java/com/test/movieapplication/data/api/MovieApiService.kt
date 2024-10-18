package com.test.movieapplication.data.api

import com.test.movieapplication.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("trending/all/day")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): MovieResponse

    @GET("search/collection")
    suspend fun searchMovieCollection(
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): MovieResponse
}