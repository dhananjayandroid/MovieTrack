package com.djay.movietrack.data.datasource.remote

/**
 * ApiURL holds the base URLs and endpoints for accessing the iTunes API.
 *
 * @author Dhananjay Kumar
 */
object ApiURL {
    // Base URL for the iTunes API.
    const val BASE_URL = "https://itunes.apple.com/"

    // Endpoint for searching movies in the Australian store.
    const val SEARCH_MOVIES = "search?country=au&media=movie"
}
