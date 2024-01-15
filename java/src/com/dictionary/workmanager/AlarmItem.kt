package com.dictionary.workmanager


data class AlarmItem(
    val message : String,
    val alarmHour:Int,
    val alarmMin: Int,
    val nextDay:Boolean = true
)