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
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.mapNotNull
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

    val taskFlow: Flow<TodoItem?> = repo.observeTask(taskId)

    @OptIn(ExperimentalCoroutinesApi::class)
    val taskProjectFlow: Flow<TodoProject?> = taskFlow
        .mapNotNull { item -> item?.project?.id?.let { projectRepo[it] } }
        .flattenConcat()

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

    suspend fun archive(id: String, isArchived: Boolean) {
        repo.update(id) {
            this[TodoItem.Field.IsArchived] = isArchived
        }
    }

    fun toggleArchived(item: TodoItem) {
        viewModelScope.launch {
            repo.toggleArchived(item)
        }
    }
}
