package com.edricchan.studybuddy.features.tasks.detail.data

import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import kotlinx.coroutines.flow.StateFlow

/** Data to be used by detail-related functionality (viewing and updating). */
interface TaskDetailData {
    /** The currently selected [TodoItem]'s ID. */
    val currentTaskId: String

    /** The currently selected [TodoItem]'s data. (Alias of `currTaskFlow.value`) */
    val currentTask: TodoItem?
        get() = currTaskFlow.value

    /** The currently selected [TodoItem]'s data, as a [StateFlow]. */
    val currTaskFlow: StateFlow<TodoItem?>

    /** The currently selected [TodoItem]'s [TodoProject] data, as a [StateFlow]. */
    val currTaskProjectFlow: StateFlow<TodoProject?>
}
