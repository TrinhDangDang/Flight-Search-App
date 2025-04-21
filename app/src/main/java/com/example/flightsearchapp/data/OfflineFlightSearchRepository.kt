package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightSearchRepository(private val airportDao: AirportDao, private val favoriteDao: FavoriteDao): FlightSearchRepository {
    override fun getFavoriteFlightsStream(): Flow<List<Favorite>> = favoriteDao.getFavoriteFlights()
    override fun getAllAirportsStream(): Flow<List<Airport>> = airportDao.getAllAirports()
    override fun getMatchingAirportsStream(searchKey: String): Flow<List<Airport>> = airportDao.getMatchingAirports(searchKey)
    override suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insert(favorite)
    override suspend fun deleteFavorite(favorite: Favorite) = favoriteDao.delete(favorite )
}