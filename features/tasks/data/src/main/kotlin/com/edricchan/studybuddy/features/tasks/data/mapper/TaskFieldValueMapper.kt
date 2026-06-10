package com.edricchan.studybuddy.features.tasks.data.mapper

import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem

fun TaskItem.FieldValue.Title.toDto(): TodoItem.FieldValue.Title =
    TodoItem.FieldValue.Title(value)

fun TaskItem.FieldValue.Content.toDto(): TodoItem.FieldValue.Content =
    TodoItem.FieldValue.Content(value.orEmpty())

fun TaskItem.FieldValue.DueDate.toDto(): TodoItem.FieldValue.DueDate =
    TodoItem.FieldValue.DueDate(value?.toTimestamp())

fun TaskItem.FieldValue.IsArchived.toDto(): TodoItem.FieldValue.IsArchived =
    TodoItem.FieldValue.IsArchived(value)

fun TaskItem.FieldValue.IsCompleted.toDto(): TodoItem.FieldValue.IsDone =
    TodoItem.FieldValue.IsDone(value)

fun TaskItem.FieldValue.CompletedDate.toDto(): TodoItem.FieldValue.CompletedDate =
    TodoItem.FieldValue.CompletedDate(value?.toTimestamp())

fun TaskItem.FieldValue.ArchivedDate.toDto(): TodoItem.FieldValue.ArchivedDate =
    TodoItem.FieldValue.ArchivedDate(value?.toTimestamp())

fun TaskItem.FieldValue.Project.toDto(): TodoItem.FieldValue.ProjectId =
    TodoItem.FieldValue.ProjectId(value?.id)

fun TaskItem.FieldValue.Priority.toDto(): TodoItem.FieldValue.Priority =
    TodoItem.FieldValue.Priority(value)

fun TaskItem.FieldValue.Tags.toDto(): TodoItem.FieldValue.Tags =
    TodoItem.FieldValue.Tags(value.orEmpty())

fun TaskItem.FieldValue.DeletedDate.toDto(): TodoItem.FieldValue.DeletedDate =
    TodoItem.FieldValue.DeletedDate(value?.toTimestamp())

fun TaskItem.FieldValue<*>.toDto(): TodoItem.FieldValue<*> = when (this) {
    is TaskItem.FieldValue.Title -> toDto()
    is TaskItem.FieldValue.Content -> toDto()
    is TaskItem.FieldValue.DueDate -> toDto()
    is TaskItem.FieldValue.IsArchived -> toDto()
    is TaskItem.FieldValue.IsCompleted -> toDto()
    is TaskItem.FieldValue.CompletedDate -> toDto()
    is TaskItem.FieldValue.ArchivedDate -> toDto()
    is TaskItem.FieldValue.DeletedDate -> toDto()
    is TaskItem.FieldValue.Project -> toDto()
    is TaskItem.FieldValue.Tags -> toDto()
    is TaskItem.FieldValue.Priority -> toDto()
}
