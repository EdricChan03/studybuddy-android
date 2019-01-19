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
import com.edricchan.studybuddy.utils.DataUtils

class SettingsFragment : PreferenceFragmentCompat() {

	private var preferences: SharedPreferences? = null

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey)
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
		if (BuildConfig.DEBUG) {
			findPreference<Preference>(DataUtils.prefHeaderDebug).isVisible = true
		}
		val showHeaderSummaryPref = findPreference<SwitchPreferenceCompat>(DataUtils.prefShowHeaderSummary)
		showHeaderSummaryPref.setOnPreferenceClickListener {
			updateHeaderSummaries(preferences!!.getBoolean(DataUtils.prefShowHeaderSummary, false))
			true
		}
		findPreference<Preference>(DataUtils.prefHeaderAccount)
				.setOnPreferenceClickListener {
					startActivity(Intent(context, AccountActivity::class.java))
					true
				}
		updateHeaderSummaries(preferences!!.getBoolean(DataUtils.prefShowHeaderSummary, false))
	}

	private fun updateHeaderSummaries(showHeaderSummaries: Boolean) {
		if (showHeaderSummaries) {
			findPreference<Preference>(DataUtils.prefHeaderAbout).setSummary(R.string.pref_header_version_summary)
			findPreference<Preference>(DataUtils.prefHeaderAccount).setSummary(R.string.pref_header_account_summary)
			findPreference<Preference>(DataUtils.prefHeaderDebug).setSummary(R.string.pref_header_debug_summary)
			findPreference<Preference>(DataUtils.prefHeaderGeneral).setSummary(R.string.pref_header_general_summary)
			findPreference<Preference>(DataUtils.prefHeaderSync).setSummary(R.string.pref_header_data_sync_summary)
			findPreference<Preference>(DataUtils.prefHeaderTodo).setSummary(R.string.pref_header_todo_summary)
		} else {
			findPreference<Preference>(DataUtils.prefHeaderAbout).summary = null
			findPreference<Preference>(DataUtils.prefHeaderAccount).summary = null
			findPreference<Preference>(DataUtils.prefHeaderDebug).summary = null
			findPreference<Preference>(DataUtils.prefHeaderGeneral).summary = null
			findPreference<Preference>(DataUtils.prefHeaderSync).summary = null
			findPreference<Preference>(DataUtils.prefHeaderTodo).summary = null
		}
	}
}
