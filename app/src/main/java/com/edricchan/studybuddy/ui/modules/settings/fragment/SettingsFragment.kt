package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants
import com.edricchan.studybuddy.constants.sharedprefs.FeatureFlagsPrefConstants
import com.edricchan.studybuddy.ui.modules.about.fragment.AboutFragment
import com.edricchan.studybuddy.ui.modules.account.AccountActivity
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.utils.FeatureFlagsUtils
import com.edricchan.studybuddy.utils.SharedUtils

class SettingsFragment : MaterialPreferenceFragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var devModeOpts: SharedPreferences
    private lateinit var featureFlagsUtils: FeatureFlagsUtils

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_headers, rootKey)
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        devModeOpts =
            requireContext().getSharedPreferences(DevModePrefConstants.FILE_DEV_MODE,
                Context.MODE_PRIVATE)
        featureFlagsUtils = FeatureFlagsUtils(requireContext())
        findPreference<Preference>(Constants.prefHeaderDebug)?.apply {
            isVisible = SharedUtils.isDevMode(context)
            if (SharedUtils.isDevMode(context, true)) {
                setTitle(R.string.pref_header_dev_mode_opts_title)
            }
        }
        findPreference<SwitchPreferenceCompat>(Constants.prefShowHeaderSummary)?.apply {
            if (!BuildConfig.DEBUG) {
                // Hide the preference
                isVisible = false
            }
            setOnPreferenceClickListener {
                updateHeaderSummaries(
                    preferences.getBoolean(
                        Constants.prefShowHeaderSummary,
                        false
                    )
                )
                true
            }
        }
        initPreferences()
        updateHeaderSummaries(preferences!!.getBoolean(Constants.prefShowHeaderSummary, false))
    }

    // Initialises the preferences as the app ID can't be hardcoded in the XML file due to the suffixes
    private fun initPreferences() {
        findPreference<Preference>(Constants.prefHeaderGeneral)?.fragment =
            GeneralSettingsFragment::class.java.name
        findPreference<Preference>(Constants.prefHeaderTodo)?.fragment =
            TodoSettingsFragment::class.java.name
        findPreference<Preference>(Constants.prefHeaderDebug)?.fragment =
            DebugSettingsFragment::class.java.name
        findPreference<Preference>(Constants.prefHeaderUpdates)?.fragment =
            UpdateSettingsFragment::class.java.name
        findPreference<Preference>(Constants.prefHeaderAbout)?.apply {
            fragment = if (featureFlagsUtils
                    .hasFeatureFlagEnabled(FeatureFlagsPrefConstants.FEATURE_FLAG_ABOUT_APP_V2_ENABLED)
            ) {
                AboutFragment::class.java.name
            } else {
                AboutSettingsFragment::class.java.name
            }
        }
        findPreference<Preference>(Constants.prefHeaderAccount)?.intent =
            Intent(context, AccountActivity::class.java)
    }

    private fun updateHeaderSummaries(showHeaderSummaries: Boolean) {
        if (showHeaderSummaries) {
            findPreference<Preference>(Constants.prefHeaderAbout)?.setSummary(R.string.pref_header_about_summary)
            findPreference<Preference>(Constants.prefHeaderUpdates)?.setSummary(R.string.pref_header_updates_summary)
            findPreference<Preference>(Constants.prefHeaderAccount)?.setSummary(R.string.pref_header_account_summary)
            findPreference<Preference>(Constants.prefHeaderDebug)?.setSummary(R.string.pref_header_debug_summary)
            findPreference<Preference>(Constants.prefHeaderGeneral)?.setSummary(R.string.pref_header_general_summary)
            findPreference<Preference>(Constants.prefHeaderTodo)?.setSummary(R.string.pref_header_todo_summary)
        } else {
            findPreference<Preference>(Constants.prefHeaderAbout)?.summary = null
            findPreference<Preference>(Constants.prefHeaderUpdates)?.summary = null
            findPreference<Preference>(Constants.prefHeaderAccount)?.summary = null
            findPreference<Preference>(Constants.prefHeaderDebug)?.summary = null
            findPreference<Preference>(Constants.prefHeaderGeneral)?.summary = null
            findPreference<Preference>(Constants.prefHeaderTodo)?.summary = null
        }
    }
}
