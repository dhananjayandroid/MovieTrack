package com.djay.movietrack.data.datasource.remote

import com.djay.movietrack.data.model.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * MoviesApiService defines the network access methods for interacting with the iTunes API.
 *
 * @author Dhananjay Kumar
 */
interface MoviesApiService {

    /**
     * Fetches movies from the iTunes API based on the search term provided.
     *
     * @param term The search keyword used to query movies.
     * @return Response containing the list of movies matched by the search term.
     */
    @GET(ApiURL.SEARCH_MOVIES)
    suspend fun searchMovies(@Query("term") term: String): Response<MovieResponse>
}
