package com.bignerdranch.android.todolist


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.UUID

class TaskDetailViewModel(): ViewModel(){
    private val listRepository = TaskRepository.get()
    private val listIdLiveData = MutableLiveData<UUID>()
    var taskLiveData: LiveData<Task?> =
        Transformations.switchMap(listIdLiveData) { listId ->
            listRepository.getList(listId)
        }
    fun loadList(listId: UUID) {
        listIdLiveData.value = listId
    }

    fun saveList(list: Task) {
        listRepository.updateList(list)
    }
}


