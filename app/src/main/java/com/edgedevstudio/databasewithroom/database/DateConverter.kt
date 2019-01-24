package com.edgedevstudio.databasewithroom.database

import androidx.room.TypeConverter

import java.util.Date

/**
 * Created by Olorunleke Opeyemi on 18/01/2019.
 */
class DateConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
