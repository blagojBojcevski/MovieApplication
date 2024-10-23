package com.test.movieapplication.data.repository

import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse

interface MovieRepositoryLocalInterface {
    suspend fun getPopularMoviesDb(page: Int): MovieResponse

    suspend fun searchMoviesDb(query: String, page: Int): MovieResponse

    suspend fun getMovieByIdDb(movieId: Int): MovieDetail

    suspend fun saveMoviesToDb(movies: List<Movie>)
}