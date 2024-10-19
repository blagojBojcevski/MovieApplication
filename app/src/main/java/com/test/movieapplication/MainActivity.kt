package com.test.movieapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.movieapplication.ui.main.MovieDetailScreen
import com.test.movieapplication.ui.main.MovieListScreen
import com.test.movieapplication.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MovieAppNavigation(viewModel = movieViewModel)
        }
    }
}

@Composable
fun MovieAppNavigation(viewModel: MovieViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "movie_list") {

        composable("movie_list") {
            MovieListScreen(
                viewModel = viewModel,
                navController
            )
        }

        composable("movieDetail/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            if (movieId != null) {
                MovieDetailScreen(viewModel = viewModel, movieId = movieId.toInt(), navController = navController, apiKey = "34190fac5efa997a8fe0dd8d51356032")
            }
        }
    }
}