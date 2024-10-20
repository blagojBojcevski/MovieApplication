package com.test.movieapplication.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.movieapplication.viewmodel.MovieViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController

@Composable
fun MovieListScreen(viewModel: MovieViewModel, navController: NavController) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Update search query in the ViewModel whenever it changes
    LaunchedEffect(searchQuery) {
        viewModel.setQuery(searchQuery)
    }

    // Collect the paginated movie list items
    val movies = viewModel.moviesFlow.collectAsLazyPagingItems()

    // Save the list state to preserve scroll position on recomposition
    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search TextField for movie queries
        TextField(
            value = searchQuery,
            onValueChange = { newQuery -> searchQuery = newQuery },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(count = movies.itemCount) { index ->
                val item = movies[index]
                item?.let {
                    if (item.poster_path != null && item.overview != null) {
                        MovieItem(
                            movie = it,
                            onMovieClick = {
                                navController.navigate("movieDetail/${item.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}