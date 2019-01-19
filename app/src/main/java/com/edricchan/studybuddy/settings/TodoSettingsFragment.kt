package com.edricchan.studybuddy.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference

import androidx.preference.PreferenceFragmentCompat

import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.DataUtils

class TodoSettingsFragment : PreferenceFragmentCompat() {
	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_todos, rootKey)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
			intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity!!.packageName)
			intent.putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notification_channel_weekly_summary_id))
			findPreference<Preference>(DataUtils.prefWeeklySummaryNotificationOpts).intent = intent
		} else {
			// Hide the preference as notification channels aren't implemented on lower versions of Android
			findPreference<Preference>(DataUtils.prefWeeklySummaryNotificationOpts).isVisible = false
		}
	}
}
