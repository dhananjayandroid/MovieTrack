package com.djay.movietrack.ui.screens.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.djay.movietrack.ui.screens.MovieDetailScreen
import com.djay.movietrack.ui.screens.MovieListScreen
import com.djay.movietrack.ui.theme.MovieTrackTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity is the entry point of the application, set up as an Android Entry Point to inject dependencies.
 * It observes the application lifecycle to manage the last visited information.
 *
 * @author Dhananjay Kumar
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppLifecycleObserver {
            viewModel.updateLastVisited()
        })
        setContent {
            MovieTrackTheme {
                // A surface container using the 'background' color from the theme.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieNavigation(viewModel)
                }
            }
        }
    }
}

/**
 * MovieNavigation manages the navigation of movie related screens within the app.
 *
 * @param viewModel ViewModel that manages the data for UI components.
 */
@Composable
fun MovieNavigation(viewModel: MoviesViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "movieList") {
        composable("movieList") {
            MovieListScreen(navigateToDetail = { movieId ->
                navController.navigate("movieDetail/$movieId")
            }, viewModel)
        }
        composable(
            "movieDetail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
            MovieDetailScreen(movieId = movieId, viewModel)
        }
    }
}

/**
 * AppLifecycleObserver observes the application's lifecycle events to handle background actions.
 * Specifically, it triggers an action when the app goes into the background.
 *
 * @param onAppBackgrounded Function to execute when the app goes into the background.
 */
class AppLifecycleObserver(private val onAppBackgrounded: () -> Unit) : DefaultLifecycleObserver {
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        onAppBackgrounded()
    }
}
