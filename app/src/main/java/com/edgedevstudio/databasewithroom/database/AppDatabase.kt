package com.edgedevstudio.databasewithroom.database

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Created by Olorunleke Opeyemi on 18/01/2019.
 */

@Database(entities = arrayOf(TaskEntry::class), version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

    companion object {

        private val DATABASE_NAME = "todolist"
        private val LOCK = Any()
        private var sInstance: AppDatabase? = null


        fun getsInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                synchronized(LOCK) {
                    Log.d(TAG, "getsInstance: Creating a New Database")
                    sInstance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        AppDatabase.DATABASE_NAME
                    )
//                        .allowMainThreadQueries()
                        .build()
                }

            }
            Log.d(TAG, "getsInstance: Getting Database Instance")
            return sInstance!!
        }
    }
}
