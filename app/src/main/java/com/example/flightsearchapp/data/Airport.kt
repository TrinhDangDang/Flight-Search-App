package com.example.flightsearchapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val iata_code: String,
    val passengers: Int
)
