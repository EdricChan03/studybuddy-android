package com.edricchan.studybuddy.core.settings.tasks.model

import com.edricchan.studybuddy.core.settings.tasks.proto.TasksSettingsSwipeAction
import com.edricchan.studybuddy.core.settings.tasks.proto.TasksSwipeActionOptions

enum class TaskSwipeAction(
    val protoValue: TasksSettingsSwipeAction
) {
    None(TasksSettingsSwipeAction.None),
    MarkComplete(TasksSettingsSwipeAction.MarkComplete),
    Archive(TasksSettingsSwipeAction.Archive),
    Delete(TasksSettingsSwipeAction.Delete),
    Edit(TasksSettingsSwipeAction.Edit),
    Snooze(TasksSettingsSwipeAction.Snooze);

    companion object {
        fun fromProto(proto: TasksSettingsSwipeAction): TaskSwipeAction {
            return TaskSwipeAction.entries.first { it.protoValue == proto }
        }
    }
}

data class TaskSwipeActionOptions(
    val towardsEnd: TaskSwipeAction = TaskSwipeAction.None,
    val towardsStart: TaskSwipeAction = TaskSwipeAction.None
)

fun TasksSwipeActionOptions.toDomain(): TaskSwipeActionOptions = TaskSwipeActionOptions(
    towardsEnd = TaskSwipeAction.fromProto(towards_end),
    towardsStart = TaskSwipeAction.fromProto(towards_start)
)

fun TaskSwipeActionOptions.toProto(): TasksSwipeActionOptions = TasksSwipeActionOptions(
    towards_end = towardsEnd.protoValue,
    towards_start = towardsStart.protoValue
)
