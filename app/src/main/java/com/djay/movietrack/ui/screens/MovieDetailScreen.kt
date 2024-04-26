package com.djay.movietrack.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.djay.movietrack.R
import com.djay.movietrack.data.model.Movie
import com.djay.movietrack.ui.screens.activity.MoviesViewModel


/**
 * MovieDetailScreen displays detailed information about a specific movie identified by movieId.
 * It observes movie data from the viewModel to find and display the details of the selected movie.
 *
 * @param movieId The ID of the movie for which details are to be displayed.
 * @param viewModel The ViewModel that holds and manages the data related to movies.
 */
@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MoviesViewModel
) {
    val scrollState = rememberScrollState()
    val movies by viewModel.movies.observeAsState(listOf())
    val movie = movies.firstOrNull { it.trackId == movieId }

    movie?.let { safeMovie ->
        MovieDetailsLayout(safeMovie, scrollState, viewModel)
    } ?: Text(
        "Loading movie details...",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}

/**
 * Layout for displaying the detailed components of a movie.
 * Includes artwork and other content details like genre and price.
 *
 * @param movie The movie object containing detailed information to display.
 * @param scrollState The scroll state for vertical scrolling behavior.
 * @param viewModel ViewModel for managing UI-related data and operations.
 */
@Composable
fun MovieDetailsLayout(
    movie: Movie,
    scrollState: ScrollState,
    viewModel: MoviesViewModel
) {
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        MovieArtworkHeader(movie.artworkUrl100)
        MovieContentDetails(movie, viewModel)
    }
}

/**
 * Displays the movie's artwork at the top of the detail screen.
 *
 * @param artworkUrl The URL of the movie's artwork.
 */
@Composable
fun MovieArtworkHeader(artworkUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(artworkUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .build(),
        contentDescription = "Movie Artwork",
        modifier = Modifier
            .height(250.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
    Spacer(modifier = Modifier.height(4.dp))
}

/**
 * Contains the main content details about the movie, such as basic info and description.
 * Also includes an action to toggle the movie as a favorite.
 *
 * @param movie The movie data.
 * @param viewModel ViewModel for handling favorite action.
 */
@Composable
fun MovieContentDetails(
    movie: Movie,
    viewModel: MoviesViewModel
) {
    MovieBasicInfo(movie)
    DescriptionWithFavoriteAction(movie, viewModel)
    MovieDescription(movie.longDescription)
}

/**
 * Displays basic information about the movie such as title, genre, and price.
 *
 * @param movie The movie whose basic info is displayed.
 */
@Composable
fun MovieBasicInfo(movie: Movie) {
    Text(
        text = movie.trackName,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Text(
        text = "Genre: ${movie.genre}",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Text(
        text = "Price: $${movie.price}",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

/**
 * Displays the movie description and a button to toggle the favorite status.
 *
 * @param movie The movie to toggle favorite.
 * @param viewModel The ViewModel that manages the toggle favorite action.
 */
@Composable
fun DescriptionWithFavoriteAction(
    movie: Movie,
    viewModel: MoviesViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            onClick = { viewModel.toggleFavorite(movie) }
        ) {
            Icon(
                imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Toggle Favorite",
                tint = if (movie.isFavorite) Color.Red else Color.Gray
            )
        }
    }
}

/**
 * Displays a detailed description of the movie.
 *
 * @param description The text description of the movie.
 */
@Composable
fun MovieDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}
