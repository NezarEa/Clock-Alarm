package com.cmc.clockalarm

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {

    private lateinit var stopButton: Button
    private lateinit var snoozeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        stopButton = findViewById(R.id.stop_button)
        snoozeButton = findViewById(R.id.snooze_button)

        // Start the alarm service to play the sound
        startAlarmService()

        stopButton.setOnClickListener {
            stopAlarmService()
        }

        snoozeButton.setOnClickListener {
            snoozeAlarm()
        }
    }

    private fun startAlarmService() {
        val serviceIntent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_START_ALARM
        }
        startService(serviceIntent)
    }

    private fun stopAlarmService() {
        val serviceIntent = Intent(this, AlarmService::class.java).apply {
            action = AlarmService.ACTION_STOP_ALARM
        }
        startService(serviceIntent)
        finish() // Close the alarm activity after stopping the alarm
    }

    private fun snoozeAlarm() {
        // Logic for snoozing the alarm
        // For example, delay the stop of the alarm for 5 minutes
        stopAlarmService()

        // Re-start the alarm after snooze time
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startAlarmService()
        }, 300000) // Snooze for 5 minutes (300000ms)
    }
}
