package com.edricchan.studybuddy.core.compat.navigation

import kotlinx.serialization.Serializable

/** Destinations that have yet to be migrated to Jetpack Compose. */
// TODO: Remove
@Serializable
sealed interface CompatDestination {
    // Top-level destinations

    /** Typed destination for the `DebugActivity` entry-point. */
    @Serializable
    data object Debug : CompatDestination

    /** Typed destination for the `DebugModalBottomSheetActivity` entry-point. */
    @Serializable
    data object DebugModalBottomSheet : CompatDestination

    /** Typed destination for the updates entry-point. */
    @Serializable
    data object Updates : CompatDestination

    /** Typed destination for the settings entry-point. */
    @Serializable
    data object Settings : CompatDestination

    /** Typed destination for the help entry-point. */
    @Serializable
    data object Help : CompatDestination

    // Feature destinations

    /** Typed destination for the tips entry-point. */
    @Serializable
    data object Tips : CompatDestination

    /** Typed destination for the calendar entry-point. */
    @Serializable
    data object Calendar : CompatDestination

    /** Destinations related to the about feature. */
    @Serializable
    sealed interface About : CompatDestination {
        /** Typed destination for the about entry-point. */
        @Serializable
        data object AppAbout : About

        /** Typed destination for viewing the app's information in the device's settings. */
        @Serializable
        data object SystemAppInfo : About

        /** Typed destination for viewing the app's source code. */
        @Serializable
        data object ViewSource : About

        /** Typed destination for viewing the app's licenses. */
        @Serializable
        data object ViewLicenses : About
    }


    /** Destinations related to the auth feature. */
    @Serializable
    sealed interface Auth : CompatDestination {
        /** Typed destination for the reset password entry-point. */
        @Serializable
        data object ResetPassword : Auth

        /** Typed destination for the account entry-point. */
        @Serializable
        data object Account : Auth

        /** Typed destination for the login entry-point. */
        @Serializable
        data object Login : Auth

        /** Typed destination for the register entry-point. */
        @Serializable
        data object Register : Auth
    }

    /** Destinations related to the tasks feature. */
    @Serializable
    sealed interface Task : CompatDestination {
        /** Top-level entry-point for the tasks feature. */
        @Serializable
        data object Root : Task

        /** Typed destination for the new task entry-point. */
        @Serializable
        data object New : Task

        /** Typed destination for the task list entry-point. */
        @Serializable
        data object List : Task

        /** Typed destination for the edit task entry-point. */
        @Serializable
        data class Edit(val taskId: String) : Task

        /** Typed destination for the view task entry-point. */
        @Serializable
        data class View(val taskId: String) : Task
    }
}
