package com.cmc.clockalarm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.app.AlertDialog
import android.content.Context
import com.cmc.clockalarm.data.Alarm
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var alarmAdapter: AlarmAdapter
    private val alarms = mutableListOf<Alarm>()
    private lateinit var alarmManager: android.app.AlarmManager

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        const val CHANNEL_ID = "alarm_channel"
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.SCHEDULE_EXACT_ALARM,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WAKE_LOCK
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager

        setupPermissions()
        createNotificationChannel()
        setupRecyclerView()
        setupFab()
        loadSavedAlarms()
    }

    private fun setupPermissions() {
        val permissionsToRequest = REQUIRED_PERMISSIONS.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSION_REQUEST_CODE)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setBypassDnd(true)
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        alarmAdapter = AlarmAdapter(alarms) { position ->
            deleteAlarm(position)
        }
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = alarmAdapter
        }
    }

    private fun setupFab() {
        findViewById<FloatingActionButton>(R.id.fabAddAlarm).setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                addNewAlarm(hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }

    private fun addNewAlarm(hour: Int, minute: Int) {
        val newAlarm = Alarm(
            id = System.currentTimeMillis().toInt(),
            hour = hour,
            minute = minute
        )
        alarms.add(newAlarm)
        alarms.sortBy { it.hour * 60 + it.minute }
        alarmAdapter.notifyDataSetChanged()
        scheduleAlarm(newAlarm)
        saveAlarms()
    }

    private fun deleteAlarm(position: Int) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_alarm_title))
            .setMessage(getString(R.string.delete_alarm_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                val alarm = alarms[position]
                cancelAlarm(alarm)
                alarms.removeAt(position)
                alarmAdapter.notifyItemRemoved(position)
                saveAlarms()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun scheduleAlarm(alarm: Alarm) {
        // Alarm scheduling implementation
    }

    private fun cancelAlarm(alarm: Alarm) {
        // Alarm cancellation implementation
    }

    private fun loadSavedAlarms() {
        // Load saved alarms implementation
    }

    private fun saveAlarms() {
        // Save alarms implementation
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // All permissions granted, proceed with setup
                createNotificationChannel()
                loadSavedAlarms()
            } else {
                // Handle permission denial
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.permission_required_title))
                    .setMessage(getString(R.string.permission_required_message))
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        setupPermissions()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                        finish()
                    }
                    .show()
            }
        }
    }
}