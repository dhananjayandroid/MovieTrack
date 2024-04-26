package com.djay.movietrack.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * This is the Movie data class which represents a movie entity in the database.
 * It is annotated with @Entity, indicating that this class is an SQLite table in the Room Database.
 * The table name is defined as "movies".
 *
 * @property trackId The unique ID of the movie.
 * @property trackName The name of the movie.
 * @property artworkUrl100 The URL of the movie's artwork (100x100).
 * @property price The price of the movie.
 * @property genre The genre of the movie.
 * @property longDescription The long description of the movie.
 * @property isFavorite A boolean flag indicating whether the movie is marked as favorite by the user.
 *
 * @author Dhananjay Kumar
 */
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey val trackId: Int, // The unique ID of the movie
    val trackName: String, // The name of the movie
    val artworkUrl100: String, // The URL of the movie's artwork (100x100)
    @SerializedName("trackPrice") val price: Double, // The price of the movie
    @SerializedName("primaryGenreName") val genre: String, // The genre of the movie
    val longDescription: String, // The long description of the movie
    var isFavorite: Boolean = false // A boolean flag indicating whether the movie is marked as favorite by the user
)
