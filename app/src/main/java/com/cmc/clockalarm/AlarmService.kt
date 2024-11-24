package com.cmc.clockalarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.cmc.clockalarm.R

class AlarmService : Service() {

    private lateinit var alarmSound: MediaPlayer


    companion object {
        const val CHANNEL_ID = "AlarmChannelID"
        const val CHANNEL_NAME = "Alarm Notifications"
        const val ACTION_START_ALARM = "com.cmc.clockalarm.ACTION_START_ALARM"
        const val ACTION_STOP_ALARM = "com.cmc.clockalarm.ACTION_STOP_ALARM"
        const val ACTION_SNOOZE_ALARM = "com.cmc.clockalarm.ACTION_SNOOZE_ALARM"
        const val SNOOZE_DURATION = 5 * 60 * 1000L
    }

    override fun onCreate() {
        super.onCreate()
        alarmSound = MediaPlayer.create(this, R.raw.alarm_sound) // Add a sound file in res/raw
        alarmSound.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_ALARM -> {
                startForegroundService()
                alarmSound.start()
            }
            ACTION_STOP_ALARM -> {
                alarmSound.stop()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText("Your alarm is ringing!")
            .setSmallIcon(R.drawable.ic_alarm) // Add a drawable icon in res/drawable
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        startForeground(1, notification)
    }

    override fun onDestroy() {
        alarmSound.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
