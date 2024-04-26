package com.djay.movietrack.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to create a singleton preferences DataStore named "settings".
val Context.dataStore by preferencesDataStore(name = "settings")

/**
 * DataStoreManager manages data storage and retrieval through DataStore.
 *
 * @param context Context to access DataStore.
 * @author Dhananjay Kumar
 */
class DataStoreManager(private val context: Context) {

    companion object {
        // Key for storing the last visited date as a string in DataStore.
        private val LAST_VISITED_KEY = stringPreferencesKey("last_visited")
    }

    /**
     * Saves the last visited date to the DataStore.
     *
     * @param date The date string to be saved.
     */
    suspend fun saveLastVisitedDate(date: String) {
        context.dataStore.edit { settings ->
            settings[LAST_VISITED_KEY] = date
        }
    }

    // Retrieves the last visited date from DataStore or returns "Never" if not set.
    val lastVisitedDate: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_VISITED_KEY] ?: "Never"
        }
}
