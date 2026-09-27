package com.edricchan.studybuddy.features.tasks.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.edricchan.studybuddy.data.paging.firestore.firestorePagingSource
import com.edricchan.studybuddy.domain.common.sorting.toFirestoreDirection
import com.edricchan.studybuddy.features.tasks.data.mapper.toDomain
import com.edricchan.studybuddy.features.tasks.data.mapper.toDto
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.create.toDto
import com.edricchan.studybuddy.features.tasks.data.model.update.toFields
import com.edricchan.studybuddy.features.tasks.data.repo.source.TaskDataSource
import com.edricchan.studybuddy.features.tasks.data.repo.source.TaskProjectDataSource
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskItemInput
import com.edricchan.studybuddy.features.tasks.domain.model.update.UpdateTaskItemInput
import com.edricchan.studybuddy.features.tasks.domain.repo.ITaskRepository
import com.edricchan.studybuddy.features.tasks.domain.repo.TasksPaginationConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val source: TaskDataSource,
    private val projectsSource: TaskProjectDataSource
) : ITaskRepository {
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
            if (config.includeArchived) it.whereEqualTo(
                TodoItem.Field.IsArchived.fieldName,
                true
            ) else it
        }.let {
            if (config.excludeCompleted) it.whereEqualTo(
                TodoItem.Field.IsDone.fieldName,
                false
            ) else it
        }.let {
            config.orderByFields.entries.fold(it) { query, (field, direction) ->
                query.orderBy(field.toDto().fieldName, direction.toFirestoreDirection())
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
                .map {
                    it.map { item ->
                        item.project?.let { projectRef ->
                            val proj = projectsSource.getSnapshot(projectRef.id)
                            item.toDomain(proj?.toDomain())
                        } ?: item.toDomain(null)
                    }
                }
                .cachedIn(config.cachedCoroutineScope)
        )
    }

    override suspend fun addTask(input: CreateTaskItemInput) {
        source.add(input.toDto { projectsSource.getRef(it) })
    }

    @Deprecated("Use the type-safe overload which uses FieldValue rather than a Map")
    override suspend fun updateTask(id: String, valueMap: Map<TaskItem.Field, Any?>) {
        source.update(id, valueMap.mapKeys { it.key.toDto().fieldName })
    }

    private suspend fun Iterable<TaskItem.FieldValue<*>>.toUpdateDto(): Map<String, Any?> =
        fold(emptyMap()) { acc, fieldValue ->
            context(source, projectsSource) {
                acc + fieldValue.toDto().toMap()
            }
        }

    private suspend fun Array<out TaskItem.FieldValue<*>>.toUpdateDto(): Map<String, Any?> =
        asIterable().toUpdateDto()

    @Deprecated("Use the overload which accepts a concrete input data class")
    override suspend fun updateTask(id: String, vararg values: TaskItem.FieldValue<*>) {
        source.update(
            id = id,
            data = values.toUpdateDto()
        )
    }

    override suspend fun updateTask(id: String, input: UpdateTaskItemInput) {
        source.update(
            id = id,
            data = input.toFields().toUpdateDto()
        )
    }

    @Deprecated("Use the overload which accepts a concrete input data class")
    override suspend fun updateTasks(ids: Set<String>, vararg values: TaskItem.FieldValue<*>) {
        val updatedData = values.toUpdateDto()

        source.runBatch {
            updateAll(ids, updatedData)
        }
    }

    override suspend fun updateTasks(ids: Set<String>, input: UpdateTaskItemInput) {
        val updatedData = input.toFields().toUpdateDto()

        source.runBatch {
            updateAll(ids, updatedData)
        }
    }

    override suspend fun deleteTaskById(id: String) {
        source.removeById(id)
    }

    override suspend fun deleteTasksById(taskIds: Set<String>) {
        source.runBatch {
            deleteAll(taskIds)
        }
    }
    //#endregion
}

/** Sets the item's [completion status][TodoItem.done] to the new [isCompleted] value. */
suspend fun TaskRepository.setCompletion(id: String, isCompleted: Boolean) =
    updateTask(id, TaskItem.FieldValue.IsCompleted(isCompleted))

/** Toggles the [item]'s [completion status][TaskItem.isCompleted]. */
suspend fun TaskRepository.toggleCompleted(item: TaskItem) =
    setCompletion(item.id, !item.isCompleted)

/** Sets the item's [archival status][TaskItem.isArchived] to the new [isArchived] value. */
suspend fun TaskRepository.setArchival(id: String, isArchived: Boolean) =
    updateTask(id, TaskItem.FieldValue.IsArchived(isArchived))

/** Toggles the [item]'s [archival status][TodoItem.archived]. */
suspend fun TaskRepository.toggleArchived(item: TaskItem) =
    setArchival(item.id, !item.isArchived)
