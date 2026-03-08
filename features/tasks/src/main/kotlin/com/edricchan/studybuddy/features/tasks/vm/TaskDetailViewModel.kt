package com.edricchan.studybuddy.features.tasks.vm

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.data.repo.TaskRepository
import com.edricchan.studybuddy.features.tasks.data.repo.setCompletion
import com.edricchan.studybuddy.features.tasks.data.repo.toggleArchived
import com.edricchan.studybuddy.features.tasks.data.repo.toggleCompleted
import com.edricchan.studybuddy.features.tasks.detail.data.TaskDetailData
import com.edricchan.studybuddy.features.tasks.detail.data.impl.FirestoreTaskDetailData
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.snackbar.SnackBarController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val repo: TaskRepository,
    detailDataFactory: FirestoreTaskDetailData.Factory,
    savedState: SavedStateHandle,
    snackBarController: SnackBarController
) : ViewModel(), TaskDetailData, SnackBarController by snackBarController {
    private companion object {
        private const val TAG = "TaskDetailViewModel"
    }

    private val routeData = savedState.toRoute<CompatDestination.Task.View>()

    override val currentTaskId: String = routeData.taskId

    private val detailData =
        detailDataFactory.create(selectedTaskId = routeData.taskId, coroutineScope = viewModelScope)

    override val currentTaskStateFlow: StateFlow<TaskDetailState> by detailData::currentTaskStateFlow

    suspend fun toggleCompleted(item: TaskItem) {
        repo.toggleCompleted(item)
    }

    fun onToggleComplete() {
        viewModelScope.launch {
            val task = currentTask()
            try {
                toggleCompleted(task)
            } catch (e: Exception) {
                showSnackBar(
                    if (!task.isCompleted) R.string.task_done_fail_msg
                    else R.string.task_undone_fail_msg,
                    SnackBarData.Duration.Long
                )
                Log.e(
                    TAG,
                    "An error occurred while marking the task as " +
                        if (!task.isCompleted) "done" else "undone",
                    e
                )
            }
        }
    }

    fun onCompletedChange(isCompleted: Boolean) {
        viewModelScope.launch {
            repo.setCompletion(currentTaskId, isCompleted)
        }
    }

    private val _pendingDelete = MutableStateFlow(false)
    val pendingDelete = _pendingDelete.asStateFlow()

    suspend fun deleteTask() {
        repo.deleteTaskById(currentTaskId)
    }

    fun onDeleteTask() {
        viewModelScope.launch {
            try {
                // We use a StateFlow here to store a pending deletion instead of showing the
                // snack-bar directly after the deleteTask() call as the fragment's
                // setTaskData method would have triggered immediately with an emitted NoData
                // state after the task is deleted, which would navigate away before it
                // could ever trigger
                _pendingDelete.value = true
                deleteTask()
            } catch (e: Exception) {
                showSnackBar(
                    R.string.task_delete_fail_msg,
                    SnackBarData.Duration.Long
                )
                Log.e(
                    TAG,
                    "onDeleteTask: An error occurred while deleting the task.",
                    e
                )
            }
        }
    }

    suspend fun toggleArchived(item: TaskItem) {
        repo.toggleArchived(item)
    }

    fun onToggleArchived() {
        viewModelScope.launch {
            val task = currentTask()
            try {
                toggleArchived(task)
            } catch (e: Exception) {
                showSnackBar(
                    if (task.isArchived) R.string.task_archive_fail_msg
                    else R.string.task_unarchive_fail_msg,
                    SnackBarData.Duration.Long
                )
                Log.e(
                    TAG,
                    "An error occurred while attempting to archive the task:",
                    e
                )
            }
        }
    }
}
