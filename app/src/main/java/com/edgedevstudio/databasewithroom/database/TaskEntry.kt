package com.edgedevstudio.databasewithroom.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import java.util.Date

/**
 * Created by Olorunleke Opeyemi on 18/01/2019.
 */

/*
The @Entity will generate a table called 'task' with
each member variables being columns
* */
@Entity(tableName = "task")
class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var description: String? = null
    var priority: Int = 0
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date? = null

   /*
    @Ignore anotation tells the room persistence library to ignore
    this constructor when reading from the database
    although this constructor is useful for insert new Data in the database
    */
    @Ignore
    constructor(description: String, priority: Int, updatedAt: Date) {
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }

    // the room persistence library uses this constructor when reading from the database to create Task Entry Objects
    constructor(id: Int, description: String, priority: Int, updatedAt: Date) {
        this.id = id
        this.description = description
        this.priority = priority
        this.updatedAt = updatedAt
    }
}
