package com.test.movieapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.paging.MoviesPagingSource
import com.test.movieapplication.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieDetailStateFlow = MutableStateFlow<MovieDetail?>(null)
    val movieDetailStateFlow: StateFlow<MovieDetail?> = _movieDetailStateFlow

    private val currentQuery = MutableStateFlow("")

    val moviesFlow = currentQuery.flatMapLatest { query ->
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MoviesPagingSource(repository, apiKey = "34190fac5efa997a8fe0dd8d51356032", query = query)
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun setQuery(query: String) {
        currentQuery.value = query
    }

    fun fetchMovieById(movieId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                val movieDetail = repository.getMovieById(movieId, apiKey)
                _movieDetailStateFlow.value = movieDetail
            } catch (e: Exception) {
            }
        }
    }
}