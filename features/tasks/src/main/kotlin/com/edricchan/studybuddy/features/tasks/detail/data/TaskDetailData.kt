package com.edricchan.studybuddy.features.tasks.detail.data

import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

/** Data to be used by detail-related functionality (viewing and updating). */
interface TaskDetailData {
    /** The currently selected [TodoItem]'s ID. */
    val currentTaskId: String

    /** The currently selected [TodoItem]'s data. (Alias of `currTaskFlow.value`) */
    @Deprecated(
        "This may return the initial `null` value if the underlying flow" +
            "hasn't started emitting values. Use the currentTask() suspend method " +
            "instead to get the current value (note that it returns the domain-specific " +
            "TaskItem data class)."
    )
    val currentTask: TodoItem?
        get() = currTaskFlow.value

    /** The currently selected [TodoItem]'s data, as a [StateFlow]. */
    @Deprecated(
        "There is an ambiguity between having no such task item " +
            "existing (represented by `null`) and the initial `null` value when the underlying " +
            "flow has yet to start emitting values. Use the currentTaskStateFlow property " +
            "instead which uses a sealed interface to differentiate between these 2 possible " +
            "states. Note that the state class uses the domain-specific TaskItem data class " +
            "which also provides access to the project-related information, if any."
    )
    val currTaskFlow: StateFlow<TodoItem?>

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

    /** The currently selected [TodoItem]'s [TodoProject] data, as a [StateFlow]. */
    @Deprecated(
        "The project data is now available in the TaskItem domain class. " +
            "To get the current task data, use the currentTaskStateFlow property"
    )
    val currTaskProjectFlow: StateFlow<TodoProject?>
}
