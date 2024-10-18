package com.test.movieapplication.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.viewmodel.MovieViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.paging.LoadState

@Composable
fun MovieListScreen(
    viewModel: MovieViewModel,
    apiKey: String) {

    var searchQuery by remember { mutableStateOf("") }

    val movies: LazyPagingItems<Movie> =
        viewModel.getMovies(apiKey = apiKey, query = searchQuery)
            .collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { query -> searchQuery = query },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        LazyColumn {
            items(count = movies.itemCount) { index ->
                val item = movies[index]
                item?.let {
                    MovieItem(movie = it)
                }
            }

            movies.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }
                }
            }
        }
    }
}