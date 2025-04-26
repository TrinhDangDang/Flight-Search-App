package com.example.flightsearchapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = false)
abstract class AirportDatabase: RoomDatabase(){
    abstract fun airportDao(): AirportDao
    abstract fun favoriteDao(): FavoriteDao

    companion object{
        @Volatile
        private var Instance: AirportDatabase? = null

        fun getDatabase(context: Context): AirportDatabase {
            return Instance ?: synchronized(this) {
                val dbFile = context.getDatabasePath("flight_search.db")

                if (!dbFile.exists()) {
                    // If the database does not exist yet, copy it manually
                    context.assets.open("database/flight_search.db").use { inputStream ->
                        dbFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }
                }

                Room.databaseBuilder(context, AirportDatabase::class.java, "flight_search.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }


    }
}
