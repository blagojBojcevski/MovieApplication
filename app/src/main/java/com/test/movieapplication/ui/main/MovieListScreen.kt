package com.test.movieapplication.ui.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.viewmodel.MovieViewModel

@Composable
fun MovieListScreen(
    viewModel: MovieViewModel,
    apiKey: String) {

    val movies: LazyPagingItems<Movie> =
        viewModel.getMovies(apiKey = apiKey)
            .collectAsLazyPagingItems()

    LazyColumn {
        items(count = movies.itemCount) { index ->
            val item = movies[index]
            item?.let {
                MovieItem(movie = it)
            }
        }
    }
}