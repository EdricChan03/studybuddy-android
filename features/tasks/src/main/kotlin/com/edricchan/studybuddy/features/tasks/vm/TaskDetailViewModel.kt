package com.edricchan.studybuddy.features.tasks.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.toggleArchived
import com.edricchan.studybuddy.features.tasks.data.repo.toggleCompleted
import com.edricchan.studybuddy.features.tasks.data.repo.update
import com.edricchan.studybuddy.features.tasks.detail.data.TaskDetailData
import com.edricchan.studybuddy.features.tasks.detail.data.impl.FirestoreTaskDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repo: TaskRepository,
    detailDataFactory: FirestoreTaskDetailData.Factory,
    savedState: SavedStateHandle
) : ViewModel(), TaskDetailData {
    private val routeData = savedState.toRoute<CompatDestination.Task.View>()

    override val currentTaskId: String = routeData.taskId

    private val detailData =
        detailDataFactory.create(selectedTaskId = routeData.taskId, coroutineScope = viewModelScope)

    @Suppress("DEPRECATION") // Deprecation usage from FirestoreDetailData::currTaskFlow
    @Deprecated(
        "There is an ambiguity between having no such task item actually " +
            "existing (represented by `null`) and the initial `null` value when the underlying " +
            "flow has yet to start emitting values. Use the currentTaskStateFlow property " +
            "instead which uses a sealed interface to differentiate between these 2 possible " +
            "states. Note that the state class uses the domain-specific TaskItem data class " +
            "which also provides access to the project-related information, if any."
    )
    override val currTaskFlow: StateFlow<TodoItem?> by detailData::currTaskFlow
    override val currentTaskStateFlow: StateFlow<TaskDetailState> by detailData::currentTaskStateFlow

    @Suppress("DEPRECATION") // Deprecation usage from FirestoreDetailData::currTaskProjectFlow
    @Deprecated(
        "The project data is now available in the TaskItem domain class. " +
            "To get the current task data, use the currentTaskStateFlow property"
    )
    override val currTaskProjectFlow: StateFlow<TodoProject?> by detailData::currTaskProjectFlow

    suspend fun toggleCompleted(item: TodoItem) {
        repo.toggleCompleted(item)
    }

    fun onToggleCompleted(item: TodoItem) {
        viewModelScope.launch {
            toggleCompleted(item)
        }
    }

    suspend fun delete(item: TodoItem) {
        repo.removeTask(item)
    }

    suspend fun deleteTask() {
        repo.deleteTaskById(currentTaskId)
    }

    suspend fun archive(isArchived: Boolean) {
        repo.update(currentTaskId) {
            this[TodoItem.Field.IsArchived] = isArchived
        }
    }

    fun toggleArchived(item: TodoItem) {
        viewModelScope.launch {
            repo.toggleArchived(item)
        }
    }
}
