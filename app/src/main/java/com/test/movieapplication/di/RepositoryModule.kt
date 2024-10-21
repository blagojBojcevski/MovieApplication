package com.test.movieapplication.di

import android.content.Context
import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.repository.MovieRepository
import com.test.movieapplication.data.repository.MovieRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        apiService: MovieApiService,
        movieDao: MovieDao,
        @ApplicationContext context: Context
    ): MovieRepositoryInterface {
        return MovieRepository(apiService, movieDao, context)
    }
}