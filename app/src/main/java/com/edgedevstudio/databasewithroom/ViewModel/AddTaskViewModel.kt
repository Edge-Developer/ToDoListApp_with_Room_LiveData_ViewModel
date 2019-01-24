package com.edgedevstudio.databasewithroom.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.edgedevstudio.databasewithroom.database.AppDatabase
import com.edgedevstudio.databasewithroom.database.TaskEntry

/**
 * Created by Olorunleke Opeyemi on 20/01/2019.
 **/
class AddTaskViewModel(database: AppDatabase, taskId : Int) : ViewModel() {

    val task : LiveData<TaskEntry>

    init {
        task = database.tasksDao().loadTaskById(taskId)
    }


}