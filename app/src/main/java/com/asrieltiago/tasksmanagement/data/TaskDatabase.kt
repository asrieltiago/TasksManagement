package com.asrieltiago.tasksmanagement.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asrieltiago.tasksmanagement.data.dao.TaskDao
import com.asrieltiago.tasksmanagement.data.model.Task

@Database(
    entities = [Task::class],
    version = 2
)
abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDao
}