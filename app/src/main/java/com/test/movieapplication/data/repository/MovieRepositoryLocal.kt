package com.test.movieapplication.data.repository

import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import javax.inject.Inject

class MovieRepositoryLocal @Inject constructor(
    private val movieDao: MovieDao
) : MovieRepositoryLocalInterface{

    /**
     * Retrieves a list of popular movies from the local Room database.
     *
     * - Fetches all movies from the database and calculates the total number of pages
     *   based on the page size of 20 items per page.
     * - This method simulates pagination by slicing the movie list.
     * - The result is wrapped in a `MovieResponse` object to match the API structure.
     *
     * @param page The current page number to retrieve for pagination.
     * @return A `MovieResponse` containing a list of popular movies from the local database.
     */
    override suspend fun getPopularMoviesDb(page: Int): MovieResponse {
        val movies = movieDao.getAllMovies()
        val totalResults = movies.size
        val totalPages = if (totalResults > 0) (totalResults / 20) + 1 else 1
        return MovieResponse(
            results = movies,
            page = page,
            total_pages = totalPages
        )
    }

    /**
     * Searches for movies in the local Room database based on a query string.
     *
     * - The query searches movie titles in the database and returns a filtered list.
     * - Pagination is simulated by splitting the result into pages of 20 items.
     * - The method returns a `MovieResponse` object that mimics the API response structure.
     *
     * @param query The search string used to filter movies by title.
     * @param page The current page number to retrieve for pagination.
     * @return A `MovieResponse` containing a list of movies that match the query from the local database.
     */
    override suspend fun searchMoviesDb(query: String, page: Int): MovieResponse {
        val movies = movieDao.searchMovie(query)
        val totalResults = movies.size
        val totalPages = if (totalResults > 0) (totalResults / 20) + 1 else 1
        return MovieResponse(
            results = movies,
            page = page,
            total_pages = totalPages
        )
    }

    /**
     * Retrieves detailed information about a movie by its unique ID from the local Room database.
     *
     * - Fetches a movie from the database using the movie's unique ID.
     * - Returns a `MovieDetail` object containing detailed information about the movie.
     * - The method converts the stored movie data to match the structure expected by the application.
     *
     * @param movieId The unique identifier of the movie to retrieve.
     * @return A `MovieDetail` object containing comprehensive information about the specified movie.
     */
    override suspend fun getMovieByIdDb(movieId: Int): MovieDetail {
        val movie = movieDao.getMovie(movieId.toLong())
        return MovieDetail(
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

    /**
     * Saves a list of movies to the local Room database.
     *
     * - Inserts a list of movie objects into the database.
     * - This method is typically used to cache movies that were fetched from the API.
     *
     * @param movies A list of `Movie` objects to be saved in the database.
     */
    override suspend fun saveMoviesToDb(movies: List<Movie>) {
        movieDao.insertMovieList(movies)
    }

}