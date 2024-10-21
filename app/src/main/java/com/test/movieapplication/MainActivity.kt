package com.test.movieapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.test.movieapplication.ui.details.MovieDetailScreen
import com.test.movieapplication.ui.main.MovieListScreen
import com.test.movieapplication.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppNavigation(viewModel = movieViewModel, applicationContext)
        }
    }
}

@Composable
fun MovieAppNavigation(viewModel: MovieViewModel, context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.MovieList) {

        // Movie list screen route
        composable(Routes.MovieList) {
            MovieListScreen(
                viewModel = viewModel,
                navController = navController,
                context = context
            )
        }

        // Movie detail screen route with a dynamic movieId argument
        composable("${Routes.MovieDetail}/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            movieId?.let {
                MovieDetailScreen(
                    viewModel = viewModel,
                    movieId = it,
                    navController = navController
                )
            }
        }
    }
}

/**
 * Object containing route definitions for the navigation graph.
 * Using constants helps reduce errors and improves code readability.
 */
object Routes {
    const val MovieList = "movie_list"
    const val MovieDetail = "movieDetail"
}