package com.edricchan.studybuddy.features.tasks.list.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.domain.repo.toggleCompleted
import com.edricchan.studybuddy.features.tasks.domain.usecase.GetPagedTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(
    private val repository: TaskRepository,
    getPagedTasks: GetPagedTasksUseCase
) : ViewModel() {
    val pagedTasks = getPagedTasks(viewModelScope)

    val refreshRequests: ReceiveChannel<Unit>
        field = Channel<Unit>()

    fun requestRefresh() {
        refreshRequests.trySend(Unit)
    }

    /** Toggles and updates the specified [task][item]'s [done][TaskItem.isCompleted] status. */
    suspend fun toggleTaskDone(item: TaskItem) {
        repository.toggleCompleted(item)
    }

    fun onToggleTaskDone(item: TaskItem) {
        viewModelScope.launch {
            toggleTaskDone(item)
        }
    }

    /** Removes the specified [task]. */
    suspend fun removeTask(task: TaskItem) {
        repository.deleteTask(task)
    }

    var pendingDeleteTask by mutableStateOf<TaskItem?>(null)
        private set

    fun requestDeleteTask(item: TaskItem) {
        pendingDeleteTask = item
    }

    fun cancelDeleteTask() {
        pendingDeleteTask = null
    }

    fun confirmDeleteTask() {
        pendingDeleteTask?.let { item ->
            viewModelScope.launch {
                removeTask(item)
            }
        } ?: run {
            Log.w(TAG, "confirmDeleteTask was requested but there is no pending item to delete")
        }
    }
}
