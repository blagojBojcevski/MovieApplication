package com.test.movieapplication.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.model.Movie
import com.test.movieapplication.data.model.MovieDetail
import com.test.movieapplication.data.model.MovieResponse
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    @MockK
    private lateinit var apiService: MovieApiService

    @MockK
    private lateinit var movieDao: MovieDao

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var connectivityManager: ConnectivityManager

    @MockK
    private lateinit var network: Network

    @MockK
    private lateinit var networkCapabilities: NetworkCapabilities

    private lateinit var movieRepository: MovieRepository

    private lateinit var movie: Movie

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        movieRepository = MovieRepository(apiService, movieDao, context)

        // Mock the connectivity service
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        // Mock the network and capabilities behavior
        every { connectivityManager.getActiveNetwork() } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities

        // Mock the network capabilities to simulate online behavior (WiFi)
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false

        // Sample Movie object
        movie = Movie(
            id = 1L,
            original_title = "Movie 1",
            overview = "Test overview",
            poster_path = "posterPath",
            backdrop_path = "backdropPath",
            original_language = "en",
            vote_count = "100",
            vote_average = 8.5
        )
    }

    @Test
    fun `getPopularMovies returns movies from API when online`() = runTest {
        // Arrange
        val apiKey = "api_key"
        val page = 1
        val movieResponse = MovieResponse(
            results = listOf(movie),
            page = page,
            total_pages = 1
        )

        // Mock the API call and DAO interaction
        coEvery { apiService.getPopularMovies(any(), any(), any()) } returns movieResponse
        coEvery { movieDao.insertMovieList(any()) } just Runs

        // Act
        val result = movieRepository.getPopularMovies(apiKey, page)

        // Assert
        Assert.assertEquals(movieResponse, result)
        coVerify { apiService.getPopularMovies("en-US", apiKey, page) }
        coVerify { movieDao.insertMovieList(movieResponse.results) }
    }

    @Test
    fun `getPopularMovies returns movies from DB when offline`() = runTest {
        // Arrange
        val apiKey = "api_key"
        val page = 1

        // Simulate offline behavior
        every { connectivityManager.getActiveNetwork() } returns null
        coEvery { movieDao.getAllMovies() } returns listOf(movie)

        // Act
        val result = movieRepository.getPopularMovies(apiKey, page)

        // Assert
        Assert.assertEquals(listOf(movie), result.results)
        coVerify { movieDao.getAllMovies() }
        coVerify(exactly = 0) { apiService.getPopularMovies(any(), any(), any()) }
    }

    @Test
    fun `searchMovies returns movies from API when online`() = runTest {
        // Arrange
        val apiKey = "api_key"
        val query = "Batman"
        val page = 1
        val movieResponse = MovieResponse(
            results = listOf(movie),
            page = page,
            total_pages = 1
        )

        // Mock API call and DAO interaction
        coEvery { apiService.searchMovieCollection(any(), any(), any(), any()) } returns movieResponse
        coEvery { movieDao.insertMovieList(any()) } just Runs

        // Act
        val result = movieRepository.searchMovies(apiKey, query, page)

        // Assert
        Assert.assertEquals(movieResponse, result)
        coVerify { apiService.searchMovieCollection(query, "en-US", apiKey, page) }
        coVerify { movieDao.insertMovieList(movieResponse.results) }
    }

    @Test
    fun `searchMovies returns movies from DB when offline`() = runTest {
        // Arrange
        val apiKey = "api_key"
        val query = "Batman"
        val page = 1

        // Simulate offline behavior
        every { connectivityManager.getActiveNetwork() } returns null
        coEvery { movieDao.searchMovie(any()) } returns listOf(movie)

        // Act
        val result = movieRepository.searchMovies(apiKey, query, page)

        // Assert
        Assert.assertEquals(listOf(movie), result.results)
        coVerify { movieDao.searchMovie(query) }
        coVerify(exactly = 0) { apiService.searchMovieCollection(any(), any(), any(), any()) }
    }

    @Test
    fun `getMovieById returns movie details from API when online`() = runTest {
        // Arrange
        val apiKey = "api_key"
        val movieDetail = MovieDetail(
            id = 1,
            original_title = movie.original_title!!,
            overview = movie.overview!!,
            poster_path = movie.poster_path,
            original_language = movie.original_language,
            original_name = "",
            vote_count = movie.vote_count,
            vote_average = movie.vote_average!!,
            authors = listOf()
        )

        // Mock API call
        coEvery { apiService.getMovieById(any(), any(), any()) } returns movieDetail

        // Act
        val result = movieRepository.getMovieById(1, apiKey)

        // Assert
        Assert.assertEquals(movieDetail, result)
        coVerify { apiService.getMovieById(1, "en-US", apiKey) }
    }

    @Test
    fun `getMovieById returns movie details from DB when offline`() = runTest {
        // Arrange
        val apiKey = "api_key"
        val movieId = 1

        // Simulate offline behavior
        every { connectivityManager.getActiveNetwork() } returns null
        coEvery { movieDao.getMovie(any()) } returns movie

        // Act
        val result = movieRepository.getMovieById(movieId, apiKey)

        // Assert
        Assert.assertEquals(movie.id, result.id)
        Assert.assertEquals(movie.original_title, result.original_title)
        Assert.assertEquals(movie.overview, result.overview)
        coVerify { movieDao.getMovie(movieId.toLong()) }
        coVerify(exactly = 0) { apiService.getMovieById(any(), any(), any()) }
    }
}