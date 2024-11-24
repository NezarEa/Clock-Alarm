package com.cmc.clockalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmService.ACTION_START_ALARM -> {
                // Start the AlarmService to begin the alarm
                val serviceIntent = Intent(context, AlarmService::class.java).apply {
                    action = AlarmService.ACTION_START_ALARM
                }
                context.startService(serviceIntent)
            }
            AlarmService.ACTION_STOP_ALARM -> {
                // Stop the AlarmService to stop the alarm
                val serviceIntent = Intent(context, AlarmService::class.java).apply {
                    action = AlarmService.ACTION_STOP_ALARM
                }
                context.startService(serviceIntent)
            }
            AlarmService.ACTION_SNOOZE_ALARM -> {
                // Snooze the alarm for 5 minutes
                val serviceIntent = Intent(context, AlarmService::class.java).apply {
                    action = AlarmService.ACTION_SNOOZE_ALARM
                }
                context.startService(serviceIntent)
                Toast.makeText(context, "Alarm snoozed for 5 minutes", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
