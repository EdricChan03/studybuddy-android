package com.edricchan.studybuddy.core.settings.tasks.model.field

import com.edricchan.studybuddy.features.tasks.proto.TaskField as TaskFieldProto

enum class TaskField(val protoValue: TaskFieldProto) {
    Title(TaskFieldProto.Title),
    Description(TaskFieldProto.Description),

    @Deprecated("Use CompletedDate instead")
    IsCompleted(TaskFieldProto.IsCompleted),

    @Deprecated("Use ArchivedDate instead")
    IsArchived(TaskFieldProto.IsArchived),
    Priority(TaskFieldProto.Priority),
    Labels(TaskFieldProto.Labels),
    DueDate(TaskFieldProto.DueDate),
    CreatedDate(TaskFieldProto.CreatedDate),
    CompletedDate(TaskFieldProto.CompletedDate),
    ArchivedDate(TaskFieldProto.ArchivedDate),
    DeletedDate(TaskFieldProto.DeletedDate);

    companion object {
        fun fromProto(proto: TaskFieldProto): TaskField {
            return TaskField.entries.first { it.protoValue == proto }
        }
    }
}
