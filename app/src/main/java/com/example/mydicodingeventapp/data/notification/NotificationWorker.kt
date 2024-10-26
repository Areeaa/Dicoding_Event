package com.example.mydicodingeventapp.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mydicodingeventapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Fetch nearest active event
            val event = fetchNearestEvent()
            if (event != null) {
                // Show notification
                showNotification(event)
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private suspend fun fetchNearestEvent(): Event? {
        return withContext(Dispatchers.IO) {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://event-api.dicoding.dev/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(ApiService::class.java)
                val response = apiService.getNearestEvent()

                if (response.isSuccessful) {
                    response.body()?.data?.firstOrNull()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun showNotification(event: Event) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "event_reminder_channel"
        val channelName = "Event Reminder"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true)
                enableVibration(true)
                description = "Daily reminder for upcoming events"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Format event date
        val eventDate = event.date?.let { date ->
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).let { parser ->
                parser.parse(date)?.let { parsedDate ->
                    SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault()).format(parsedDate)
                }
            }
        } ?: "Date not specified"

        // Create notification
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.calendar_clock) // Make sure to create this icon
            .setContentTitle("Upcoming Event: ${event.name}")
            .setContentText("Event starts at $eventDate")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // munculkan notifikasi
        notificationManager.notify(1, notification)
    }

    companion object {
        // API interface
        interface ApiService {
            @GET("events")
            suspend fun getNearestEvent(
                @Query("active") active: Int = -1,
                @Query("limit") limit: Int = 1
            ): Response<EventResponse>
        }

        // data class api
        data class EventResponse(
            val error: Boolean,
            val message: String,
            val data: List<Event>
        )

        data class Event(
            val id: String,
            val name: String,
            val date: String?,
            val description: String?,
            val status: String?,
            val image: String?
        )
    }
}