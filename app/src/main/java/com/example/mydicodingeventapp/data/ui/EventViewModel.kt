package com.example.mydicodingeventapp.data.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mydicodingeventapp.data.retrofit.ApiConfig
import com.example.mydicodingeventapp.data.response.EventResponse
import com.example.mydicodingeventapp.data.response.ListEventsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel : ViewModel() {
    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> get() = _events

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents


    fun loadUpcomingEvent() {
        val apiService = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            // Make the API call
            apiService.getEvent(active = 1).enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { eventResponse ->
                            _upcomingEvents.postValue(eventResponse.listEvents)
                        }
                        //atau bisa begini
                        //_upcomingEvents.value = response.body()?.listEvents

                    } else {
                        Log.e("OnFailure: ", response.message())
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {

                    Log.e("OnFailure: ", "${t.message}")
                }
            })
        }
    }

    fun finishedEvent() {
        val apiService = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {

            apiService.getEvent(active = 0 ).enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { eventResponse ->
                            _finishedEvents.postValue(eventResponse.listEvents)
                        }
                    } else {
                        Log.e("OnFailure: ", response.message())
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {

                    Log.e("OnFailure: ", "${t.message}")
                }
            })
        }
    }

    fun searchUpcomingEvents(query: String) {
        if (query.isBlank()) {
            _events.postValue(emptyList())
            return
        }
        val apiService = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getEvent(active = 1, query = query).enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { eventResponse ->
                            if (eventResponse.listEvents.isEmpty()) {
                                _toastMessage.postValue("Event tidak ditemukan")
                            }
                            _events.postValue(eventResponse.listEvents)
                        } ?: run {
                            _events.postValue(emptyList())
                        }
                    } else {
                        _events.postValue(emptyList())
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    Log.e("searchEvents", "Failed to fetch events: ${t.message}", t)
                    _events.postValue(emptyList())
                }
            })
        }
    }

    fun searchFinishedEvents(query: String) {
        if (query.isBlank()) {
            _events.postValue(emptyList())
            return
        }

        val apiService = ApiConfig.getApiService()
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getEvent(active = 0, query = query).enqueue(object : Callback<EventResponse> {
                override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let { eventResponse ->
                            if (eventResponse.listEvents.isEmpty()) {
                                _toastMessage.postValue("Event tidak ditemukan")
                            }
                            _events.postValue(eventResponse.listEvents)
                        } ?: run {
                            _events.postValue(emptyList())
                        }
                    } else {
                        _events.postValue(emptyList())
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    Log.e("OnFailure: ", "${t.message}")
                    _toastMessage.postValue("Error: ${t.message}")
                }
            })
        }
    }

    fun resetToastMessage() {
        _toastMessage.value = null
    }




}