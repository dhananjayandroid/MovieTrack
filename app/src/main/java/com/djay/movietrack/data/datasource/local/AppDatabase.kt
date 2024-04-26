package com.djay.movietrack.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.djay.movietrack.data.datasource.local.dao.MovieDao
import com.djay.movietrack.data.model.Movie

/**
 * AppDatabase provides a database instance for the application.
 * It includes entities and the version of the database schema.
 *
 * @author Dhananjay Kumar
 */
@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Retrieves the DAO for movie operations.
     *
     * @return instance of MovieDao for accessing movies data.
     */
    abstract fun movieDao(): MovieDao
}
