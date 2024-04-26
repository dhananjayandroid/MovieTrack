package com.djay.movietrack.data.repository

import com.djay.movietrack.data.datasource.local.AppDatabase
import com.djay.movietrack.data.datasource.remote.MoviesApiService
import com.djay.movietrack.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

/**
 * This is the MovieRepository class which handles data operations for Movie objects.
 * It uses a MoviesApiService for fetching data from an API and a Room database for local storage.
 *
 * @property apiService The service used to fetch movies from an API.
 * @property movieDao The DAO used for accessing the local Room database.
 *
 * @author Dhananjay Kumar
 */
class MovieRepository(
    private val apiService: MoviesApiService, // The service used to fetch movies from an API
    db: AppDatabase // The Room database
) {
    private val movieDao = db.movieDao() // The DAO used for accessing the local Room database

    /**
     * Fetches movies based on a search term. If the API fetch fails or returns no results and it's an initial load,
     * it returns the favorite movies from the local database.
     *
     * @param searchTerm The term to search for in the movie titles.
     * @param initialLoad A flag indicating whether this is the initial load.
     * @return A list of movies.
     */
    suspend fun getMovies(searchTerm: String, initialLoad: Boolean = false): List<Movie> =
        withContext(Dispatchers.IO) {
            coroutineScope {
                val apiMoviesDeferred = async { fetchMoviesFromApi(searchTerm) } // Fetch movies from the API
                val favoriteMoviesDeferred = async { movieDao.getAllMovies() } // Fetch favorite movies from the local database

                val apiMovies = apiMoviesDeferred.await() ?: emptyList()
                val favoriteMoviesFromDb = favoriteMoviesDeferred.await()

                if (apiMovies.isEmpty() && initialLoad)
                    favoriteMoviesFromDb // If the API fetch returned no results and it's an initial load, return the favorite movies from the local database
                else {
                    val favoriteMovies = favoriteMoviesFromDb.associateBy { it.trackId }
                    apiMovies.map { apiMovie ->
                        val movie = Movie(
                            trackId = apiMovie.trackId,
                            trackName = apiMovie.trackName,
                            artworkUrl100 = apiMovie.artworkUrl100,
                            price = apiMovie.price,
                            genre = apiMovie.genre,
                            longDescription = apiMovie.longDescription,
                            isFavorite = false
                        )

                        movie.copy(isFavorite = favoriteMovies[movie.trackId]?.isFavorite ?: false) // Copy the movie object and set the isFavorite property based on the local database
                    }
                }
            }
        }

    /**
     * Fetches movies from the API based on a search term.
     *
     * @param searchTerm The term to search for in the movie titles.
     * @return A list of movies, or null if the fetch failed.
     */
    private suspend fun fetchMoviesFromApi(searchTerm: String): List<Movie>? {
        return try {
            val response = apiService.searchMovies(searchTerm) // Fetch movies from the API
            if (response.isSuccessful) {
                response.body()?.results // If the fetch was successful, return the results
            } else {
                throw Exception("API request failed with error: ${response.message()}") // If the fetch failed, throw an exception
            }
        } catch (e: Exception) {
            null // If an exception was thrown, return null
        }
    }

    /**
     * Adds a movie to the favorites in the local database.
     *
     * @param movie The movie to add to the favorites.
     */
    suspend fun addFavorite(movie: Movie) {
        val favoriteMovie = movie.copy(isFavorite = true) // Create a copy of the movie object with isFavorite set to true
        movieDao.insertMovie(favoriteMovie) // Insert the favorite movie into the local database
    }

    /**
     * Removes a movie from the favorites in the local database.
     *
     * @param movie The movie to remove from the favorites.
     */
    suspend fun removeFavorite(movie: Movie) {
        movieDao.deleteMovie(movie) // Delete the movie from the local database
    }
}
