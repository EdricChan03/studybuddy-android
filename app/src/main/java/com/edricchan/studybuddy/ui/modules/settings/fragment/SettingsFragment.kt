package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.preference.Preference
import androidx.preference.PreferenceHeaderFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.FeatureFlagsPrefConstants
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.androidx.preference.setFragment
import com.edricchan.studybuddy.features.auth.account.compat.AccountFragment
import com.edricchan.studybuddy.features.settings.general.ui.compat.GeneralSettingsFragment
import com.edricchan.studybuddy.features.settings.task.ui.compat.TodoSettingsFragment
import com.edricchan.studybuddy.features.settings.updates.ui.compat.UpdateSettingsFragment
import com.edricchan.studybuddy.ui.modules.about.fragment.AboutFragment
import com.edricchan.studybuddy.ui.modules.debug.DebugFragment
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.utils.FeatureFlagsUtils
import com.edricchan.studybuddy.utils.dev.isDevMode
import com.edricchan.studybuddy.features.settings.R as SettingsR

class SettingsFragment : PreferenceHeaderFragmentCompat() {
    override fun onCreatePreferenceHeader() = SettingsHeader()

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            // No-op
        }

        // Handle up button when the detail view is open so that a single
        // back press doesn't exit the entire fragment
        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId != android.R.id.home) return false

            // Both the list and the detail fragments are shown, or they're
            // already closed
            if (!slidingPaneLayout.isSlideable || !slidingPaneLayout.isOpen) return false

            slidingPaneLayout.close()
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner)
    }

    class SettingsHeader : MaterialPreferenceFragment() {
        private lateinit var preferences: SharedPreferences
        private lateinit var featureFlagsUtils: FeatureFlagsUtils

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_headers, rootKey)
            preferences = requireContext().defaultSharedPreferences

            featureFlagsUtils = FeatureFlagsUtils(requireContext())
            findPreference<Preference>(Constants.prefHeaderDebug)?.apply {
                with(requireContext().isDevMode(useSharedPrefsOnly = true)) {
                    isVisible = this
                    if (this) setTitle(R.string.pref_header_dev_mode_opts_title)
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
            updateHeaderSummaries(preferences.getBoolean(Constants.prefShowHeaderSummary, false))
        }

        // The FQN to the fragments can't be hardcoded in the XML file due to the
        // dynamically set app suffix for the nightly and debug flavours, so we have to
        // set it programmatically
        private fun initPreferences() {
            findPreference<Preference>(Constants.prefHeaderGeneral)?.setFragment<GeneralSettingsFragment>()
            findPreference<Preference>(Constants.prefHeaderTodo)?.setFragment<TodoSettingsFragment>()
            findPreference<Preference>(Constants.prefHeaderDebug)?.setFragment<DebugFragment>()
            findPreference<Preference>(Constants.prefHeaderUpdates)?.setFragment<UpdateSettingsFragment>()
            findPreference<Preference>(Constants.prefHeaderAbout)?.setFragment {
                if (featureFlagsUtils
                        .hasFeatureFlagEnabled(FeatureFlagsPrefConstants.FEATURE_FLAG_ABOUT_APP_V2_ENABLED)
                ) {
                    AboutFragment::class
                } else {
                    AboutSettingsFragment::class
                }
            }
            findPreference<Preference>(Constants.prefHeaderAccount)?.setFragment<AccountFragment>()
        }

        private fun updateHeaderSummaries(showHeaderSummaries: Boolean) {
            if (showHeaderSummaries) {
                findPreference<Preference>(Constants.prefHeaderAbout)?.setSummary(R.string.pref_header_about_summary)
                findPreference<Preference>(Constants.prefHeaderUpdates)?.setSummary(SettingsR.string.pref_header_updates_summary)
                findPreference<Preference>(Constants.prefHeaderAccount)?.setSummary(R.string.pref_header_account_summary)
                findPreference<Preference>(Constants.prefHeaderDebug)?.setSummary(R.string.pref_header_debug_summary)
                findPreference<Preference>(Constants.prefHeaderGeneral)?.setSummary(R.string.pref_header_general_summary)
                findPreference<Preference>(Constants.prefHeaderTodo)?.setSummary(SettingsR.string.pref_header_task_summary)
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
}
