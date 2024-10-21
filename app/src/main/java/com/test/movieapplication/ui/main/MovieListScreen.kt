package com.test.movieapplication.ui.main

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.test.movieapplication.viewmodel.MovieViewModel
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.test.movieapplication.utils.isOnline

@Composable
fun MovieListScreen(viewModel: MovieViewModel, navController: NavController, context: Context) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        viewModel.setQuery(searchQuery)
    }

    val movies = viewModel.moviesFlow.collectAsLazyPagingItems()
    val listState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        if (!context.isOnline()) {
            Text(
                text = "You are offline. Showing cached data.",
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        TextField(
            value = searchQuery,
            onValueChange = { newQuery -> searchQuery = newQuery },
            label = { Text("Search Movies") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        SwipeRefresh(
            state = SwipeRefreshState(isRefreshing),
            onRefresh = {
                isRefreshing = true
                viewModel.setQuery(searchQuery)
                movies.refresh()
                isRefreshing = false
            }
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = listState,
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(movies.itemCount) { index ->
                    val item = movies[index]
                    item?.let {
                        if (item.poster_path != null && item.overview != null) {
                            MovieItem(
                                movie = it,
                                onMovieClick = {
                                    navController.navigate("movieDetail/${item.id}")
                                },
                                isOnline = context.isOnline()
                            )
                        }
                    }
                }
            }
        }
    }
}
