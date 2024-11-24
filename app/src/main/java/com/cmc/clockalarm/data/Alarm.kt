package com.cmc.clockalarm.data

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    var isEnabled: Boolean = true
)
