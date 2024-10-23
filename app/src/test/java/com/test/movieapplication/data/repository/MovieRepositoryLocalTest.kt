package com.test.movieapplication.data.repository

import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.model.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MovieRepositoryLocalTest {

    @MockK
    private lateinit var movieDao: MovieDao

    private lateinit var movieRepository: MovieRepositoryLocal

    private lateinit var movie: Movie

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        movieRepository = MovieRepositoryLocal(movieDao)

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
    fun `getPopularMovies returns movies from DB when offline`() = runTest {
        val page = 1

        coEvery { movieDao.getAllMovies() } returns listOf(movie)

        // Act
        val result = movieRepository.getPopularMoviesDb(page)

        // Assert
        Assert.assertEquals(listOf(movie), result.results)
        coVerify { movieDao.getAllMovies() }
    }


    @Test
    fun `searchMovies returns movies from DB when offline`() = runTest {
        val query = "Batman"
        val page = 1

        coEvery { movieDao.searchMovie(any()) } returns listOf(movie)

        // Act
        val result = movieRepository.searchMoviesDb( query, page)

        // Assert
        Assert.assertEquals(listOf(movie), result.results)
        coVerify { movieDao.searchMovie(query) }
    }

    @Test
    fun `getMovieById returns movie details from DB when offline`() = runTest {
        val movieId = 1

        coEvery { movieDao.getMovie(any()) } returns movie

        // Act
        val result = movieRepository.getMovieByIdDb(movieId)

        // Assert
        Assert.assertEquals(movie.id, result.id)
        Assert.assertEquals(movie.original_title, result.original_title)
        Assert.assertEquals(movie.overview, result.overview)
        coVerify { movieDao.getMovie(movieId.toLong()) }
    }
}