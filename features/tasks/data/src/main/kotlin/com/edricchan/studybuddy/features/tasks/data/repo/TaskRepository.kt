package com.edricchan.studybuddy.features.tasks.data.repo

import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.data.source.firestore.IDefaultFirestoreDataSource
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val source: IDefaultFirestoreDataSource<@JvmSuppressWildcards TodoItem>
) {
    /** Retrieves the user's list of tasks as a [Flow] of updates. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasksFlow: Flow<List<TodoItem>> = source.items

    /** Retrieves the user's list of tasks given the specified [query]. */
    suspend fun queryTasks(query: QueryMapper): List<TodoItem> = source.findAll(query).first()

    /** Retrieves the user's list of tasks given the specified [query] as a [Flow]. */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeQueryTasks(query: QueryMapper): Flow<List<TodoItem>> = source.findAll(query)

    /** Retrieves the task given its [id] as a [Flow]. */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeTask(id: String): Flow<TodoItem?> = source[id]

    /** Adds the specified [task]. */
    suspend fun addTask(task: TodoItem): DocumentReference = source.add(task)

    /** Removes the specified [task]. */
    suspend fun removeTask(task: TodoItem) = removeTask(task.id)

    /** Removes the specified task given its [id]. */
    suspend fun removeTask(id: String) {
        source.removeById(id)
    }

    /** Bulk removes the specified list of tasks by their [ids]. */
    suspend fun removeTasks(ids: Set<String>) {
        source.runBatch {
            deleteAll(ids)
        }
    }

    /** Update the task (given its [id]) with the specified [data]. */
    suspend fun updateTask(id: String, data: Map<String, Any?>) {
        source.update(id, data)
    }

    /** Updates the list of tasks (given by [ids]) with the specified [data]. */
    suspend fun updateTasks(ids: Set<String>, data: Map<String, Any?>) {
        source.runBatch {
            updateAll(ids, data)
        }
    }
}

suspend fun TaskRepository.update(
    id: String,
    data: Map<TodoItem.Field, Any?>
) {
    updateTask(id, data.mapKeys { it.key.fieldName })
}

suspend fun TaskRepository.update(
    id: String,
    dataAction: MutableMap<TodoItem.Field, Any?>.() -> Unit
) {
    updateTask(id, buildMap(dataAction).mapKeys { it.key.fieldName })
}

/** Toggles the [item]'s [completion status][TodoItem.done]. */
suspend fun TaskRepository.toggleCompleted(item: TodoItem) =
    update(item.id, mapOf(TodoItem.Field.IsDone to !(item.done ?: false)))

/** Toggles the [item]'s [archival status][TodoItem.archived]. */
suspend fun TaskRepository.toggleArchived(item: TodoItem) =
    update(item.id, mapOf(TodoItem.Field.IsArchived to !(item.archived ?: false)))
