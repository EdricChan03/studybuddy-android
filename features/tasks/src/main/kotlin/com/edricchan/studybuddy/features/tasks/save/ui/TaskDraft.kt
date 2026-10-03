package com.edricchan.studybuddy.features.tasks.save.ui

import com.edricchan.studybuddy.domain.common.updater.compareFields
import com.edricchan.studybuddy.exts.datetime.toInstant
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.domain.model.create.CreateTaskItemInput
import com.edricchan.studybuddy.features.tasks.domain.model.update.UpdateTaskItemInput
import java.time.LocalDate
import java.time.LocalTime

data class TaskDraft(
    val title: String,
    val description: String,
    val dueDate: LocalDate?,
    val dueTime: LocalTime?,
    val isCompleted: Boolean,
    val isArchived: Boolean,
    val tags: Set<String>,
    val project: TaskProject?
)

fun TaskDraft.toCreateInput(): CreateTaskItemInput = CreateTaskItemInput(
    title = title,
    description = description,
    // TODO: Decouple the due-time from the due-date when we migrate away from Firestore at some
    //  point to a proper SQL-based database which supports storing times separately from dates
    dueDate = dueDate?.atTime(dueTime ?: LocalTime.MIDNIGHT)?.toInstant(),
    isCompleted = isCompleted,
    isArchived = isArchived,
    tags = tags,
    projectId = project?.id
)

fun TaskDraft.toUpdateInput(
    sourceItem: TaskItem
): UpdateTaskItemInput = UpdateTaskItemInput(
    title = compareFields(sourceItem.title, title),
    description = compareFields(sourceItem.content, description),
    // TODO: Decouple the due-time from the due-date when we migrate away from Firestore at some
    //  point to a proper SQL-based database which supports storing times separately from dates
    dueDate = compareFields(
        sourceItem.dueDate,
        dueDate?.atTime(dueTime ?: LocalTime.now())?.toInstant()
    ),
    isCompleted = compareFields(sourceItem.isCompleted, isCompleted),
    isArchived = compareFields(sourceItem.isArchived, isArchived),
    tags = compareFields(sourceItem.tags.orEmpty(), tags),
    projectId = compareFields(sourceItem.project?.id, project?.id)
)
