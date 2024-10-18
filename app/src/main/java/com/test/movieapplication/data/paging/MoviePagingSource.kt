package com.test.movieapplication.data.paging

import retrofit2.HttpException
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.model.Movie
import java.io.IOException

class MoviesPagingSource(
    private val apiService: MovieApiService,
    private val apiKey: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getPopularMovies(apiKey = apiKey)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.results.isEmpty()) null else page + 1
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