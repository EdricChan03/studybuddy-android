package com.edricchan.studybuddy.ui.modules.settings.fragment.todo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.settings.tasks.summary.prefWeeklySummaryEnabled
import com.edricchan.studybuddy.core.settings.tasks.summary.prefWeeklySummaryNotificationOpts
import com.edricchan.studybuddy.exts.android.buildIntent
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragmentMainSwitch
import com.edricchan.studybuddy.ui.preference.mainswitch.MainSwitchPreference

class WeeklySummarySettingsFragment : MaterialPreferenceFragmentMainSwitch() {
    override fun createMainSwitch(context: Context) = MainSwitchPreference(context).apply {
        key = prefWeeklySummaryEnabled
        setTitle(R.string.pref_weekly_summary_masterswitch_enabled_title)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)
        addPreferencesFromResource(R.xml.pref_todos_weekly_summary)

        findPreference<Preference>(prefWeeklySummaryNotificationOpts)?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent = buildIntent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS) {
                    putExtra(Settings.EXTRA_APP_PACKAGE, requireActivity().packageName)
                    putExtra(
                        Settings.EXTRA_CHANNEL_ID,
                        getString(R.string.notification_channel_weekly_summary_id)
                    )
                }
            } else {
                // Hide the preference as notification channels aren't implemented on lower versions of Android
                isVisible = false
            }
        }
    }

}
