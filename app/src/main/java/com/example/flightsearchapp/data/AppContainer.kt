package com.example.flightsearchapp.data

import android.content.Context

interface AppContainer{
    val flightSearchRepository: FlightSearchRepository
}

class AppDataContainer(private val context: Context): AppContainer{
//    private val database: AirportDatabase by lazy {
//        AirportDatabase.getDatabase(context)
//    }
    override val flightSearchRepository: FlightSearchRepository by lazy {
        OfflineFlightSearchRepository(AirportDatabase.getDatabase(context).airportDao(),
            AirportDatabase.getDatabase(context).favoriteDao())
    }
}