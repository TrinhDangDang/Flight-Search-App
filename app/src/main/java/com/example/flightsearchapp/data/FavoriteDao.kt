package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoriteDao {

    @Query("SELECT * from favorite")
    fun getFavoriteFlights(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

//    @Query("DELETE FROM favorite WHERE departure_code = :departureCode AND destination_code = :destinationCode")
//    suspend fun deleteByCodes(departureCode: String, destinationCode: String)

    @Delete
    suspend fun delete(favorite: Favorite)
}