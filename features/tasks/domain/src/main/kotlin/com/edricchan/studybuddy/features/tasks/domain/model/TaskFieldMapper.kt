package com.edricchan.studybuddy.features.tasks.domain.model

import com.edricchan.studybuddy.features.tasks.proto.TaskField

fun TaskField.toDomain(): TaskItem.Field = when (this) {
    TaskField.Task_Title -> TaskItem.Field.Title
    TaskField.Task_Description -> TaskItem.Field.Content
    TaskField.Task_IsCompleted -> TaskItem.Field.IsCompleted
    TaskField.Task_IsArchived -> TaskItem.Field.IsArchived
    TaskField.Task_Priority -> TaskItem.Field.Priority
    TaskField.Task_Labels -> TaskItem.Field.Tags
    TaskField.Task_Project -> TaskItem.Field.Project
    TaskField.Task_DueDate -> TaskItem.Field.DueDate
    TaskField.Task_CreatedDate -> TaskItem.Field.CreatedAt
    TaskField.Task_LastModifiedDate -> TaskItem.Field.LastModified
    TaskField.Task_CompletedDate -> TaskItem.Field.CompletedDate
    TaskField.Task_ArchivedDate -> TaskItem.Field.ArchivedDate
    TaskField.Task_DeletedDate -> TaskItem.Field.DeletedDate
}

fun TaskItem.Field.toProto(): TaskField = when (this) {
    TaskItem.Field.Title -> TaskField.Task_Title
    TaskItem.Field.Content -> TaskField.Task_Description
    TaskItem.Field.DueDate -> TaskField.Task_DueDate
    TaskItem.Field.IsCompleted -> TaskField.Task_IsCompleted
    TaskItem.Field.IsArchived -> TaskField.Task_IsArchived
    TaskItem.Field.Priority -> TaskField.Task_Priority
    TaskItem.Field.Tags -> TaskField.Task_Labels
    TaskItem.Field.Project -> TaskField.Task_Project
    TaskItem.Field.CreatedAt -> TaskField.Task_CreatedDate
    TaskItem.Field.LastModified -> TaskField.Task_LastModifiedDate
    TaskItem.Field.CompletedDate -> TaskField.Task_CompletedDate
    TaskItem.Field.ArchivedDate -> TaskField.Task_ArchivedDate
    TaskItem.Field.DeletedDate -> TaskField.Task_DeletedDate
}
