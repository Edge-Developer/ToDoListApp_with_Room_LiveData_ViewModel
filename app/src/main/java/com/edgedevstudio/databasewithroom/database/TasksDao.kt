package com.edgedevstudio.databasewithroom.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by Olorunleke Opeyemi on 18/01/2019.
 */

@Dao
interface TasksDao {

    @Query("SELECT * FROM task ORDER BY priority")
    fun loadAllTask(): LiveData<List<TaskEntry>>

    @Insert
    fun insertTask(taskEntry: TaskEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)

    @Query("SELECT * from task WHERE id = :id")
    fun loadTaskById(id: Int): LiveData<TaskEntry>
}
