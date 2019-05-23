package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import com.edricchan.studybuddy.R

class TodoSettingsFragment : com.takisoft.preferencex.PreferenceFragmentCompat() {
	override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_todos, rootKey)
	}
}
