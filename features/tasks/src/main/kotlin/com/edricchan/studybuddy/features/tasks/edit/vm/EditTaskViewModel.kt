package com.edricchan.studybuddy.features.tasks.edit.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val repo: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    sealed interface TaskState {
        data object Loading : TaskState
        data class Success(val item: TaskItem) : TaskState
        data object DoesNotExist : TaskState
    }

    val taskId = savedStateHandle.toRoute<CompatDestination.Task.Edit>().taskId

    val taskDetail = repo.observeTaskById(taskId)
        .map {
            if (it == null) return@map TaskState.DoesNotExist
            TaskState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TaskState.Loading
        )

    fun updateTask(
        data: Map<TaskItem.Field, Any>,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            repo.runCatching { updateTask(taskId, data) }
                .onSuccess { onSuccess() }
                .onFailure(onFailure)
        }
    }
}
