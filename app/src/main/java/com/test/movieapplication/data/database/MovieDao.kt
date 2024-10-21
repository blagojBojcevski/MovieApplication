package com.test.movieapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.movieapplication.data.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movies: List<Movie>)

    @Query("SELECT * FROM Movie WHERE id = :id_")
    suspend fun getMovie(id_: Long): Movie

    @Query("SELECT * FROM Movie")
    suspend fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE original_title LIKE '%' || :title || '%'")
    suspend fun searchMovie(title: String):  List<Movie>
}
