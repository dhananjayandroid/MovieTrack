package com.djay.movietrack.di

import com.djay.movietrack.data.datasource.local.AppDatabase
import com.djay.movietrack.data.datasource.remote.MoviesApiService
import com.djay.movietrack.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * RepositoryModule provides app-wide repository dependencies for the application.
 * This module ensures that the repository is available as a singleton throughout the app
 * through Dagger Hilt's dependency injection framework.
 *
 * @author Dhananjay Kumar
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a singleton MovieRepository instance.
     * This repository is responsible for handling data operations related to movies,
     * both from a local database and from a remote API service.
     *
     * @param apiService The MoviesApiService to perform API calls.
     * @param appDatabase The AppDatabase for local data storage operations.
     * @return A single instance of MovieRepository.
     */
    @Singleton
    @Provides
    fun provideMovieRepository(
        apiService: MoviesApiService,
        appDatabase: AppDatabase
    ): MovieRepository {
        return MovieRepository(apiService, appDatabase)
    }
}
