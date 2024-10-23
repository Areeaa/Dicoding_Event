package com.example.mydicodingeventapp.data.retrofit

import com.example.mydicodingeventapp.data.response.DetailEventResponse
import com.example.mydicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") active: Int,
        @Query("q") query: String? = null
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: String
    ): Call<DetailEventResponse>


}