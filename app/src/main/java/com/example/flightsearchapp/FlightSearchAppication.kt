package com.example.flightsearchapp

import android.app.Application
import android.content.Context
import android.preference.PreferenceDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.AppContainer
import com.example.flightsearchapp.data.AppDataContainer

private const val KEYWORD_DATASTORE_NAME = "keyword_data_store"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = KEYWORD_DATASTORE_NAME
)

class FlightSearchApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this, this.dataStore)
    }
}