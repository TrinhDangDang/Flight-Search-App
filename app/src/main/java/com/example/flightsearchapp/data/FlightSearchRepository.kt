package com.example.flightsearchapp.data


import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    fun getFavoriteFlightsStream(): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    fun getAllAirportsStream(): Flow<List<Airport>>

    fun getMatchingAirportsStream(searchKey: String): Flow<List<Airport>>
}