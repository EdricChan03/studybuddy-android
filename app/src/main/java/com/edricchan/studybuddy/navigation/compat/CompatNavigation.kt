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
import com.edricchan.studybuddy.core.compat.navigation.UriUpdates
import com.edricchan.studybuddy.core.deeplink.AppPrefixUrls
import com.edricchan.studybuddy.core.deeplink.WebPrefixUrls
import com.edricchan.studybuddy.features.auth.account.compat.AccountFragment
import com.edricchan.studybuddy.features.auth.ui.LoginActivity
import com.edricchan.studybuddy.features.auth.ui.RegisterActivity
import com.edricchan.studybuddy.features.auth.ui.ResetPasswordActivity
import com.edricchan.studybuddy.features.help.compat.HelpListFragment
import com.edricchan.studybuddy.features.settings.main.ui.compat.SettingsFragment
import com.edricchan.studybuddy.features.tasks.compat.ui.fragment.TaskDetailFragment
import com.edricchan.studybuddy.features.tasks.create.ui.compat.NewTaskFragment
import com.edricchan.studybuddy.features.tasks.edit.ui.compat.EditTaskFragment
import com.edricchan.studybuddy.features.tasks.list.ui.compat.TaskListFragment
import com.edricchan.studybuddy.ui.common.licenses.OssLicensesFragment
import com.edricchan.studybuddy.ui.modules.about.fragment.AboutFragment
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugModalBottomSheetFragment
import com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags.FeatureFlagsSettingsFragment
import com.edricchan.studybuddy.ui.modules.updates.UpdatesFragment
import kotlin.reflect.typeOf

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

fun NavGraphBuilder.authGraph(
    context: Context
) {
    fragment<AccountFragment, CompatDestination.Auth.Account>(
        typeMap = mapOf(typeOf<CompatDestination.Auth.Account.AccountAction>() to CompatDestination.Auth.Account.AccountAction.NavType)
    ) {
        label = context.getString(R.string.title_activity_account)

        (AppPrefixUrls + WebPrefixUrls).forEach {
            deepLink<CompatDestination.Auth.Account>(basePath = "$it/account")
        }
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
    fragment<EditTaskFragment, CompatDestination.Task.Edit> {
        label = context.getString(R.string.title_activity_edit_task)
    }

    fragment<NewTaskFragment, CompatDestination.Task.New> {
        deepLink {
            action = NewTaskFragment.ACTION_NEW_TASK_SHORTCUT
        }

        label = context.getString(R.string.title_activity_new_task)
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
    fragment<DebugModalBottomSheetFragment, CompatDestination.DebugModalBottomSheet> {
        label = context.getString(R.string.title_activity_debug_modal_bottom_sheet)
    }

    fragment<FeatureFlagsSettingsFragment, CompatDestination.FeatureFlagsList> {
        label = context.getString(R.string.debug_activity_feature_flags_title)
    }

    fragment<UpdatesFragment, CompatDestination.Updates> {
        label = context.getString(R.string.title_activity_updates)
        deepLink(UriUpdates)
    }

    fragment<HelpListFragment, CompatDestination.Help> {
        label = context.getString(R.string.title_activity_help)
    }

    aboutGraph(context = context)
    authGraph(context = context)
    taskGraph(context = context)

    fragment<CalendarFragment, CompatDestination.Calendar> {
        label = context.getString(R.string.bottom_nav_calendar)
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
