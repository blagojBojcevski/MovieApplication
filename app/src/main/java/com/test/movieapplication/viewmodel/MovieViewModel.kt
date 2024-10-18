package com.test.movieapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun getMovies(apiKey: String): Flow<PagingData<Movie>> {
        return repository.loadMoviesWithPaging(apiKey).cachedIn(viewModelScope)
    }
}