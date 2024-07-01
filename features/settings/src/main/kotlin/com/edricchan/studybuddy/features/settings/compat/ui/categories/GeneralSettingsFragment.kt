package com.edricchan.studybuddy.features.settings.compat.ui.categories

import android.os.Bundle
import android.util.Log
import androidx.preference.SwitchPreferenceCompat
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.ui.theming.PREF_DYNAMIC_THEME
import com.edricchan.studybuddy.ui.theming.applyDynamicTheme
import com.edricchan.studybuddy.ui.theming.isDynamicColorAvailable

class GeneralSettingsFragment : MaterialPreferenceFragment() {
    private val logTag = TAG

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)

        findPreference<SwitchPreferenceCompat>(PREF_DYNAMIC_THEME)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val isDynamicThemeEnabled = newValue as? Boolean ?: false
                Log.d(
                    logTag,
                    "isDynamicThemeEnabled: $isDynamicThemeEnabled"
                )

                // Update dynamic theme
                requireContext().applyDynamicTheme()

                activity?.recreate()

                true
            }
            isEnabled = isDynamicColorAvailable
        }
    }
}
