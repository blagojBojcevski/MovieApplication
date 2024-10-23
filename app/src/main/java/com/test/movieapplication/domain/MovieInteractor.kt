package com.test.movieapplication.domain

import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import com.test.movieapplication.data.repository.MovieRepositoryAPIInterface
import com.test.movieapplication.data.repository.MovieRepositoryLocalInterface
import com.test.movieapplication.utils.fetchWithFallback
import javax.inject.Inject

class MovieInteractor @Inject constructor(
    private val apiRepository: MovieRepositoryAPIInterface,
    private val localRepository: MovieRepositoryLocalInterface
) {

    /**
     * Fetches movie details by ID, prioritizing the API. If a network issue occurs,
     * the function retrieves the movie details from the local database.
     *
     * - movieId: The ID of the movie to fetch.
     * - apiKey: The API key required for the network request.
     * - Falls back to local repository if there's an IOException.
     */
    suspend fun getMovie(movieId: Int, apiKey: String): MovieDetail {
        return fetchWithFallback(
            apiCall = { apiRepository.getMovieById(movieId, apiKey) },
            localCall = { localRepository.getMovieByIdDb(movieId) }
        )
    }

    /**
    * Fetches a list of popular movies, prioritizing the API. If the network request fails,
    * it retrieves the popular movies from the local database.
    *
    * - apiKey: The API key required for the network request.
    * - page: The page number for pagination of the popular movies.
    * - Falls back to local repository in case of a network error.
    */
    suspend fun getPopularMovies(apiKey: String, page: Int): MovieResponse {
        return fetchWithFallback(
            apiCall = { apiRepository.getPopularMovies(apiKey, page) },
            localCall = { localRepository.getPopularMoviesDb(page) }
        )
    }

    /**
     * Searches for movies based on a query, prioritizing the API. If the network request fails,
     * it performs the search in the local database.
     *
     * - apiKey: The API key required for the network request.
     * - query: The search term used to find matching movies.
     * - page: The page number for pagination of the search results.
     * - Falls back to local repository in case of a network error.
     */
    suspend fun searchMovies(apiKey: String, query: String, page: Int): MovieResponse {
        return fetchWithFallback(
            apiCall = { apiRepository.searchMovies(apiKey, query, page) },
            localCall = { localRepository.searchMoviesDb(query, page) }
        )
    }
}
