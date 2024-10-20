package com.test.movieapplication.data.repository

import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService
): MovieRepositoryInterface {
    private val defaultLanguage = "en-US"

    /**
     * Fetches a list of popular movies from the API.
     *
     * @param apiKey The API key required for authentication.
     * @param page The page number for paginated results.
     * @return A `MovieResponse` containing the list of popular movies.
     */
    override suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse =
        apiService.getPopularMovies(defaultLanguage, apiKey, page)

    /**
     * Searches for movies based on a query string.
     *
     * @param apiKey The API key required for authentication.
     * @param query The search query for finding movies.
     * @param page The page number for paginated results.
     * @return A `MovieResponse` containing the list of movies matching the search query.
     */
    override suspend fun searchMovies(apiKey: String, query: String, page: Int): MovieResponse =
        apiService.searchMovieCollection(query, defaultLanguage, apiKey, page)

    /**
     * Fetches detailed information about a specific movie by its ID.
     *
     * @param movieId The unique ID of the movie.
     * @param apiKey The API key required for authentication.
     * @return A `MovieDetail` object containing detailed information about the movie.
     */
    override suspend fun getMovieById(movieId: Int, apiKey: String): MovieDetail =
        apiService.getMovieById(movieId = movieId, apiKey = apiKey)
}
