package com.djay.movietrack.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.djay.movietrack.ui.screens.activity.MoviesViewModel
import com.djay.movietrack.ui.screens.widgets.LoadingIndicator
import com.djay.movietrack.ui.screens.widgets.MovieCard
import com.djay.movietrack.ui.screens.widgets.SearchAppBar

/**
 * MovieListScreen displays a list of movies and allows for searching and navigating to movie details.
 * It includes a search bar, loading indicator, and the date of the last visit to the app.
 *
 * @param navigateToDetail Function to navigate to movie details using the movie ID.
 * @param viewModel The MoviesViewModel that provides the movie data and functionality.
 */
@Composable
fun MovieListScreen(
    navigateToDetail: (Int) -> Unit,
    viewModel: MoviesViewModel
) {
    val movies by viewModel.movies.observeAsState(listOf())
    val isLoading by viewModel.isLoading.observeAsState(false)

    // State to control the visibility of the search bar and the content of the search text.
    val showSearchBar = rememberSaveable { mutableStateOf(false) }
    val searchText = rememberSaveable { mutableStateOf("") }

    Column {
        // Search bar that handles user inputs for searching movies.
        SearchAppBar(
            searchText = searchText,
            showSearchBar = showSearchBar,
            onSearchChanged = { newText ->
                searchText.value = newText
                viewModel.searchMovies(newText) // Trigger a search in the ViewModel.
            },
            onSearchClosed = {
                showSearchBar.value = false
                searchText.value = ""
                viewModel.searchMovies("star") // Resets the search to default when closed.
            }
        )
        // Display the last visited date.
        val lastVisited by viewModel.lastVisited.collectAsState(initial = "Never")
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "Last Visited: $lastVisited",
            style = MaterialTheme.typography.labelMedium
        )
        // Loading indicator to show while movies are being fetched.
        LoadingIndicator(isLoading)
        // List of movies displayed in a scrollable column.
        LazyColumn {
            items(movies) { movie ->
                MovieCard(
                    movie = movie,
                    onNavigateToDetail = { navigateToDetail(it.trackId) }, // Navigate to movie details.
                    onToggleFavorite = viewModel::toggleFavorite // Toggle favorite status of a movie.
                )
            }
        }
    }
}
