package com.djay.movietrack.ui.screens.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Displays a loading indicator (circular progress bar) in the center of the screen when loading data.
 * The indicator is visible only when the isLoading parameter is true.
 *
 * @param isLoading Boolean flag indicating whether to show the loading indicator.
 */
@Composable
fun LoadingIndicator(isLoading: Boolean) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()  // Displays the circular progress indicator.
        }
    }
}
