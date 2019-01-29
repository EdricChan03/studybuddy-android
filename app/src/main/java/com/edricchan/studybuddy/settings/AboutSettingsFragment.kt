package com.edricchan.studybuddy.settings

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.UpdatesActivity
import com.edricchan.studybuddy.utils.Constants

class AboutSettingsFragment : PreferenceFragmentCompat() {
	private var preferences: SharedPreferences? = null

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_versions, rootKey)
		val context = activity
		preferences = PreferenceManager.getDefaultSharedPreferences(getContext()!!)
		val appAuthorUrl = Uri.parse("https://github.com/Chan4077")
		findPreference<Preference>(Constants.prefUpdates).intent = Intent(activity, UpdatesActivity::class.java)
		val appAuthor = findPreference<Preference>(Constants.prefAppAuthor)
		appAuthor.setOnPreferenceClickListener {
			SharedUtils.launchUri(context!!, appAuthorUrl, preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
			true
		}
		val appSrc = findPreference<Preference>(Constants.prefAppSrcCode)
		appSrc.setOnPreferenceClickListener {
			SharedUtils.launchUri(context!!, Constants.uriSrcCode, preferences!!.getBoolean(Constants.prefUseCustomTabs, true))
			true
		}
		val appVersion = findPreference<Preference>(Constants.prefAppVersion)
		appVersion.summary = BuildConfig.VERSION_NAME

		val appInfo = findPreference<Preference>(Constants.prefAppInfo)
		appInfo.intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
	}
}
