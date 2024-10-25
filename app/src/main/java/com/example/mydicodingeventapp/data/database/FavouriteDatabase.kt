package com.example.mydicodingeventapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteEvent::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouriteDao

    companion object {
        @Volatile
        private var INSTANCE: FavouriteDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavouriteDatabase {
            if (INSTANCE == null) {
                synchronized(FavouriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavouriteDatabase::class.java, "favourite_event_database"
                    )
                        .build()
                }
            }
            return INSTANCE as FavouriteDatabase

        }
    }
}
