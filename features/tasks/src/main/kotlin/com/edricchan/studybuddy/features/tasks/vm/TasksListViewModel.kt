package com.edricchan.studybuddy.features.tasks.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.core.settings.tasks.repo.TasksSettingsRepository
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.toggleCompleted
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.toDomain
import com.edricchan.studybuddy.features.tasks.domain.repo.TasksPaginationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksListViewModel @Inject constructor(
    private val repository: TaskRepository,
    settingsRepo: TasksSettingsRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedTasks = settingsRepo.filterOptions.flatMapLatest {
        val ordering = it.orderByFields.mapKeysTo(LinkedHashMap()) { (field, dir) ->
            field.protoValue.toDomain()
        }

        // TODO: Hook up ordering support when we migrate off of Firestore. (Firestore requires a
        //   composite index to be added for *every* new sort-direction order that would be added
        //   to a query)
        repository.observeTasks(
            TasksPaginationConfig(
                cachedCoroutineScope = viewModelScope
            )
        )
    }

    val refreshRequests: ReceiveChannel<Unit>
        field = Channel<Unit>()

    fun requestRefresh() {
        refreshRequests.trySend(Unit)
    }

    /** Toggles and updates the specified [task][item]'s [done][TodoItem.done] status. */
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
