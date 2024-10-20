package com.test.movieapplication.data.repository

import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.model.MovieResponse
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    @Mock
    private lateinit var apiService: MovieApiService

    private lateinit var repository: MovieRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = MovieRepository(apiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test fetching popular movies returns correct data`() = runTest {
        val popularMoviesResponse = MovieResponse(
            results = listOf(
                Movie(id = 1, original_title = "Movie 1", overview = "Overview 1", poster_path = "/path1.jpg", backdrop_path = "", original_language = "", original_name = ""),
                Movie(id = 2, original_title = "Movie 2", overview = "Overview 2", poster_path = "/path2.jpg", backdrop_path = "", original_language = "", original_name = "")
            ),
            total_pages = 1,
            page = 1
        )

        `when`(apiService.getPopularMovies("en-US", "fake_api_key", 1)).thenReturn(popularMoviesResponse)

        val result = repository.getPopularMovies("fake_api_key", 1)

        assertNotNull(result)
        assertEquals(2, result.results.size)
        assertEquals("Movie 1", result.results[0].original_title)
        assertEquals("Movie 2", result.results[1].original_title)
    }

    @Test
    fun `test search movies returns correct data`() = runTest {
        val searchResponse = MovieResponse(
            results = listOf(
                Movie(id = 1, original_title = "Movie 1", overview = "Overview 1", poster_path = "/path1.jpg", backdrop_path = "", original_language = "", original_name = "")
            ),
            total_pages = 1,
            page = 1
        )

        `when`(apiService.searchMovieCollection("query", "en-US", "fake_api_key", 1)).thenReturn(searchResponse)

        val result = repository.searchMovies("fake_api_key", "query", 1)

        assertNotNull(result)
        assertEquals(1, result.results.size)
        assertEquals("Movie 1", result.results[0].original_title)
    }
}
