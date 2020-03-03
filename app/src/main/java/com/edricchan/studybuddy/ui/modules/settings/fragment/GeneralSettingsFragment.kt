package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import com.edricchan.studybuddy.R
import com.takisoft.preferencex.PreferenceFragmentCompat

class GeneralSettingsFragment : PreferenceFragmentCompat() {
	override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.pref_general, rootKey)
	}
}
