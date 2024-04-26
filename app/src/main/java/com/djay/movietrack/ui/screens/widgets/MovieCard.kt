package com.djay.movietrack.ui.screens.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.djay.movietrack.R
import com.djay.movietrack.data.model.Movie

/**
 * Displays a movie card that includes movie artwork, title, genre, price, and a favorite toggle.
 * The card can be clicked to navigate to the movie details.
 *
 * @param movie The movie data to display.
 * @param onNavigateToDetail Function to call when the card is clicked, used to navigate to the movie details.
 * @param onToggleFavorite Function to call when the favorite icon is clicked, used to toggle the favorite status of the movie.
 */
@Composable
fun MovieCard(
    movie: Movie,
    onNavigateToDetail: (Movie) -> Unit,
    onToggleFavorite: (Movie) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onNavigateToDetail(movie) }, // Clickable to navigate to movie details.
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Displays the movie artwork with placeholders for loading and error states.
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.artworkUrl100)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .build(),
                contentDescription = "Movie Artwork",
                modifier = Modifier.size(80.dp, 80.dp),
                contentScale = ContentScale.Crop
            )
            // Column for displaying the movie's title, genre, and price.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
            ) {
                Text(text = movie.trackName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = movie.genre,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = "$${movie.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // IconButton to toggle the favorite status of the movie.
            IconButton(onClick = { onToggleFavorite(movie) }) {
                Icon(
                    imageVector = if (movie.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (movie.isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (movie.isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}
