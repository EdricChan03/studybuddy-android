package com.edricchan.studybuddy.ui.preference

import androidx.preference.*

abstract class MaterialPreferenceFragment : PreferenceFragmentCompat() {
    override fun onDisplayPreferenceDialog(preference: Preference) {
        when (preference) {
            is ListPreference -> showListPreferenceDialog(preference)
            is MultiSelectListPreference -> showMultiSelectListPreferenceDialog(preference)
            is EditTextPreference -> showEditTextPreferenceDialog(preference)
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}
