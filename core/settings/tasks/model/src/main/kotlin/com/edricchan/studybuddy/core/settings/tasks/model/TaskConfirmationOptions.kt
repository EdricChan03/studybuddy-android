package com.edricchan.studybuddy.core.settings.tasks.model

import com.edricchan.studybuddy.core.settings.tasks.proto.TasksConfirmationOptions

data class TaskConfirmationOptions(
    val onDelete: Boolean = true,
    val onArchive: Boolean = false,
    val onComplete: Boolean = false
)

fun TasksConfirmationOptions.toDomain(): TaskConfirmationOptions = TaskConfirmationOptions(
    onDelete = show_on_delete ?: true,
    onArchive = show_on_archive ?: false,
    onComplete = show_on_mark_complete ?: false
)

fun TaskConfirmationOptions.toProto(): TasksConfirmationOptions = TasksConfirmationOptions(
    show_on_delete = onDelete,
    show_on_archive = onArchive,
    show_on_mark_complete = onComplete
)
