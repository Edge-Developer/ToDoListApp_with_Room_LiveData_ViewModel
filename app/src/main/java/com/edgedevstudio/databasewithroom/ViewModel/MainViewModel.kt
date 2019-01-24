package com.edgedevstudio.databasewithroom.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.edgedevstudio.databasewithroom.database.AppDatabase
import com.edgedevstudio.databasewithroom.database.TaskEntry

/**
 * Created by Olorunleke Opeyemi on 20/01/2019.
 **/
class MainViewModel(app : Application): AndroidViewModel(app){

    val TAG = "MainActivity"
    val tasks : LiveData<List<TaskEntry>>

    init {
        val database = AppDatabase.getsInstance(this.getApplication())
        tasks = database.tasksDao().loadAllTask()
        Log.d(TAG, "Actively Retrieving Tasks from the Database")
    }
}