package com.test.movieapplication.data.repository

import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import javax.inject.Inject

class MovieRepositoryAPI @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao
) : MovieRepositoryAPIInterface {

    private val defaultLanguage = "en-US"

    /**
     * Fetches a list of popular movies from the API and caches the results in the local Room database.
     *
     * - This function retrieves data directly from the API using the specified API key and page number.
     * - Results are paginated, and you can specify the page number to fetch different sets of results.
     * - The API key is required for authentication to access the movie data.
     *
     * @param apiKey The API key required to make the API request.
     * @param page The page number to retrieve for paginated results.
     * @return A `MovieResponse` containing a list of popular movies.
     */
    override suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse {
        val response = apiService.getPopularMovies(defaultLanguage, apiKey, page)
        movieDao.insertMovieList(response.results)
       return response
    }

    /**
     * Searches for movies based on a query string.
     *
     * - This function queries the API with the provided search term and returns matching results.
     * - The API key is required for authentication, and the query allows you to filter movies by title or content.
     * - Results are paginated, so you can specify the page number to retrieve a specific set of results.
     *
     * @param apiKey The API key required for authentication.
     * @param query The search query for finding movies.
     * @param page The page number for paginated results.
     * @return A `MovieResponse` containing the list of movies matching the search query.
     */
    override suspend fun searchMovies(apiKey: String, query: String, page: Int): MovieResponse {
        val response = apiService.searchMovieCollection(query, defaultLanguage, apiKey, page)
        movieDao.insertMovieList(response.results)
        return response
    }

    /**
     * Retrieves detailed information about a movie by its ID.
     * - This function retrieves detailed movie information by making a request to the API using the movie ID.
     * - The API key is required to authenticate the request.
     * - The method returns comprehensive details about a specific movie, including metadata, overview, and more.
     *
     * @param movieId The ID of the movie to retrieve.
     * @param apiKey The API key required for authentication.
     * @return A `MovieDetail` object containing detailed information about the movie.
     */
    override suspend fun getMovieById(movieId: Int, apiKey: String): MovieDetail {
        return apiService.getMovieById(movieId,defaultLanguage, apiKey)
    }
}
