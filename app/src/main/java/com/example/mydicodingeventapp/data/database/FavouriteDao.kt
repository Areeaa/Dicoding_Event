package com.example.mydicodingeventapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favouriteEvent: FavouriteEvent)

    @Delete
    fun delete(favouriteEvent: FavouriteEvent)

    @Query("SELECT * FROM FavouriteEvent WHERE id = :id")
    fun getFavouriteEventById(id: String): LiveData<FavouriteEvent?>

    @Query("SELECT * FROM FavouriteEvent")
    fun getAllFavoriteEvent(): LiveData<List<FavouriteEvent>>

}

