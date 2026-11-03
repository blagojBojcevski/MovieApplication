package com.test.movieapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

        composable(Routes.MovieList) {
            MovieListScreen(
                viewModel = viewModel,
                navController = navController,
                context = context
            )
        }


        composable(
            route = "${Routes.MovieDetail}/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            MovieDetailScreen(onBack = { navController.popBackStack() })
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