package com.edricchan.studybuddy.features.tasks.domain.repo

import com.edricchan.studybuddy.domain.common.sorting.OrderSpec
import com.edricchan.studybuddy.domain.common.sorting.SortDirection
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import kotlinx.coroutines.CoroutineScope

/**
 * Configuration options for [ITaskRepository.observeTasks].
 * @property cachedCoroutineScope Desired [CoroutineScope] to cache the paging data in
 * (see [androidx.paging.cachedIn] for more info).
 * @property includeArchived Whether archived tasks should be included in the list.
 * @property excludeCompleted Whether completed tasks should be excluded from the list.
 * @property pageSize Number of tasks to be shown per page.
 * @property orderByFields The set of [TaskOrderSpec] configurations to order the resulting data by,
 * applied sequentially in-order.
 */
data class TasksPaginationConfig(
    val cachedCoroutineScope: CoroutineScope,
    val includeArchived: Boolean = false,
    val excludeCompleted: Boolean = false,
    val pageSize: Int = 30,
    val orderByFields: Set<TaskOrderSpec> = setOf(TaskOrderSpec())
) {
    data class TaskOrderSpec(
        override val field: TaskItem.Field = TaskItem.Field.CreatedAt,
        override val direction: SortDirection = SortDirection.Descending
    ) : OrderSpec<TaskItem.Field>(
        field = field,
        direction = direction
    )
}
