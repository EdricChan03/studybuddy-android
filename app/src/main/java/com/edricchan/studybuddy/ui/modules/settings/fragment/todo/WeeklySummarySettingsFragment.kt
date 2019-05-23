package com.edricchan.studybuddy.ui.modules.settings.fragment.todo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.takisoft.preferencex.PreferenceFragmentCompatMasterSwitch

class WeeklySummarySettingsFragment : PreferenceFragmentCompatMasterSwitch() {
	override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_todos_weekly_summary, rootKey)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
			intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity!!.packageName)
			intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_channel_weekly_summary_id))
			findPreference<Preference>(Constants.prefWeeklySummaryNotificationOpts)?.intent = intent
		} else {
			// Hide the preference as notification channels aren't implemented on lower versions of Android
			findPreference<Preference>(Constants.prefWeeklySummaryNotificationOpts)?.isVisible = false
		}
	}

}