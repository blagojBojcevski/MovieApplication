package com.test.movieapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.test.movieapplication.ui.main.MovieListScreen
import com.test.movieapplication.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val apiKey = "34190fac5efa997a8fe0dd8d51356032"

            MovieListScreen(
                viewModel = movieViewModel,
                apiKey= apiKey
            )
        }
    }
}