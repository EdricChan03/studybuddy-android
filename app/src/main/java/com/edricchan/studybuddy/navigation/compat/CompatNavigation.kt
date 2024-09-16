package com.edricchan.studybuddy.navigation.compat

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.fragment
import androidx.navigation.navigation
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.UriSettings
import com.edricchan.studybuddy.features.auth.ui.LoginActivity
import com.edricchan.studybuddy.features.auth.ui.RegisterActivity
import com.edricchan.studybuddy.features.auth.ui.ResetPasswordActivity
import com.edricchan.studybuddy.features.help.compat.HelpListFragment
import com.edricchan.studybuddy.ui.common.licenses.OssLicensesFragment
import com.edricchan.studybuddy.ui.modules.about.fragment.AboutFragment
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugModalBottomSheetActivity
import com.edricchan.studybuddy.ui.modules.main.MainActivity.Companion.ACTION_ADD_NEW_TODO
import com.edricchan.studybuddy.ui.modules.settings.fragment.SettingsFragment
import com.edricchan.studybuddy.ui.modules.task.EditTaskActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.fragment.TaskDetailFragment
import com.edricchan.studybuddy.ui.modules.task.fragment.TaskListFragment
import com.edricchan.studybuddy.ui.modules.tips.fragment.TipsFragment
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity

fun NavGraphBuilder.aboutGraph() {
    fragment<AboutFragment, CompatDestination.About.AppAbout>()
    fragment<OssLicensesFragment, CompatDestination.About.ViewLicenses>()
    activity<CompatDestination.About.SystemAppInfo> {
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

    fragment<TaskDetailFragment, CompatDestination.Task.View>()

    fragment<TaskListFragment, CompatDestination.Task.List>()
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

    fragment<HelpListFragment, CompatDestination.Help>()

    aboutGraph()
    authGraph()
    taskGraph()

    fragment<CalendarFragment, CompatDestination.Calendar>()
    fragment<TipsFragment, CompatDestination.Tips>()

    fragment<SettingsFragment, CompatDestination.Settings> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            deepLink {
                action = Intent.ACTION_APPLICATION_PREFERENCES
            }
        }
        deepLink(UriSettings)
    }
}
