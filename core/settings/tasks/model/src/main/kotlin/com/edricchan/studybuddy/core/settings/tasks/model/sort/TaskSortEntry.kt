package com.edricchan.studybuddy.core.settings.tasks.model.sort

import com.edricchan.studybuddy.core.settings.tasks.model.field.TaskField
import com.edricchan.studybuddy.domain.common.sorting.SortDirection
import com.edricchan.studybuddy.domain.common.sorting.toDomain
import com.edricchan.studybuddy.domain.common.sorting.toProto
import com.edricchan.studybuddy.features.tasks.proto.TaskSortEntry as TaskSortEntryProto

data class TaskSortEntry(
    val field: TaskField,
    val direction: SortDirection
) {
    constructor(
        pair: Pair<TaskField, SortDirection>
    ) : this(
        field = pair.first,
        direction = pair.second
    )

    fun asPair(): Pair<TaskField, SortDirection> = field to direction
}

fun TaskSortEntry.toProto(): TaskSortEntryProto = TaskSortEntryProto(
    field_ = field.protoValue,
    direction = direction.toProto()
)

fun TaskSortEntryProto.toDomain(): TaskSortEntry = TaskSortEntry(
    field = TaskField.fromProto(field_),
    direction = direction.toDomain()
)
