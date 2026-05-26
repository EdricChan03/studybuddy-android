package com.edricchan.studybuddy.core.settings.tasks.model.field

import com.edricchan.studybuddy.features.tasks.proto.TaskField as TaskFieldProto

enum class TaskField(val protoValue: TaskFieldProto) {
    Title(TaskFieldProto.Task_Title),
    Description(TaskFieldProto.Task_Description),

    @Deprecated("Use CompletedDate instead")
    IsCompleted(TaskFieldProto.Task_IsCompleted),

    @Deprecated("Use ArchivedDate instead")
    IsArchived(TaskFieldProto.Task_IsArchived),
    Priority(TaskFieldProto.Task_Priority),
    Labels(TaskFieldProto.Task_Labels),
    DueDate(TaskFieldProto.Task_DueDate),
    CreatedDate(TaskFieldProto.Task_CreatedDate),
    LastModifiedDate(TaskFieldProto.Task_LastModifiedDate),
    CompletedDate(TaskFieldProto.Task_CompletedDate),
    ArchivedDate(TaskFieldProto.Task_ArchivedDate),
    DeletedDate(TaskFieldProto.Task_DeletedDate);

    companion object {
        fun fromProto(proto: TaskFieldProto): TaskField {
            return TaskField.entries.first { it.protoValue == proto }
        }
    }
}
