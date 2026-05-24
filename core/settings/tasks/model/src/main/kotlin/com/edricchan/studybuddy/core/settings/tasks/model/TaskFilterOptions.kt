package com.edricchan.studybuddy.core.settings.tasks.model

import com.edricchan.studybuddy.core.settings.tasks.model.field.TaskField
import com.edricchan.studybuddy.core.settings.tasks.model.sort.TaskSortEntry
import com.edricchan.studybuddy.core.settings.tasks.model.sort.toDomain
import com.edricchan.studybuddy.core.settings.tasks.model.sort.toProto
import com.edricchan.studybuddy.core.settings.tasks.proto.TasksFilterOptions
import com.edricchan.studybuddy.domain.common.sorting.SortDirection

data class TaskFilterOptions(
    // LinkedHashSet (unlike a regular Set) preserves insertion order
    val orderByFields: LinkedHashSet<TaskSortEntry> = linkedSetOf(
        TaskSortEntry(field = TaskField.CreatedDate, direction = SortDirection.Descending)
    ),
    val includeCompleted: Boolean = false,
    val includeArchived: Boolean = false
)

fun TasksFilterOptions.toDomain(): TaskFilterOptions = TaskFilterOptions(
    orderByFields = order_by_fields.map { it.toDomain() }.toCollection(LinkedHashSet()),
    includeCompleted = include_completed,
    includeArchived = include_archived
)

fun TaskFilterOptions.toProto(): TasksFilterOptions = TasksFilterOptions(
    order_by_fields = orderByFields.map { it.toProto() },
    include_completed = includeCompleted,
    include_archived = includeArchived
)
