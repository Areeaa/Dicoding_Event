package com.example.mydicodingeventapp.data.ui

import FavouriteRepository
import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mydicodingeventapp.data.database.FavouriteEvent
import com.example.mydicodingeventapp.data.response.DetailEventResponse
import com.example.mydicodingeventapp.data.response.Event
import com.example.mydicodingeventapp.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _event = MutableLiveData<Event?>()
    val event: LiveData<Event?> get() = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val mFavouriteRepository: FavouriteRepository = FavouriteRepository(application)

    fun loadDetail(id: String) {
        _isLoading.postValue(true)
        val apiService = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getDetailEvent(id).enqueue(object : Callback<DetailEventResponse> {
                override fun onResponse(call: Call<DetailEventResponse>, response: Response<DetailEventResponse>) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        response.body()?.event?.let { event ->
                            _event.postValue(event)
                        }
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()} ")
                    }
                }

                override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    Log.e("OnFailure", "${t.message}")
                }
            })
        }
    }




    fun insert(favouriteEvent: FavouriteEvent) {
        mFavouriteRepository.insert(favouriteEvent)
    }

    fun delete(favouriteEvent: FavouriteEvent){
        mFavouriteRepository.delete(favouriteEvent)
    }

    fun getFavouriteEventById(id: String): LiveData<FavouriteEvent?> {
        return mFavouriteRepository.getFavouriteEventById(id)
    }
}
