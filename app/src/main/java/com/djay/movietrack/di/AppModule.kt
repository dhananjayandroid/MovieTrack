package com.djay.movietrack.di

import android.content.Context
import androidx.room.Room
import com.djay.movietrack.data.datasource.local.AppDatabase
import com.djay.movietrack.data.datasource.local.DataStoreManager
import com.djay.movietrack.data.datasource.remote.ApiURL
import com.djay.movietrack.data.datasource.remote.MoviesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * AppModule provides app-wide dependencies for the application.
 * It includes providers for Retrofit, DAOs, and the AppDatabase among others.
 * These dependencies are available as singletons throughout the app.
 *
 * @author Dhananjay Kumar
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton Retrofit instance configured with the base URL and Gson converter.
     * This instance is used to create API services for network requests.
     *
     * @return Configured Retrofit instance.
     */
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ApiURL.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Provides a singleton MoviesApiService instance for accessing the iTunes API.
     *
     * @param retrofit Retrofit instance to create the MoviesApiService.
     * @return MoviesApiService instance.
     */
    @Singleton
    @Provides
    fun provideMoviesApiService(retrofit: Retrofit): MoviesApiService =
        retrofit.create(MoviesApiService::class.java)

    /**
     * Provides a singleton AppDatabase instance for the application.
     *
     * @param context Application context to create the database.
     * @return AppDatabase instance.
     */
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "movie_track_database")
            .fallbackToDestructiveMigration()
            .build()

    /**
     * Provides a singleton MovieDao instance from the AppDatabase.
     *
     * @param database AppDatabase instance to retrieve the MovieDao.
     * @return MovieDao instance.
     */
    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase) = database.movieDao()

    /**
     * Provides a singleton DataStoreManager instance for managing preference data.
     *
     * @param context Application context to initialize DataStoreManager.
     * @return DataStoreManager instance.
     */
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStoreManager {
        return DataStoreManager(context)
    }
}
