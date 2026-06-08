package com.edricchan.studybuddy.navigation.compat

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.activity
import androidx.navigation.fragment.fragment
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.UriSettings
import com.edricchan.studybuddy.core.compat.navigation.UriUpdates
import com.edricchan.studybuddy.features.auth.navigation.graph.authGraph
import com.edricchan.studybuddy.features.help.compat.HelpListFragment
import com.edricchan.studybuddy.features.settings.main.ui.compat.SettingsFragment
import com.edricchan.studybuddy.features.tasks.navigation.graph.taskGraph
import com.edricchan.studybuddy.ui.common.licenses.OssLicensesFragment
import com.edricchan.studybuddy.ui.modules.about.fragment.AboutFragment
import com.edricchan.studybuddy.ui.modules.calendar.fragment.CalendarFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugModalBottomSheetFragment
import com.edricchan.studybuddy.ui.modules.updates.UpdatesFragment
import com.edricchan.studybuddy.utils.android.fromApi

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

/**
 * Navigation graph for destinations that have yet to be migrated to
 * Jetpack Compose.
 * @see CompatDestination
 */
// TODO: Migrate destinations to Jetpack Compose
fun NavGraphBuilder.compatGraphs(
    context: Context,
    isLoggedIn: Boolean
) {
    activity<CompatDestination.Debug> {
        activityClass = DebugActivity::class
    }
    fragment<DebugModalBottomSheetFragment, CompatDestination.DebugModalBottomSheet> {
        label = context.getString(R.string.title_activity_debug_modal_bottom_sheet)
    }

    fragment<UpdatesFragment, CompatDestination.Updates> {
        label = context.getString(R.string.title_activity_updates)
        deepLink(UriUpdates)
    }

    fragment<HelpListFragment, CompatDestination.Help> {
        label = context.getString(R.string.title_activity_help)
    }

    aboutGraph(context = context)
    context(context) {
        authGraph(isLoggedIn = isLoggedIn)
        taskGraph()
    }

    fragment<CalendarFragment, CompatDestination.Calendar> {
        label = context.getString(R.string.bottom_nav_calendar)
    }

    fragment<SettingsFragment, CompatDestination.Settings> {
        fromApi(Build.VERSION_CODES.N) {
            deepLink {
                action = Intent.ACTION_APPLICATION_PREFERENCES
            }
        }
        deepLink(UriSettings)
        label = context.getString(R.string.title_activity_settings)
    }
}
