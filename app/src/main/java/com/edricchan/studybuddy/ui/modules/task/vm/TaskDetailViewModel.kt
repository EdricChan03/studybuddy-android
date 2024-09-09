package com.edricchan.studybuddy.ui.modules.task.vm

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
import com.edricchan.studybuddy.ui.modules.task.detail.TaskDetailData
import com.edricchan.studybuddy.ui.modules.task.detail.impl.FirestoreTaskDetailData
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

    override val currTaskFlow: StateFlow<TodoItem?> by detailData::currTaskFlow
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
        currTaskFlow.value?.let { repo.removeTask(it) }
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
