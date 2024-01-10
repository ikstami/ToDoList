package com.bignerdranch.android.todolist

import com.bignerdranch.android.todolist.database.TaskDatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.lang.Exception
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "Tasks"
class TaskRepository private constructor(context: Context) {
        private val database: TaskDatabase = Room
            .databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                DATABASE_NAME
            )
            .build()
        private val listDao = database.taskDao()
        private val executor = Executors.newSingleThreadExecutor()
        fun getLists(): LiveData<List<Task>> = listDao.getTasks()

        fun getList(id: UUID): LiveData<Task?> = listDao.getTask(id)

        fun addList(list: Task) {
            executor.execute {
                listDao.addList(list)
            }
        }

        fun updateList(list: Task) {
            executor.execute {
                listDao.updateList(list)
            }
        }
        fun deleteList(id: UUID) {
            executor.execute {
                listDao.deleteList(id)
            }
        }

        companion object {
            private var INSTANCE: TaskRepository? = null
            fun initialize(context: Context) {
                if (INSTANCE == null) {
                    INSTANCE =
                        TaskRepository(context)
                }
            }
            fun get(): TaskRepository {
                return INSTANCE ?:
                throw
                IllegalStateException("TaskRepository must be initialized")
            }
        }

    }
