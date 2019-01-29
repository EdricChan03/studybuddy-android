package com.edricchan.studybuddy.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat

import com.edricchan.studybuddy.AccountActivity
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.Constants

class SettingsFragment : PreferenceFragmentCompat() {

	private var preferences: SharedPreferences? = null

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey)
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
		if (BuildConfig.DEBUG) {
			findPreference<Preference>(Constants.prefHeaderDebug).isVisible = true
		}
		val showHeaderSummaryPref = findPreference<SwitchPreferenceCompat>(Constants.prefShowHeaderSummary)
		showHeaderSummaryPref.setOnPreferenceClickListener {
			updateHeaderSummaries(preferences!!.getBoolean(Constants.prefShowHeaderSummary, false))
			true
		}
		findPreference<Preference>(Constants.prefHeaderAccount)
				.setOnPreferenceClickListener {
					startActivity(Intent(context, AccountActivity::class.java))
					true
				}
		updateHeaderSummaries(preferences!!.getBoolean(Constants.prefShowHeaderSummary, false))
	}

	private fun updateHeaderSummaries(showHeaderSummaries: Boolean) {
		if (showHeaderSummaries) {
			findPreference<Preference>(Constants.prefHeaderAbout).setSummary(R.string.pref_header_version_summary)
			findPreference<Preference>(Constants.prefHeaderAccount).setSummary(R.string.pref_header_account_summary)
			findPreference<Preference>(Constants.prefHeaderDebug).setSummary(R.string.pref_header_debug_summary)
			findPreference<Preference>(Constants.prefHeaderGeneral).setSummary(R.string.pref_header_general_summary)
			findPreference<Preference>(Constants.prefHeaderSync).setSummary(R.string.pref_header_data_sync_summary)
			findPreference<Preference>(Constants.prefHeaderTodo).setSummary(R.string.pref_header_todo_summary)
		} else {
			findPreference<Preference>(Constants.prefHeaderAbout).summary = null
			findPreference<Preference>(Constants.prefHeaderAccount).summary = null
			findPreference<Preference>(Constants.prefHeaderDebug).summary = null
			findPreference<Preference>(Constants.prefHeaderGeneral).summary = null
			findPreference<Preference>(Constants.prefHeaderSync).summary = null
			findPreference<Preference>(Constants.prefHeaderTodo).summary = null
		}
	}
}
