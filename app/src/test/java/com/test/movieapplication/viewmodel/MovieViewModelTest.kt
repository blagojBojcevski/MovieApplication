package com.test.movieapplication.viewmodel

import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import com.test.movieapplication.domain.MovieInteractor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    private lateinit var repository: MovieInteractor

    private lateinit var viewModel: MovieViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        repository = mockk()

        Dispatchers.setMain(testDispatcher)

        viewModel = MovieViewModel(repository)
    }

    @Test
    fun `fetchMovieById updates movieDetailStateFlow`() = runTest {
        val movieDetail = MovieDetail(
            id = 1,
            original_title = "Inception",
            overview = "A mind-bending thriller",
            poster_path = "/inception.jpg",
            original_language = "en",
            original_name = "Inception",
            vote_count = "10000",
            vote_average = 8.8,
            authors = listOf()
        )

        // Mock repository call using MockK
        coEvery { repository.getMovie(1, any()) } returns movieDetail

        viewModel.fetchMovieById(1)

        advanceUntilIdle()

        assertEquals(movieDetail, viewModel.movieDetailStateFlow.value)

        // Verify that repository method was called
        coVerify { repository.getMovie(1, any()) }
    }

    @Test
    fun `searchMovies returns MovieResponse`() = runTest {
        val movie = Movie(
            id = 1,
            original_title = "Inception",
            overview = "A mind-bending thriller",
            poster_path = "/inception.jpg",
            backdrop_path = "/inception.jpg",
            original_language = "en",
            vote_count = "122",
            vote_average = 1.1
        )
        val movieResponse = MovieResponse(
            results = listOf(movie),
            page = 1,
            total_pages = 10
        )

        // Mock repository call for searching movies
        coEvery { repository.searchMovies(any(), "Inception", 1) } returns movieResponse

        val result = repository.searchMovies("some_api_key", "Inception", 1)

        assertEquals(movieResponse, result)

        // Verify repository interaction
        coVerify { repository.searchMovies(any(), "Inception", 1) }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}