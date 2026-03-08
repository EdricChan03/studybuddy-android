package com.edricchan.studybuddy.features.tasks.detail.data

import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

/** Data to be used by detail-related functionality (viewing and updating). */
interface TaskDetailData {
    /** The currently selected [TodoItem]'s ID. */
    val currentTaskId: String

    /** The currently selected task data, as a [StateFlow] of [TaskDetailState]. */
    val currentTaskStateFlow: StateFlow<TaskDetailState>

    /**
     * Awaits the [currentTaskStateFlow] state flow until the first
     * [TaskDetailState.Success] item is emitted, returning its [TaskDetailState.Success.item]
     * value.
     */
    suspend fun currentTask(): TaskItem {
        return currentTaskStateFlow.filterIsInstance<TaskDetailState.Success>()
            .first().item
    }
}

/**
 * Awaits the [TaskDetailData.currentTaskStateFlow] state flow until the first
 * [TaskDetailState.Success] item is emitted, returning its [TaskDetailState.Success.item]
 * value mapped to the result of [onTask].
 */
suspend inline fun <R> TaskDetailData.mapCurrentTask(onTask: (TaskItem) -> R): R {
    return currentTask().let(onTask)
}
