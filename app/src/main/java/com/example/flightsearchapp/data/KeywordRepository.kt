package com.example.flightsearchapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KeywordRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val SEARCH_KEY = stringPreferencesKey("search_key")
    }
    val currentSearchKey: Flow<String> = dataStore.data.map {
        preferences -> preferences[SEARCH_KEY]?: ""
    }

    suspend fun saveCurrentSearchKey(searchKey: String){
        dataStore.edit {
            preferences -> preferences[SEARCH_KEY] = searchKey
        }
    }



}