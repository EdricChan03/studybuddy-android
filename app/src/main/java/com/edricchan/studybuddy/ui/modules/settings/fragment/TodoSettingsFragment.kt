package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment

class TodoSettingsFragment : MaterialPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_todos, rootKey)
    }
}
