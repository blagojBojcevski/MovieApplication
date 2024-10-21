package com.test.movieapplication.data.repository

import android.content.Context
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import com.test.movieapplication.utils.isOnline
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService,
    private val movieDao: MovieDao,
    @ApplicationContext private val context: Context
) : MovieRepositoryInterface {

    private val defaultLanguage = "en-US"

    /**
     * Checks if the device is online or offline.
     *
     * @return `true` if the device is connected to the internet, `false` otherwise.
     */
    private fun isOnline(): Boolean {
        return context.isOnline()
    }

    /**
     * Retrieves a list of popular movies either from the API (if online) or from the local database (if offline).
     * The results are cached in the Room database if fetched from the network.
     *
     * @param apiKey The API key required for authentication.
     * @param page The page number for paginated results.
     * @return A `MovieResponse` containing the list of popular movies.
     */
    override suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse {
        return if (isOnline()) {
            val response = apiService.getPopularMovies(defaultLanguage, apiKey, page)
            movieDao.insertMovieList(response.results)
            response
        } else {
            val movies = movieDao.getAllMovies()
            val totalResults = movies.size
            val totalPages = if (totalResults > 0) (totalResults / 20) + 1 else 1
            MovieResponse(
                results = movies,
                page = page,
                total_pages = totalPages
            )
        }
    }

    /**
     * Searches for movies based on a query string.
     * If the device is online, results are fetched from the API. Otherwise, it retrieves results from the local Room database.
     *
     * @param apiKey The API key required for authentication.
     * @param query The search query for finding movies.
     * @param page The page number for paginated results.
     * @return A `MovieResponse` containing the list of movies matching the search query.
     */
    override suspend fun searchMovies(apiKey: String, query: String, page: Int): MovieResponse {
        return if (isOnline()) {
            val response = apiService.searchMovieCollection(query, defaultLanguage, apiKey, page)
            movieDao.insertMovieList(response.results)
            response
        } else {
            val movies = movieDao.searchMovie(query)
            val totalResults = movies.size
            val totalPages = if (totalResults > 0) (totalResults / 20) + 1 else 1
            MovieResponse(
                results = movies,
                page = page,
                total_pages = totalPages
            )
        }
    }

    /**
     * Retrieves detailed information about a movie by its ID.
     * The movie details are fetched from the API when online or from the local database when offline.
     *
     * @param movieId The ID of the movie to retrieve.
     * @param apiKey The API key required for authentication.
     * @return A `MovieDetail` object containing detailed information about the movie.
     */
    override suspend fun getMovieById(movieId: Int, apiKey: String): MovieDetail {
        return if (isOnline()) {
            apiService.getMovieById(movieId,defaultLanguage, apiKey)
        } else {
           val movie = movieDao.getMovie(movieId.toLong())
            MovieDetail(
                id = movie.id,
                original_title = movie.original_title!!,
                overview = movie.overview!!,
                poster_path = movie.poster_path,
                original_language = movie.original_language,
                original_name = "",
                vote_count = movie.vote_count,
                vote_average = movie.vote_average,
                authors= listOf()
            )
        }
    }
}
