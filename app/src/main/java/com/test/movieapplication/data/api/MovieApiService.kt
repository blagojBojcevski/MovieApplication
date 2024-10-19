package com.test.movieapplication.data.api

import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("trending/movie/week")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("search/movie")
    suspend fun searchMovieCollection(
        @Query("query") query: String,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieById(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US",
        @Query("api_key") apiKey: String
    ): MovieDetail
}