package com.test.movieapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.test.movieapplication.BuildConfig
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.paging.MoviesPagingSource
import com.test.movieapplication.data.repository.MovieRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepositoryInterface
) : ViewModel() {

    // API key for authentication with the movie API
    private val apiKey = BuildConfig.API_KEY

    // StateFlow that holds the detailed information of a selected movie
    private val _movieDetailStateFlow = MutableStateFlow<MovieDetail?>(null)

    // Publicly exposed StateFlow for observing movie detail updates
    val movieDetailStateFlow: StateFlow<MovieDetail?> = _movieDetailStateFlow

    // StateFlow that holds the current search query for filtering movies
    private val currentQuery = MutableStateFlow("")

    /**
    * Flow of movies based on the current query. It triggers a new search whenever
    * the query changes.
    *
    * - flatMapLatest: Ensures that only the latest query result is collected.
    * - Pager: Creates the paginated data source.
    * - cachedIn: Caches the flow within the viewModelScope to ensure data survives configuration changes.
    */
    val moviesFlow = currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MoviesPagingSource(repository, apiKey = apiKey, query = query)
            }
        ).flow.cachedIn(viewModelScope)
    }

    /**
     * Updates the search query used to fetch movies.
     *
     * @param query The new search query string.
     */
    fun setQuery(query: String) {
        currentQuery.value = query
    }


    /**
     * Fetches the details of a movie by its ID.
     * The result is posted to the `_movieDetailStateFlow` for UI observation.
     *
     * @param movieId The ID of the movie to fetch details for.
     */
    fun fetchMovieById(movieId: Int) {
        viewModelScope.launch {
            try {
                val movieDetail = repository.getMovieById(movieId, apiKey)
                _movieDetailStateFlow.value = movieDetail
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
