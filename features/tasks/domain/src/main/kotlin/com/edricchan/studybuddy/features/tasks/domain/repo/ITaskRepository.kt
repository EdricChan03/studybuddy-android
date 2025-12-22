package com.edricchan.studybuddy.features.tasks.domain.repo

import androidx.paging.PagingData
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
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
