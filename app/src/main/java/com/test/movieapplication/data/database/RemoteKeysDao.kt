package com.test.movieapplication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: RemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE label = :label")
    suspend fun get(label: String): RemoteKeys?

    @Query("DELETE FROM remote_keys WHERE label = :label")
    suspend fun clear(label: String)
}