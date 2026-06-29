package com.edricchan.studybuddy.core.settings.tasks.model

import com.edricchan.studybuddy.core.settings.tasks.model.field.TaskField
import com.edricchan.studybuddy.core.settings.tasks.model.sort.TaskSortEntry
import com.edricchan.studybuddy.core.settings.tasks.model.sort.toDomain
import com.edricchan.studybuddy.core.settings.tasks.model.sort.toProto
import com.edricchan.studybuddy.core.settings.tasks.proto.TasksFilterOptions
import com.edricchan.studybuddy.domain.common.sorting.SortDirection

typealias TaskOrderMap = LinkedHashMap<TaskField, SortDirection>

val DefaultTaskOrderPair = TaskField.CreatedDate to SortDirection.Descending
val DefaultTaskOrdering: TaskOrderMap = linkedMapOf(
    DefaultTaskOrderPair
)

data class TaskFilterOptions(
    val orderByFields: TaskOrderMap = DefaultTaskOrdering,
    val includeCompleted: Boolean = false,
    val includeArchived: Boolean = false
)

fun TasksFilterOptions.toDomain(): TaskFilterOptions = TaskFilterOptions(
    orderByFields = order_by_fields.map { it.toDomain() }
        .associateTo(LinkedHashMap()) { it.field to it.direction },
    includeCompleted = include_completed,
    includeArchived = include_archived
)

fun TaskFilterOptions.toProto(): TasksFilterOptions = TasksFilterOptions(
    order_by_fields = orderByFields.map { TaskSortEntry(it.toPair()).toProto() }
        .toList(),
    include_completed = includeCompleted,
    include_archived = includeArchived
)
