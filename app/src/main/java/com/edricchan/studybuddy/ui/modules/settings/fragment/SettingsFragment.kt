package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.takisoft.preferencex.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

	private var preferences: SharedPreferences? = null

	override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_headers, rootKey)
		preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
		if (BuildConfig.DEBUG) {
			findPreference<Preference>(Constants.prefHeaderDebug)?.isVisible = true
		}
		findPreference<SwitchPreferenceCompat>(Constants.prefShowHeaderSummary)?.apply {
			setOnPreferenceClickListener {
				updateHeaderSummaries(preferences!!.getBoolean(Constants.prefShowHeaderSummary, false))
				true
			}
		}
		initPreferences()
		updateHeaderSummaries(preferences!!.getBoolean(Constants.prefShowHeaderSummary, false))
	}

	// Initialises the preferences as the app ID can't be hardcoded in the XML file due to the suffixes
	private fun initPreferences() {
		findPreference<Preference>(Constants.prefHeaderGeneral)?.fragment = GeneralSettingsFragment::class.java.name
		findPreference<Preference>(Constants.prefHeaderTodo)?.fragment = TodoSettingsFragment::class.java.name
		findPreference<Preference>(Constants.prefHeaderSync)?.fragment = SyncSettingsFragment::class.java.name
		findPreference<Preference>(Constants.prefHeaderDebug)?.fragment = DebugSettingsFragment::class.java.name
		findPreference<Preference>(Constants.prefHeaderUpdates)?.fragment = UpdateSettingsFragment::class.java.name
		findPreference<Preference>(Constants.prefHeaderAbout)?.fragment = AboutSettingsFragment::class.java.name
		findPreference<Preference>(Constants.prefHeaderAccount)?.intent = Intent(context, AccountActivity::class.java)
	}

	private fun updateHeaderSummaries(showHeaderSummaries: Boolean) {
		if (showHeaderSummaries) {
			findPreference<Preference>(Constants.prefHeaderAbout)?.setSummary(R.string.pref_header_about_summary)
			findPreference<Preference>(Constants.prefHeaderUpdates)?.setSummary(R.string.pref_header_updates_summary)
			findPreference<Preference>(Constants.prefHeaderAccount)?.setSummary(R.string.pref_header_account_summary)
			findPreference<Preference>(Constants.prefHeaderDebug)?.setSummary(R.string.pref_header_debug_summary)
			findPreference<Preference>(Constants.prefHeaderGeneral)?.setSummary(R.string.pref_header_general_summary)
			findPreference<Preference>(Constants.prefHeaderSync)?.setSummary(R.string.pref_header_data_sync_summary)
			findPreference<Preference>(Constants.prefHeaderTodo)?.setSummary(R.string.pref_header_todo_summary)
		} else {
			findPreference<Preference>(Constants.prefHeaderAbout)?.summary = null
			findPreference<Preference>(Constants.prefHeaderUpdates)?.summary = null
			findPreference<Preference>(Constants.prefHeaderAccount)?.summary = null
			findPreference<Preference>(Constants.prefHeaderDebug)?.summary = null
			findPreference<Preference>(Constants.prefHeaderGeneral)?.summary = null
			findPreference<Preference>(Constants.prefHeaderSync)?.summary = null
			findPreference<Preference>(Constants.prefHeaderTodo)?.summary = null
		}
	}
}
