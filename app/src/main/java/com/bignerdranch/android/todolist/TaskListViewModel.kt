package com.bignerdranch.android.todolist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.todolist.Task
import com.bignerdranch.android.todolist.TaskRepository
import java.lang.Exception
import java.util.UUID

class TaskListViewModel : ViewModel() {
    private val listRepository = TaskRepository.get()
    val taskLiveData  = listRepository.getLists()
    fun addList(task: Task) {
        try {
            listRepository.addList(task)
        }
        catch(e: Exception)
        {
            Log.d("YourTag", "Result: addList")
        }
    }
    fun deleteList(taskId: UUID) {
        listRepository.deleteList(taskId)
    }
}
