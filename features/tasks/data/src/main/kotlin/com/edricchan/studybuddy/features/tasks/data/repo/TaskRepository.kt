package com.edricchan.studybuddy.features.tasks.data.repo

import androidx.annotation.Discouraged
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.edricchan.studybuddy.data.common.QueryMapper
import com.edricchan.studybuddy.data.paging.firestore.firestorePagingSource
import com.edricchan.studybuddy.data.source.firestore.IDefaultFirestoreDataSource
import com.edricchan.studybuddy.domain.common.sorting.toFirestoreDirection
import com.edricchan.studybuddy.features.tasks.data.mapper.toDomain
import com.edricchan.studybuddy.features.tasks.data.mapper.toDto
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.repo.ITaskRepository
import com.edricchan.studybuddy.features.tasks.domain.repo.TasksPaginationConfig
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val source: IDefaultFirestoreDataSource<@JvmSuppressWildcards TodoItem>,
    private val projectsSource: IDefaultFirestoreDataSource<@JvmSuppressWildcards TodoProject>
) : ITaskRepository {
    /** Retrieves the user's list of tasks as a [Flow] of updates. */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Deprecated("Use the observeTasks method instead which returns a paginated list of tasks")
    val tasksFlow: Flow<List<TodoItem>> = source.items

    /** Retrieves the user's list of tasks given the specified [query] as a [Flow]. */
    @Discouraged(
        "Low-level querying is unsupported, use the abstracted " +
            "observeTasks method instead"
    )
    fun observeQueryTasks(query: QueryMapper): Flow<List<TodoItem>> = source.findAll(query)

    /** Retrieves the task given its [id] as a [Flow]. */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Deprecated("Use the observeTaskById method instead which returns the domain TaskItem model")
    fun observeTask(id: String): Flow<TodoItem?> = source[id]

    /** Adds the specified [task]. */
    @Deprecated("Use the variant of addTask which takes the domain TaskItem model")
    suspend fun addTask(task: TodoItem): DocumentReference = source.add(task)

    /** Removes the specified [task]. */
    @Deprecated(
        "Use deleteTask instead, which takes the domain TaskItem model",
        ReplaceWith("this.deleteTaskById(task.id)")
    )
    suspend fun removeTask(task: TodoItem) = deleteTaskById(task.id)

    /** Removes the specified task given its [id]. */
    @Deprecated(
        "Use deleteTaskById instead",
        ReplaceWith("this.deleteTaskById(id)")
    )
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
    @JvmName("updateTaskRawKeys")
    suspend fun updateTask(id: String, data: Map<String, Any?>) {
        source.update(id, data)
    }

    /** Updates the list of tasks (given by [ids]) with the specified [data]. */
    suspend fun updateTasks(ids: Set<String>, data: Map<String, Any?>) {
        source.runBatch {
            updateAll(ids, data)
        }
    }

    //#region New ITaskRepository interface implementations
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeTaskById(id: String): Flow<TaskItem?> {
        val taskFlow = source[id]

        return taskFlow.flatMapLatest { item ->
            item?.project?.let { projectRef ->
                taskFlow.combine(projectsSource[projectRef.id]) { task, project ->
                    task?.toDomain(project?.toDomain())
                }
            } ?: taskFlow.map { it?.toDomain(null) }
        }
    }

    override fun observeTasks(
        config: TasksPaginationConfig
    ): Flow<PagingData<TaskItem>> = flow {
        // flow {} is used here as we need a suspending context for the
        // getCollectionRef call
        val query = source.getCollectionRef().let {
            if (!config.includeArchived) it.whereEqualTo(
                TodoItem.Field.IsArchived.fieldName,
                true
            ) else it
        }.let {
            if (config.excludeCompleted) it.whereEqualTo(
                TodoItem.Field.IsDone.fieldName,
                true
            ) else it
        }.let {
            config.orderByFields.fold(it) { query, spec ->
                query.orderBy(spec.field.toDto().fieldName, spec.direction.toFirestoreDirection())
            }
        }

        emitAll(
            Pager(
                config = PagingConfig(
                    pageSize = config.pageSize
                )
            ) {
                firestorePagingSource<TodoItem>(
                    query = query,
                    limit = config.pageSize.toLong()
                )
            }.flow
                .cachedIn(config.cachedCoroutineScope)
                .map {
                    it.map { item ->
                        item.project?.let { projectRef ->
                            val proj = projectsSource.getSnapshot(projectRef.id)
                            item.toDomain(proj?.toDomain())
                        } ?: item.toDomain(null)
                    }
                }
        )
    }

    override suspend fun addTask(task: TaskItem) {
        source.add(task.toDto { projectsSource.getRef(it.id) })
    }

    override suspend fun updateTask(id: String, valueMap: Map<TaskItem.Field, Any?>) {
        source.update(id, valueMap.mapKeys { it.key.toDto().fieldName })
    }

    override suspend fun deleteTaskById(id: String) {
        source.removeById(id)
    }

    override suspend fun deleteTasksById(taskIds: Set<String>) {
        removeTasks(taskIds)
    }
    //#endregion
}

@Deprecated("Use the overload which takes the domain-specific TaskItem.Field enum")
suspend fun TaskRepository.update(
    id: String,
    data: Map<TodoItem.Field, Any?>
) {
    updateTask(id, valueMap = data.mapKeys { it.key.toDomain() })
}

@Deprecated("Use the overload which takes the domain-specific TaskItem.Field enum")
suspend fun TaskRepository.update(
    id: String,
    dataAction: MutableMap<TodoItem.Field, Any?>.() -> Unit
) {
    updateTask(id, valueMap = buildMap(dataAction).mapKeys { it.key.toDomain() })
}

/** Sets the item's [completion status][TodoItem.done] to the new [isCompleted] value. */
suspend fun TaskRepository.setCompletion(id: String, isCompleted: Boolean) =
    updateTask(id, TaskItem.FieldValue.IsCompleted(isCompleted))

/** Toggles the [item]'s [completion status][TodoItem.done]. */
@Deprecated("Use the overload which takes the domain-specific TaskItem class")
suspend fun TaskRepository.toggleCompleted(item: TodoItem) =
    setCompletion(item.id, !(item.done ?: false))

/** Toggles the [item]'s [completion status][TaskItem.isCompleted]. */
suspend fun TaskRepository.toggleCompleted(item: TaskItem) =
    setCompletion(item.id, !item.isCompleted)

/** Sets the item's [archival status][TaskItem.isArchived] to the new [isArchived] value. */
suspend fun TaskRepository.setArchival(id: String, isArchived: Boolean) =
    updateTask(id, TaskItem.FieldValue.IsArchived(isArchived))

/** Toggles the [item]'s [archival status][TodoItem.archived]. */
@Deprecated("Use the overload which takes the domain-specific TaskItem class")
suspend fun TaskRepository.toggleArchived(item: TodoItem) =
    setArchival(item.id, !(item.archived ?: false))

/** Toggles the [item]'s [archival status][TodoItem.archived]. */
suspend fun TaskRepository.toggleArchived(item: TaskItem) =
    setArchival(item.id, !item.isArchived)
