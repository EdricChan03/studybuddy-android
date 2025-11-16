package com.edricchan.studybuddy.features.tasks.domain.repo

import androidx.paging.PagingData
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

/** Repository interface for CRUD operations related to the tasks feature. */
interface ITaskRepository {
    /** Gets the task by its [id] as a [Flow]. */
    fun observeTaskById(id: String): Flow<TaskItem?>

    /**
     * Configuration options for [observeTasks].
     * @property includeArchived Whether archived tasks should be included in the list.
     * @property excludeCompleted Whether completed tasks should be excluded from the list.
     * @property pageSize Number of tasks to be shown per page.
     * @property orderByField The [TaskItem.Field] to order the list of tasks by.
     */
    data class PaginationConfig(
        val includeArchived: Boolean = false,
        val excludeCompleted: Boolean = false,
        val pageSize: Int = 30,
        val orderByField: TaskItem.Field = TaskItem.Field.CreatedAt
    )

    /**
     * Gets a paginated list of tasks.
     */
    fun observeTasks(
        config: PaginationConfig
    ): Flow<PagingData<TaskItem>>

    /** Adds the specified task to the database. */
    suspend fun addTask(task: TaskItem)

    /** Updates the specified task with the given [valueMap]. */
    suspend fun updateTask(id: String, valueMap: Map<TaskItem.Field, Any?>)

    /** Updates the specified task with the given list of [values]. */
    suspend fun updateTask(id: String, vararg values: TaskItem.FieldValue<*>) {
        updateTask(id = id, valueMap = values.associate { it.field to it.value })
    }

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
