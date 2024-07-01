package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import androidx.preference.Preference
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.settings.tasks.summary.prefWeeklySummaryEnabled
import com.edricchan.studybuddy.exts.androidx.preference.setFragment
import com.edricchan.studybuddy.ui.modules.settings.fragment.todo.WeeklySummarySettingsFragment
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment

class TodoSettingsFragment : MaterialPreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_todos, rootKey)
        findPreference<Preference>(prefWeeklySummaryEnabled)
            ?.setFragment<WeeklySummarySettingsFragment>()
    }
}
