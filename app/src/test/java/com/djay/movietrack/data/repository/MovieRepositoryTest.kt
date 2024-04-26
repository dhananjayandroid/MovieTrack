package com.djay.movietrack.data.repository

import com.djay.movietrack.data.datasource.local.AppDatabase
import com.djay.movietrack.data.datasource.local.dao.MovieDao
import com.djay.movietrack.data.datasource.remote.MoviesApiService
import com.djay.movietrack.data.model.Movie
import com.djay.movietrack.data.model.MovieResponse
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class MovieRepositoryTest {

    private lateinit var repository: MovieRepository
    private val apiService = mockk<MoviesApiService>()
    private val db = mockk<AppDatabase>()
    private val movieDao = mockk<MovieDao>()

    @Before
    fun setup() {
        coEvery { db.movieDao() } returns movieDao
        repository = MovieRepository(apiService, db)
    }

    @Test
    fun `getMovies fetches from API and combines with favorites from DB`() = runTest {
        val apiMovies = listOf(
            Movie(trackId = 1, trackName = "Api Movie", artworkUrl100 = "", price = 10.0, genre = "Action", longDescription = "", isFavorite = false)
        )
        val dbMovies = listOf(
            Movie(trackId = 1, trackName = "Db Movie", artworkUrl100 = "", price = 10.0, genre = "Action", longDescription = "", isFavorite = true)
        )

        coEvery { apiService.searchMovies(any()) } returns Response.success(MovieResponse(0, apiMovies))
        coEvery { movieDao.getAllMovies() } returns dbMovies

        val movies = repository.getMovies("test")

        assertEquals(1, movies.size)
        assertTrue(movies.first().isFavorite)
    }

    @Test
    fun `getMovies handles initial load when API returns empty list`() = runTest {
        coEvery { apiService.searchMovies(any()) } returns Response.success(MovieResponse(0, emptyList()))
        coEvery { movieDao.getAllMovies() } returns listOf(
            Movie(trackId = 2, trackName = "Favorite Movie", artworkUrl100 = "", price = 20.0, genre = "Drama", longDescription = "", isFavorite = true)
        )

        val movies = repository.getMovies("test", initialLoad = true)

        assertEquals(1, movies.size)
        assertEquals("Favorite Movie", movies.first().trackName)
    }

    @Test
    fun `addFavorite inserts movie into database`() = runTest {
        val movie = Movie(trackId = 3, trackName = "New Favorite", artworkUrl100 = "", price = 30.0, genre = "Comedy", longDescription = "", isFavorite = false)

        coEvery { movieDao.insertMovie(any()) } just Runs

        repository.addFavorite(movie)

        coVerify { movieDao.insertMovie(match { it.isFavorite && it.trackId == 3 }) }
    }

    @Test
    fun `removeFavorite removes movie from database`() = runTest {
        val movie = Movie(trackId = 4, trackName = "Remove Favorite", artworkUrl100 = "", price = 40.0, genre = "Horror", longDescription = "", isFavorite = true)

        coEvery { movieDao.deleteMovie(any()) } just Runs

        repository.removeFavorite(movie)

        coVerify { movieDao.deleteMovie(movie) }
    }

    @Test
    fun `getMovies handles API errors gracefully`() = runTest {
        coEvery { apiService.searchMovies(any()) } throws Exception("Network error")
        coEvery { movieDao.getAllMovies() } returns emptyList()

        val movies = repository.getMovies("test")

        assertTrue(movies.isEmpty())
    }
}
