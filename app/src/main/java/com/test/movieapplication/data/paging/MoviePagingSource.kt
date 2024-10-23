package com.test.movieapplication.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.domain.MovieInteractor

/**
 * A PagingSource that loads movie data from a MovieRepository.
 * It supports both fetching popular movies and searching movies based on a query.
 * This class is responsible for loading paginated data using Paging 3.
 *
 * @param repository The MovieRepository to fetch movie data from.
 * @param apiKey The API key required to authenticate with the movie API.
 * @param query The search query; if empty, popular movies will be fetched.
 */
class MoviesPagingSource(
    private val repository: MovieInteractor,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Movie>() {

    /**
     * Loads a page of movie data.
     * This function is called asynchronously by the Paging library to fetch data.
     *
     * @param params The LoadParams containing information like page size and current key.
     * @return LoadResult containing the list of movies and keys for pagination.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val currentPage = params.key ?: 1 // Default to the first page if key is null

        return try {
            // Fetch movies from the repository based on whether the query is empty or not
            val response = if (query.isEmpty()) {
                repository.getPopularMovies(apiKey, currentPage)
            } else {
                repository.searchMovies(apiKey, query, currentPage)
            }

            // Log a warning if the API returns an empty result
            if (response.results.isNullOrEmpty()) {
                Log.w("MoviesPagingSource", "No movies returned, check API query or key")
            }

            // Calculate the previous and next keys for pagination
            val prevKey = if (currentPage > 1) currentPage - 1 else null
            val nextKey = if (currentPage < response.total_pages) currentPage + 1 else null

            // Return the page of movies along with the pagination keys
            LoadResult.Page(
                data = response.results.orEmpty(), // Safeguard against null data
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            // If an error occurs, return a LoadResult.Error to signal failure
            LoadResult.Error(e)
        }
    }

    /**
     * Determines the key for the page to be loaded when a refresh is triggered.
     *
     * @param state The PagingState containing the last accessed positions.
     * @return The key to use for the refresh, or null if not applicable.
     */
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        // Use the closest anchor position to determine the page to refresh
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
