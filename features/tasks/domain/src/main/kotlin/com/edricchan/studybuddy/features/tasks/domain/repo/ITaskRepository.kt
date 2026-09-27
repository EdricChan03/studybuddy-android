package com.edricchan.studybuddy.features.tasks.domain.repo

import androidx.paging.PagingData
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskItemInput
import com.edricchan.studybuddy.features.tasks.domain.model.update.UpdateTaskItemInput
import kotlinx.coroutines.flow.Flow

/** Repository interface for CRUD operations related to the tasks feature. */
interface ITaskRepository {
    /** Gets the task by its [id] as a [Flow]. */
    fun observeTaskById(id: String): Flow<TaskItem?>

    /**
     * Gets a paginated list of tasks.
     * @param config Configuration options specifying how the items should be paginated,
     * as well as filtering options. (See [TasksPaginationConfig])
     */
    fun observeTasks(
        config: TasksPaginationConfig
    ): Flow<PagingData<TaskItem>>

    /** Adds the specified task to the database. */
    suspend fun addTask(input: CreateTaskItemInput)

    /** Updates the specified task with the given [valueMap]. */
    @Deprecated("Use the type-safe overload which uses FieldValue rather than a Map")
    suspend fun updateTask(id: String, valueMap: Map<TaskItem.Field, Any?>)

    /** Updates the specified task with the given list of [values]. */
    @Deprecated("Use the overload which accepts a concrete input data class")
    suspend fun updateTask(id: String, vararg values: TaskItem.FieldValue<*>)

    /** Updates the specified task with the given [input]. */
    suspend fun updateTask(id: String, input: UpdateTaskItemInput)

    /** Update the specified tasks with the given list of [values]. */
    @Deprecated("Use the overload which accepts a concrete input data class")
    suspend fun updateTasks(ids: Set<String>, vararg values: TaskItem.FieldValue<*>)

    /** Updates the specified tasks with the given [input]. */
    suspend fun updateTasks(ids: Set<String>, input: UpdateTaskItemInput)

    /** Deletes the specified task from the database. */
    suspend fun deleteTaskById(id: String)

    /** Deletes the specified task from the database. */
    suspend fun deleteTask(task: TaskItem) {
        deleteTaskById(task.id)
    }

    /** Bulk deletes the specified [taskIds] from the database. */
    suspend fun deleteTasksById(taskIds: Set<String>)

    /** Bulk deletes the specified [tasks] from the database. */
    suspend fun deleteTasks(tasks: Set<TaskItem>) {
        deleteTasksById(tasks.map { it.id }.toSet())
    }
}
