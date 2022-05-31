package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import android.util.Log
import androidx.preference.SwitchPreferenceCompat
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.utils.ThemeUtils
import com.edricchan.studybuddy.utils.themeUtils
import com.google.android.material.color.DynamicColors

class GeneralSettingsFragment : MaterialPreferenceFragment() {
    private val logTag = TAG
    private lateinit var themeUtils: ThemeUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeUtils = requireContext().themeUtils
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)

        findPreference<SwitchPreferenceCompat>(ThemeUtils.PREF_DYNAMIC_THEME)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val isDynamicThemeEnabled = newValue as? Boolean ?: false
                Log.d(
                    logTag,
                    "isDynamicThemeEnabled: $isDynamicThemeEnabled"
                )

                // Update dynamic theme
                themeUtils.applyTheme()

                activity?.recreate()

                true
            }
            isEnabled = DynamicColors.isDynamicColorAvailable()
        }
    }
}
