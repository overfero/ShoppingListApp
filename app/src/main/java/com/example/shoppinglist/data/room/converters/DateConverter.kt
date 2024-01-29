package com.example.shoppinglist.data.room.converters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date

open class DateConverter {

    @TypeConverter
    fun toDate(date: Long?): Date?{
        return date?.let{Date(it)}
    }

    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }
}

fun formatDate(date: Date): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    return sdf.format(date)
}