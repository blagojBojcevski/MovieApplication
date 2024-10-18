package com.test.movieapplication.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.paging.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiService: MovieApiService
) {
    fun loadMoviesWithPaging(apiKey: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(apiService, apiKey) }
        ).flow
    }

}