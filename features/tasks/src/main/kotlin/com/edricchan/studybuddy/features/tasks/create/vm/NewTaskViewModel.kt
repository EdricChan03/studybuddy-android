package com.edricchan.studybuddy.features.tasks.create.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    fun submitTask(
        item: TodoItem,
        onSuccess: (DocumentReference) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            repository.runCatching { addTask(item) }
                .onSuccess(onSuccess)
                .onFailure(onFailure)
        }
    }
}
