package com.test.movieapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val label: String,   // na primer "popular"
    val nextPage: Int?,              // null = nema poveke strani
    val lastUpdated: Long            // vreme na posledno osvezhuvanje
)