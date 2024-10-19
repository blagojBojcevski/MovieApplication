package com.test.movieapplication.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.repository.MovieRepository

class MoviesPagingSource(
    private val repository: MovieRepository,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1

            val response = if (query.isEmpty()) {
                repository.getPopularMovies(apiKey, currentPage)
            } else {
                repository.searchMovies(apiKey, query, currentPage)
            }
            if (response.results.isNullOrEmpty()) {
                Log.w("MoviesPagingSource", "No movies returned, check API query or key")
            }
            val totalPages = response.total_pages
            val prevKey = if (currentPage > 1) currentPage - 1 else null
            val nextKey = if (currentPage < totalPages) currentPage + 1 else null

            LoadResult.Page(
                data = response.results,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}