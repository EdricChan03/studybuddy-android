package com.edricchan.studybuddy.core.compat.navigation

import androidx.annotation.Keep
import androidx.core.os.BundleCompat
import androidx.navigation.NavType
import androidx.savedstate.SavedState
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
}
