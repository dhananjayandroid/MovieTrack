package com.djay.movietrack.ui.screens.activity

import com.djay.movietrack.data.datasource.local.DataStoreManager
import com.djay.movietrack.data.model.Movie
import com.djay.movietrack.data.repository.MovieRepository
import com.djay.movietrack.utils.MainDispatcherRule
import com.djay.movietrack.utils.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {

    @get:Rule
    val rule = MainDispatcherRule()

    private lateinit var viewModel: MoviesViewModel
    private val movieRepository = mockk<MovieRepository>(relaxed = true)
    private val dataStoreManager = mockk<DataStoreManager>(relaxed = true)

    @Before
    fun setup() {
        viewModel = MoviesViewModel(movieRepository, dataStoreManager)
    }

    @Test
    fun `initial state is correct`() {
        assertEquals("star", viewModel.searchTerm.getOrAwaitValue())
        assertFalse(viewModel.isLoading.getOrAwaitValue())
        assertTrue(viewModel.movies.getOrAwaitValue().isEmpty())
    }

    @Test
    fun `searchMovies updates movies`() {
        // Mock data setup
        val movies = listOf(
            Movie(
                trackId = 1, trackName = "Star Wars", artworkUrl100 = "URL", price = 10.0,
                genre = "Sci-Fi", longDescription = "Long Description", isFavorite = false
            )
        )
        coEvery { movieRepository.getMovies(any(), any()) } returns movies

        // Act
        viewModel.searchMovies("Star Wars")

        // Assert
        val moviesResult = viewModel.movies.getOrAwaitValue()

        assertEquals(movies, moviesResult)
    }

    @Test
    fun `updateLastVisited sets current date in DataStore`() {
        viewModel.updateLastVisited()

        // Verifies that the saveLastVisitedDate method was called once
        coVerify { dataStoreManager.saveLastVisitedDate(any()) }
    }

    @Test
    fun `updateMovies handles errors gracefully`() {
        coEvery { movieRepository.getMovies(any(), any()) } throws Exception("Network error")

        viewModel.searchMovies("star")

        assertTrue(viewModel.movies.getOrAwaitValue().isEmpty())
    }

    @Test
    fun `toggleFavorite updates movie favorite status`() {
        val initialMovies = listOf(
            Movie(
                trackId = 1,
                trackName = "Movie 1",
                artworkUrl100 = "URL1",
                price = 10.0,
                genre = "Genre1",
                longDescription = "Description1",
                isFavorite = false
            ),
            Movie(
                trackId = 2,
                trackName = "Movie 2",
                artworkUrl100 = "URL2",
                price = 20.0,
                genre = "Genre2",
                longDescription = "Description2",
                isFavorite = true
            )
        )
        val expectedMovies = listOf(
            Movie(
                trackId = 1,
                trackName = "Movie 1",
                artworkUrl100 = "URL1",
                price = 10.0,
                genre = "Genre1",
                longDescription = "Description1",
                isFavorite = true
            ),
            Movie(
                trackId = 2,
                trackName = "Movie 2",
                artworkUrl100 = "URL2",
                price = 20.0,
                genre = "Genre2",
                longDescription = "Description2",
                isFavorite = true
            )
        )
        coEvery { movieRepository.getMovies(any(), any()) } returns initialMovies
        viewModel.searchMovies("Star Wars")

        // Act
        viewModel.toggleFavorite(initialMovies.first())

        val updatedMovies = viewModel.movies.getOrAwaitValue()
        assertEquals(expectedMovies, updatedMovies)
        coVerify { movieRepository.addFavorite(any()) }
        coVerify(exactly = 0) { movieRepository.removeFavorite(any()) }
    }

}
