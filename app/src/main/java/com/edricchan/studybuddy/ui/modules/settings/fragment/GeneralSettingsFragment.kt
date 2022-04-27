package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.os.Bundle
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment

class GeneralSettingsFragment : MaterialPreferenceFragment() {

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_general, rootKey)
    }
}
