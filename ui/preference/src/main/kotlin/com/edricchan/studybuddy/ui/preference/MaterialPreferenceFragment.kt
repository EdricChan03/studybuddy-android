package com.edricchan.studybuddy.ui.preference

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

abstract class MaterialPreferenceFragment : PreferenceFragmentCompat() {
    /**
     * Whether insets should be applied to this fragment's contents.
     *
     * Currently, when set to `true`, the
     * [androidx.core.graphics.Insets.bottom] padding from the
     * [WindowInsetsCompat.Type.navigationBars] insets will be applied to the
     * [getListView].
     */
    protected open val shouldApplyInsets = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (shouldApplyInsets) {
            listView.clipToPadding = false
            ViewCompat.setOnApplyWindowInsetsListener(listView) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())

                v.updatePadding(bottom = insets.bottom)

                windowInsets
            }
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        when (preference) {
            is ListPreference -> showListPreferenceDialog(preference)
            is MultiSelectListPreference -> showMultiSelectListPreferenceDialog(preference)
            is EditTextPreference -> showEditTextPreferenceDialog(preference)
            else -> super.onDisplayPreferenceDialog(preference)
        }
    }
}
