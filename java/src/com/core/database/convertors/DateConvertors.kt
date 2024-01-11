package com.core.database.convertors

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConvertors {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    @TypeConverter
    fun toTimeMillis(date : String): String {
        return dateFormat.parse(date).time.toString()
    }
}