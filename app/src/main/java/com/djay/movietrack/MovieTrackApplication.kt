package com.djay.movietrack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * MovieTrackApplication serves as the base class for maintaining global application state.
 * This class is set up to use Hilt for dependency injection across the entire application.
 * Extend this class from Android's Application class and annotate with @HiltAndroidApp
 * to automatically create and manage components for dependency injection.
 *
 * @author Dhananjay Kumar
 */
@HiltAndroidApp
class MovieTrackApplication : Application() {
    // Custom application setup can be done here if needed.
}
