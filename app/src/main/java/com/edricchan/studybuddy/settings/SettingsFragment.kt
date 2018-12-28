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
import com.edricchan.studybuddy.utils.DataUtil

class SettingsFragment : PreferenceFragmentCompat() {

	private var preferences: SharedPreferences? = null

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey)
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
		if (BuildConfig.DEBUG) {
			findPreference<Preference>(DataUtil.prefHeaderDebug).isVisible = true
		}
		val showHeaderSummaryPref = findPreference<SwitchPreferenceCompat>(DataUtil.prefShowHeaderSummary)
		showHeaderSummaryPref.setOnPreferenceClickListener {
			updateHeaderSummaries(preferences!!.getBoolean(DataUtil.prefShowHeaderSummary, false))
			true
		}
		findPreference<Preference>(DataUtil.prefHeaderAccount)
				.setOnPreferenceClickListener {
					startActivity(Intent(context, AccountActivity::class.java))
					true
				}
		updateHeaderSummaries(preferences!!.getBoolean(DataUtil.prefShowHeaderSummary, false))
	}

	private fun updateHeaderSummaries(showHeaderSummaries: Boolean) {
		if (showHeaderSummaries) {
			findPreference<Preference>(DataUtil.prefHeaderAbout).setSummary(R.string.pref_header_version_summary)
			findPreference<Preference>(DataUtil.prefHeaderAccount).setSummary(R.string.pref_header_account_summary)
			findPreference<Preference>(DataUtil.prefHeaderDebug).setSummary(R.string.pref_header_debug_summary)
			findPreference<Preference>(DataUtil.prefHeaderGeneral).setSummary(R.string.pref_header_general_summary)
			findPreference<Preference>(DataUtil.prefHeaderSync).setSummary(R.string.pref_header_data_sync_summary)
			findPreference<Preference>(DataUtil.prefHeaderTodo).setSummary(R.string.pref_header_todo_summary)
		} else {
			findPreference<Preference>(DataUtil.prefHeaderAbout).summary = null
			findPreference<Preference>(DataUtil.prefHeaderAccount).summary = null
			findPreference<Preference>(DataUtil.prefHeaderDebug).summary = null
			findPreference<Preference>(DataUtil.prefHeaderGeneral).summary = null
			findPreference<Preference>(DataUtil.prefHeaderSync).summary = null
			findPreference<Preference>(DataUtil.prefHeaderTodo).summary = null
		}
	}
}
