package com.test.movieapplication.di

import com.test.movieapplication.data.api.MovieApiService
import com.test.movieapplication.data.database.MovieDao
import com.test.movieapplication.data.repository.MovieRepositoryAPI
import com.test.movieapplication.data.repository.MovieRepositoryAPIInterface
import com.test.movieapplication.data.repository.MovieRepositoryLocal
import com.test.movieapplication.data.repository.MovieRepositoryLocalInterface
import com.test.movieapplication.domain.MovieInteractor
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
    fun provideMainRepository(
        apiService: MovieApiService,
        movieDao: MovieDao
    ): MovieRepositoryAPIInterface {
        return MovieRepositoryAPI(apiService, movieDao)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(movieDao: MovieDao): MovieRepositoryLocalInterface {
        return MovieRepositoryLocal(movieDao)
    }

    @Provides
    @Singleton
    fun provideMovieInteractor(
        apiRepository: MovieRepositoryAPIInterface,
        localRepository: MovieRepositoryLocalInterface
    ): MovieInteractor {
        return MovieInteractor(apiRepository, localRepository)
    }
}