package com.djay.movietrack.ui.screens.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djay.movietrack.data.datasource.local.DataStoreManager
import com.djay.movietrack.data.model.Movie
import com.djay.movietrack.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * MoviesViewModel manages the UI-related data for the movie screens and handles the business logic.
 * It communicates with the repository to fetch and update the data.
 * It also tracks and updates user preferences like the last visited time.
 *
 * @param movieRepository The repository to manage movie data operations.
 * @param dataStoreManager The manager to handle user preferences stored locally.
 * @author Dhananjay Kumar
 */
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchTerm = MutableLiveData("star")
    val searchTerm: LiveData<String> = _searchTerm

    // Live data to observe last visited date from data store.
    val lastVisited = dataStoreManager.lastVisitedDate

    init {
        updateMovies()
    }

    /**
     * Toggles the favorite status of a movie and updates the repository accordingly.
     * @param movie The movie to toggle the favorite status.
     */
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            val newList = _movies.value?.map { existingMovie ->
                if (existingMovie.trackId == movie.trackId) {
                    existingMovie.copy(isFavorite = !existingMovie.isFavorite)
                        .also { updatedMovie ->
                            if (updatedMovie.isFavorite) {
                                movieRepository.addFavorite(updatedMovie)
                            } else {
                                movieRepository.removeFavorite(updatedMovie)
                            }
                        }
                } else {
                    existingMovie
                }
            }

            newList?.let {
                _movies.postValue(it)
            }
        }
    }

    /**
     * Searches movies by the given term and updates the list of movies.
     * @param searchTerm The search term to filter movies.
     */
    fun searchMovies(searchTerm: String) {
        _searchTerm.value = searchTerm
        updateMovies()
    }

    /**
     * Updates the list of movies based on the current search term and updates loading status.
     */
    private fun updateMovies() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _searchTerm.value?.let {
                val moviesFromRepo = movieRepository.getMovies(it, it == "star")
                _movies.postValue(moviesFromRepo)
            }
            _isLoading.postValue(false)
        }
    }

    /**
     * Updates the last visited time in the user preferences.
     */
    fun updateLastVisited() {
        viewModelScope.launch {
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd h:mm:ss a", Locale.getDefault()).format(Date())
            dataStoreManager.saveLastVisitedDate(currentDate)
        }
    }
}
