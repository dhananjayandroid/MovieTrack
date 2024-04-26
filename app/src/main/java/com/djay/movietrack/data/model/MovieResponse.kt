package com.djay.movietrack.data.model

/**
 * MovieResponse represents the response structure received from the iTunes API when querying movies.
 * It encapsulates the count of results and the list of movies returned.
 *
 * @param resultCount The number of movies found.
 * @param results List of movies matching the query.
 * @author Dhananjay Kumar
 */
data class MovieResponse(
    val resultCount: Int,
    val results: List<Movie>
)
