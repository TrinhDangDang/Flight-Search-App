package com.example.flightsearchapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


interface AppContainer{
    val flightSearchRepository: FlightSearchRepository
    val keywordRepository: KeywordRepository
}

class AppDataContainer(private val context: Context, private val dataStore: DataStore<Preferences>): AppContainer{
//    private val database: AirportDatabase by lazy {
//        AirportDatabase.getDatabase(context)
//    }
    override val flightSearchRepository: FlightSearchRepository by lazy {
        OfflineFlightSearchRepository(AirportDatabase.getDatabase(context).airportDao(),
            AirportDatabase.getDatabase(context).favoriteDao())
    }
    override val keywordRepository: KeywordRepository by lazy {
        KeywordRepository(dataStore)
    }
}