package com.bignerdranch.android.todolist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.todolist.Task

@Database(entities = [ Task::class ], version=1)
@TypeConverters(TaskTypeConverters::class)

abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}