package com.edricchan.studybuddy.navigation.compat

import android.provider.Settings
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.fragment
import androidx.navigation.navigation
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.features.about.navigation.AboutDestination
import com.edricchan.studybuddy.features.auth.ui.LoginActivity
import com.edricchan.studybuddy.features.auth.ui.RegisterActivity
import com.edricchan.studybuddy.features.auth.ui.ResetPasswordActivity
import com.edricchan.studybuddy.features.help.HelpActivity
import com.edricchan.studybuddy.ui.common.licenses.OssLicensesFragment
import com.edricchan.studybuddy.ui.modules.about.fragment.AboutFragment
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugModalBottomSheetActivity
import com.edricchan.studybuddy.ui.modules.main.MainActivity.Companion.ACTION_ADD_NEW_TODO
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.EditTaskActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.ViewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.fragment.TodoFragment
import com.edricchan.studybuddy.ui.modules.tips.fragment.TipsFragment
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
import kotlinx.serialization.Serializable

/** Destinations that have yet to be migrated to Jetpack Compose. */
// TODO: Remove
sealed interface CompatDestination {
    // Top-level destinations

    /** Typed destination for the [DebugActivity] entry-point. */
    @Serializable
    data object Debug : CompatDestination

    /** Typed destination for the [DebugModalBottomSheetActivity] entry-point. */
    @Serializable
    data object DebugModalBottomSheet : CompatDestination

    /** Typed destination for the [UpdatesActivity] entry-point. */
    @Serializable
    data object Updates : CompatDestination

    /** Typed destination for the [SettingsActivity] entry-point. */
    @Serializable
    data object Settings : CompatDestination

    /** Typed destination for the [HelpActivity] entry-point. */
    @Serializable
    data object Help : CompatDestination

    // Feature destinations

    /** Typed destination for the [TipsFragment] entry-point. */
    @Serializable
    data object Tips : CompatDestination

    /** Typed destination for the [CalendarFragment] entry-point. */
    @Serializable
    data object Calendar : CompatDestination

    /** Destinations related to the about feature. */
    @Serializable
    sealed interface About : CompatDestination {
        /** Typed destination for the [AboutFragment] entry-point. */
        @Serializable
        data object AppAbout : About

        /** Typed destination for viewing the app's licenses. */
        @Serializable
        data object ViewLicenses : About
    }


    /** Destinations related to the auth feature. */
    @Serializable
    sealed interface Auth : CompatDestination {
        /** Typed destination for the [ResetPasswordActivity] entry-point. */
        @Serializable
        data object ResetPassword : Auth

        /** Typed destination for the [AccountActivity] entry-point. */
        @Serializable
        data object Account : Auth

        /** Typed destination for the [LoginActivity] entry-point. */
        @Serializable
        data object Login : Auth

        /** Typed destination for the [RegisterActivity] entry-point. */
        @Serializable
        data object Register : Auth
    }

    /** Destinations related to the tasks feature. */
    @Serializable
    sealed interface Task : CompatDestination {
        /** Top-level entry-point for the tasks feature. */
        @Serializable
        data object Root : Task

        /** Typed destination for the [NewTaskActivity] entry-point. */
        @Serializable
        data object New : Task

        /** Typed destination for the [TodoFragment] entry-point. */
        @Serializable
        data object List : Task

        /** Typed destination for the [EditTaskActivity] entry-point. */
        @Serializable
        data class Edit(val taskId: String) : Task

        /** Typed destination for the [ViewTaskActivity] entry-point. */
        @Serializable
        data class View(val taskId: String) : Task
    }
}

fun NavGraphBuilder.aboutGraph() {
    fragment<AboutFragment, CompatDestination.About.AppAbout>()
    fragment<OssLicensesFragment, CompatDestination.About.ViewLicenses>()
    activity<AboutDestination.SystemAppInfo> {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = "package:${BuildConfig.APPLICATION_ID}".toUri()
    }
}

fun NavGraphBuilder.authGraph() {
    activity<CompatDestination.Auth.Account> {
        activityClass = AccountActivity::class
    }

    activity<CompatDestination.Auth.ResetPassword> {
        activityClass = ResetPasswordActivity::class
    }

    activity<CompatDestination.Auth.Login> {
        activityClass = LoginActivity::class
    }

    activity<CompatDestination.Auth.Register> {
        activityClass = RegisterActivity::class
    }
}

fun NavGraphBuilder.taskGraph() = navigation<CompatDestination.Task.Root>(
    startDestination = CompatDestination.Task.List
) {
    activity<CompatDestination.Task.Edit> {
        activityClass = EditTaskActivity::class
    }

    activity<CompatDestination.Task.New> {
        activityClass = NewTaskActivity::class
        deepLink {
            action = ACTION_ADD_NEW_TODO
        }
    }

    activity<CompatDestination.Task.View> {
        activityClass = ViewTaskActivity::class
    }

    fragment<TodoFragment, CompatDestination.Task.List>()
}

/**
 * Navigation graph for destinations that have yet to be migrated to
 * Jetpack Compose.
 * @see CompatDestination
 */
// TODO: Migrate destinations to Jetpack Compose
fun NavGraphBuilder.compatGraphs() {
    activity<CompatDestination.Debug> {
        activityClass = DebugActivity::class
    }
    activity<CompatDestination.DebugModalBottomSheet> {
        activityClass = DebugModalBottomSheetActivity::class
    }

    activity<CompatDestination.Updates> {
        activityClass = UpdatesActivity::class
    }

    activity<CompatDestination.Settings> {
        activityClass = SettingsActivity::class
    }

    activity<CompatDestination.Help> {
        activityClass = HelpActivity::class
    }

    aboutGraph()
    authGraph()
    taskGraph()

    fragment<CalendarFragment, CompatDestination.Calendar>()
    fragment<TipsFragment, CompatDestination.Tips>()
}
