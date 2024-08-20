package com.edricchan.studybuddy.ui.modules.task.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.data.repo.TaskProjectRepository
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.toggleArchived
import com.edricchan.studybuddy.features.tasks.data.repo.toggleCompleted
import com.edricchan.studybuddy.features.tasks.data.repo.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repo: TaskRepository,
    private val projectRepo: TaskProjectRepository,
    savedState: SavedStateHandle
) : ViewModel() {
    private val routeData = savedState.toRoute<CompatDestination.Task.View>()

    val taskId: String = routeData.taskId

    val taskFlow: StateFlow<TodoItem?> =
        repo.observeTask(taskId).stateIn(viewModelScope, SharingStarted.Lazily, null)

    val currentTask by taskFlow::value

    @OptIn(ExperimentalCoroutinesApi::class)
    val taskProjectFlow: Flow<TodoProject?> = taskFlow
        .map { item -> item?.project?.id }
        // Return a flow with null if the ID is null
        .flatMapConcat { id -> id?.let(projectRepo::get) ?: flowOf(null) }

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
        taskFlow.value?.let { repo.removeTask(it) }
    }

    suspend fun archive(isArchived: Boolean) {
        repo.update(taskId) {
            this[TodoItem.Field.IsArchived] = isArchived
        }
    }

    fun toggleArchived(item: TodoItem) {
        viewModelScope.launch {
            repo.toggleArchived(item)
        }
    }
}
