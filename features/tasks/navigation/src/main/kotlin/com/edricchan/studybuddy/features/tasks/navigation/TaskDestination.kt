package com.edricchan.studybuddy.features.tasks.navigation

import kotlinx.serialization.Serializable

/** Type-safe destinations for the tasks feature. */
@Serializable
sealed interface TaskDestination {
    /** Root destination for all sub-destinations in the tasks graph. */
    @Serializable
    data object TaskGraphRoot : TaskDestination

    /** Destination to list the signed-in user's tasks. */
    @Serializable
    data object ListTasks : TaskDestination

    /** Destination to create a new task. */
    @Serializable
    data object NewTask : TaskDestination

    /** For destinations that require a task's metadata. */
    private interface Detail {
        /** The requested ID to be displayed or used. */
        val taskId: String
    }

    /** Destination to view the requested [task][taskId]. */
    @Serializable
    data class ViewTask(override val taskId: String) : TaskDestination, Detail

    /** Destination to edit the requested [task][taskId]. */
    @Serializable
    data class EditTask(override val taskId: String) : TaskDestination, Detail
}
