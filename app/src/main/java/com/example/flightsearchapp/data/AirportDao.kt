package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AirportDao {
    @Query("SELECT * from airport ORDER BY name ASC")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("""SELECT * from airport WHERE name LIKE '%' || :searchKey || '%' 
           OR iata_code LIKE '%' || :searchKey || '%'
        ORDER BY name ASC""")
    fun getMatchingAirports(searchKey: String): Flow<List<Airport>>
}