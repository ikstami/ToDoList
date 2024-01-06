package com.bignerdranch.android.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.UUID
class TaskDetailViewModel(): ViewModel() {
    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Task?> =
        Transformations.switchMap(taskIdLiveData) { taskId ->
            taskRepository.getTask(taskId)
        }

    fun loadList(taskId: UUID) {
        taskIdLiveData.value = taskId
    }

    fun saveList(task: Task) {
        taskRepository.updateList(task)
    }
}


