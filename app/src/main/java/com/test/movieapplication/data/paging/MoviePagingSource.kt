package com.test.movieapplication.data.paging

import retrofit2.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.model.Movie
import java.io.IOException

class MoviesPagingSource(
    private val apiService: MovieApiService,
    private val apiKey: String,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val response = if (query.isEmpty()) {
                apiService.getPopularMovies(apiKey = apiKey, page = currentPage)
            } else {
                apiService.searchMovieCollection(apiKey = apiKey, query = query, page = currentPage)
            }
            val totalPages = response.total_pages
            val prevKey = if (currentPage > 1) currentPage - 1 else null
            val nextKey = if (currentPage < totalPages) currentPage + 1 else null

            LoadResult.Page(
                data = response.results,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }
}