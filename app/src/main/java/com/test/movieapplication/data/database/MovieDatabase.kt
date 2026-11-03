package com.test.movieapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.movieapplication.data.model.Movie

@Database(
    entities = [Movie::class, RemoteKeys::class],  // dodadi RemoteKeys
    version = 2,                                   // bese 1
    exportSchema = true
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao    // NOVO
}