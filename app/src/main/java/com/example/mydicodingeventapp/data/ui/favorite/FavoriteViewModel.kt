package com.example.mydicodingeventapp.data.ui.favorite

import FavouriteRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mydicodingeventapp.data.database.FavouriteEvent

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private val mFavouriteRepository: FavouriteRepository = FavouriteRepository(application)


    fun getFavoriteEvents(): LiveData<List<FavouriteEvent>> {
        return mFavouriteRepository.getAllFavoriteEvents()
    }

}