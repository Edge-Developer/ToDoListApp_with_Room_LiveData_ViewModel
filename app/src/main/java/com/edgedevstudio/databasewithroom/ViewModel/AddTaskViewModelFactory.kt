package com.edgedevstudio.databasewithroom.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edgedevstudio.databasewithroom.database.AppDatabase


/**
 * Created by Olorunleke Opeyemi on 20/01/2019.
 **/
class AddTaskViewModelFactory(val appDatabase: AppDatabase, val taskId: Int) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTaskViewModel(appDatabase, taskId) as T
    }
}