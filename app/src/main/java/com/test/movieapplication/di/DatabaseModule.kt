package com.test.movieapplication.di

import android.app.Application
import androidx.room.Room
import com.test.movieapplication.R
import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): MovieDatabase {
        return Room
            .databaseBuilder(
                application,
                MovieDatabase::class.java,
                application.getString(R.string.database)
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePosterDao(appDatabase: MovieDatabase): MovieDao {
        return appDatabase.movieDao()
    }
}
