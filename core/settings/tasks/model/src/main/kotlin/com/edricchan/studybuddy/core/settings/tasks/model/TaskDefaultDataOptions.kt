package com.edricchan.studybuddy.core.settings.tasks.model

import com.edricchan.studybuddy.core.settings.tasks.proto.TasksDefaultDataOptions
import com.edricchan.studybuddy.data.common.temporal.toLocalTime
import com.edricchan.studybuddy.data.common.temporal.toTimeOfDay
import java.time.Duration
import java.time.Instant
import java.time.LocalTime

data class TaskDefaultDataOptions(
    val title: String? = null,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false,
    val priority: Int? = null,
    val labels: Set<String> = emptySet(),
    val dueDate: DueDate? = null,
    val dueTime: DueTime? = null
) {
    sealed interface DueDate {
        data class FixedInstant(val instant: Instant) : DueDate
        data class FutureDuration(val duration: Duration) : DueDate
    }

    sealed interface DueTime {
        data class Fixed(val time: LocalTime) : DueTime
        data object Current : DueTime
    }
}

fun TaskDefaultDataOptions.toProto(): TasksDefaultDataOptions = TasksDefaultDataOptions(
    title = title,
    description = description,
    is_completed = isCompleted,
    is_archived = isArchived,
    priority = priority,
    labels = labels.toList(),
    due_date_no_default = if (dueDate == null) true else null,
    due_date_fixed_instant = (dueDate as? TaskDefaultDataOptions.DueDate.FixedInstant)?.instant,
    due_date_future_duration = (dueDate as? TaskDefaultDataOptions.DueDate.FutureDuration)?.duration,
    due_time_no_default = if (dueTime == null) true else null,
    due_time_fixed = (dueTime as? TaskDefaultDataOptions.DueTime.Fixed)?.time?.toTimeOfDay(),
    due_time_current = dueTime is TaskDefaultDataOptions.DueTime.Current
)

private val TasksDefaultDataOptions.dueDate: TaskDefaultDataOptions.DueDate?
    get() {
        var dueDate: TaskDefaultDataOptions.DueDate? = null
        due_date_fixed_instant?.let { dueDate = TaskDefaultDataOptions.DueDate.FixedInstant(it) }
        due_date_future_duration?.let {
            dueDate = TaskDefaultDataOptions.DueDate.FutureDuration(it)
        }
        return dueDate
    }

private val TasksDefaultDataOptions.dueTime: TaskDefaultDataOptions.DueTime?
    get() {
        var dueTime: TaskDefaultDataOptions.DueTime? = null
        due_time_fixed?.let { dueTime = TaskDefaultDataOptions.DueTime.Fixed(it.toLocalTime()) }
        due_time_current?.let {
            dueTime = TaskDefaultDataOptions.DueTime.Current
        }
        return dueTime
    }

fun TasksDefaultDataOptions.toDomain(): TaskDefaultDataOptions {
    return TaskDefaultDataOptions(
        title = title,
        description = description,
        isCompleted = is_completed ?: false,
        isArchived = is_archived ?: false,
        priority = priority,
        labels = labels.toSet(),
        dueDate = dueDate,
        dueTime = dueTime
    )
}
