package com.edricchan.studybuddy.navigation.compat

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.fragment
import androidx.navigation.navigation
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
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
import com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags.FeatureFlagsSettingsFragment
import com.edricchan.studybuddy.ui.modules.task.EditTaskActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.fragment.TaskDetailFragment
import com.edricchan.studybuddy.ui.modules.task.fragment.TaskListFragment
import com.edricchan.studybuddy.ui.modules.tips.fragment.TipsFragment
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity

fun NavGraphBuilder.aboutGraph(
    context: Context
) {
    fragment<AboutFragment, CompatDestination.About.AppAbout> {
        label = context.getString(R.string.title_activity_about)
    }
    fragment<OssLicensesFragment, CompatDestination.About.ViewLicenses> {
        label = context.getString(R.string.title_activity_licenses)
    }
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

fun NavGraphBuilder.taskGraph(
    context: Context
) = navigation<CompatDestination.Task.Root>(
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

    fragment<TaskDetailFragment, CompatDestination.Task.View> {
        label = context.getString(R.string.title_activity_view_task)
    }

    fragment<TaskListFragment, CompatDestination.Task.List> {
        label = context.getString(R.string.bottom_nav_todos)
    }
}

/**
 * Navigation graph for destinations that have yet to be migrated to
 * Jetpack Compose.
 * @see CompatDestination
 */
// TODO: Migrate destinations to Jetpack Compose
fun NavGraphBuilder.compatGraphs(context: Context) {
    activity<CompatDestination.Debug> {
        activityClass = DebugActivity::class
    }
    activity<CompatDestination.DebugModalBottomSheet> {
        activityClass = DebugModalBottomSheetActivity::class
    }

    fragment<FeatureFlagsSettingsFragment, CompatDestination.FeatureFlagsList> {
        label = context.getString(R.string.debug_activity_feature_flags_title)
    }

    activity<CompatDestination.Updates> {
        activityClass = UpdatesActivity::class
    }

    fragment<HelpListFragment, CompatDestination.Help> {
        label = context.getString(R.string.title_activity_help)
    }

    aboutGraph(context = context)
    authGraph()
    taskGraph(context = context)

    fragment<CalendarFragment, CompatDestination.Calendar> {
        label = context.getString(R.string.bottom_nav_calendar)
    }
    fragment<TipsFragment, CompatDestination.Tips> {
        label = context.getString(R.string.bottom_nav_tips)
    }

    fragment<SettingsFragment, CompatDestination.Settings> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            deepLink {
                action = Intent.ACTION_APPLICATION_PREFERENCES
            }
        }
        deepLink(UriSettings)
        label = context.getString(R.string.title_activity_settings)
    }
}
