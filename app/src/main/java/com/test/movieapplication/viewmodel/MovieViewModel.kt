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

    private val apiKey = BuildConfig.API_KEY

    // Backing StateFlow for the movie detail, mutable only within the ViewModel
    private val _movieDetailStateFlow = MutableStateFlow<MovieDetail?>(null)

    // Exposed immutable StateFlow for observing movie details in the UI
    val movieDetailStateFlow: StateFlow<MovieDetail?> = _movieDetailStateFlow

    // Holds the current search query for movies
    private val currentQuery = MutableStateFlow("")

    /**
     * Flow of movies based on the current query. It triggers a new search whenever
     * the query changes. The data is paginated using Paging 3 library.
     *
     * - flatMapLatest: Ensures that only the latest query result is collected.
     * - Pager: Creates the paginated data source.
     * - cachedIn: Caches the flow within the viewModelScope to ensure data survives configuration changes.
     */
    val moviesFlow = currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20), // Configures pagination with a page size of 20 items
            pagingSourceFactory = {
                MoviesPagingSource(repository, apiKey = apiKey, query = query) // Fetches data from the PagingSource
            }
        ).flow.cachedIn(viewModelScope) // Cache the result in the ViewModel's coroutine scope
    }

    /**
     * Sets a new search query for fetching movies. This updates the `currentQuery` which in turn triggers
     * a new paginated search in `moviesFlow`.
     *
     * @param query The search query entered by the user.
     */
    fun setQuery(query: String) {
        currentQuery.value = query // Updates the query, causing moviesFlow to refresh
    }

    /**
     * Fetches detailed information about a specific movie by its ID.
     * The result is posted to the `_movieDetailStateFlow` which can be observed by the UI.
     *
     * @param movieId The ID of the movie to fetch details for.
     */
    fun fetchMovieById(movieId: Int) {
        viewModelScope.launch {
            try {
                // Calls the repository to fetch movie details by ID
                val movieDetail = repository.getMovieById(movieId, apiKey)
                // Updates the movie detail state flow with the fetched data
                _movieDetailStateFlow.value = movieDetail
            } catch (e: Exception) {
                // Logs the error to the console for debugging purposes
                e.printStackTrace() // Use a logger in production instead of printStackTrace
            }
        }
    }
}
