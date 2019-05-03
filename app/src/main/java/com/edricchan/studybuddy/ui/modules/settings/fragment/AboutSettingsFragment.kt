package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateUtils
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.toDate
import com.edricchan.studybuddy.ui.modules.updates.UpdatesActivity
import com.edricchan.studybuddy.utils.Constants
import com.edricchan.studybuddy.utils.SharedPrefConstants
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import java.util.*

class AboutSettingsFragment : PreferenceFragmentCompat() {
	private lateinit var preferences: SharedPreferences
	private lateinit var updateInfoPreferences: SharedPreferences
	private lateinit var lastUpdatedDate: Date
	private lateinit var lastCheckedForUpdatesDate: Date

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putLong(LAST_UPDATED_DATE_TAG, lastUpdatedDate.time)
		outState.putLong(LAST_CHECK_FOR_UPDATES_DATE_TAG, lastCheckedForUpdatesDate.time)
		super.onSaveInstanceState(outState)
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_versions, rootKey)
		val context = activity
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
		updateInfoPreferences = context.getSharedPreferences(SharedPrefConstants.FILE_UPDATE_INFO, Context.MODE_PRIVATE)
		lastUpdatedDate = if (savedInstanceState != null) {
			savedInstanceState.getLong(LAST_UPDATED_DATE_TAG).toDate()
		} else {
			updateInfoPreferences.getLong(SharedPrefConstants.PREF_LAST_UPDATED_DATE, 0L).toDate()
		}
		lastCheckedForUpdatesDate = if (savedInstanceState != null) {
			savedInstanceState.getLong(LAST_CHECK_FOR_UPDATES_DATE_TAG).toDate()
		} else {
			updateInfoPreferences.getLong(SharedPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE, 0L).toDate()
		}
		val appAuthorUrl = Uri.parse("https://edricchan03.github.io")
		findPreference<Preference>(Constants.prefUpdates)?.apply {
			intent = Intent(activity, UpdatesActivity::class.java)
			// Check if the user has updated before
			if (lastUpdatedDate.time != 0L) {
				summary = getString(R.string.pref_updates_summary_last_updated, getRelativeDateTimeString(lastUpdatedDate))
			}
		}
		findPreference<Preference>(Constants.prefUpdates)?.intent = Intent(activity, UpdatesActivity::class.java)
		val appAuthor = findPreference<Preference>(Constants.prefAboutAppAuthor)
		appAuthor?.setOnPreferenceClickListener {
			SharedUtils.launchUri(context, appAuthorUrl, preferences.getBoolean(Constants.prefUseCustomTabs, true))
			true
		}
		val appSrc = findPreference<Preference>(Constants.prefAboutSourceCode)
		appSrc?.setOnPreferenceClickListener {
			SharedUtils.launchUri(context, Constants.uriSrcCode, preferences.getBoolean(Constants.prefUseCustomTabs, true))
			true
		}
		val appVersion = findPreference<Preference>(Constants.prefAboutAppVersion)
		appVersion?.summary = BuildConfig.VERSION_NAME

		val appInfo = findPreference<Preference>(Constants.prefAboutAppInfo)
		appInfo?.intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))

		findPreference<Preference>(Constants.prefAboutLicenses)?.intent = Intent(activity, OssLicensesMenuActivity::class.java)
	}

	private fun getRelativeDateTimeString(date: Date): CharSequence = DateUtils.getRelativeDateTimeString(
			context,
			date.time,
			DateUtils.MINUTE_IN_MILLIS,
			DateUtils.WEEK_IN_MILLIS,
			0
	)

	companion object {
		// Indicates when the app was last updated
		private const val LAST_UPDATED_DATE_TAG = "lastUpdatedDate"
		// Indicates when the app last checked for updates
		private const val LAST_CHECK_FOR_UPDATES_DATE_TAG = "lastCheckForUpdatesDate"
	}
}
