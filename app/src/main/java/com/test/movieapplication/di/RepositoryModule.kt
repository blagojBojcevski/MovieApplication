package com.test.movieapplication.di

import com.test.movieapplication.data.repository.MovieRepository
import com.test.movieapplication.data.repository.MovieRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieRepository: MovieRepository
    ): MovieRepositoryInterface {
        return movieRepository
    }
}