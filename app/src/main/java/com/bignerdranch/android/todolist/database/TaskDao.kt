package com.bignerdranch.android.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.UUID
import com.bignerdranch.android.todolist.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM Task ORDER BY priority")
    fun getTasks(): LiveData<List<Task>>
    @Query("SELECT * FROM Task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Update
    fun updateList(list: Task)
    @Insert
    fun addList(list: Task)
    @Query("DELETE FROM Task WHERE id=(:id)")
    fun deleteList(id: UUID)
}