package com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags

import android.os.Bundle
import androidx.preference.Preference
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.FeatureFlagsPrefConstants
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.utils.FeatureFlagsUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FeatureFlagsSettingsFragment : MaterialPreferenceFragment() {
    private lateinit var featureFlagsUtils: FeatureFlagsUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        featureFlagsUtils = FeatureFlagsUtils(requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = FeatureFlagsPrefConstants.FILE_FEATURE_FLAGS
        setPreferencesFromResource(R.xml.pref_feature_flags, rootKey)
        findPreference<Preference>(Constants.featureFlagsReset)?.setOnPreferenceClickListener {
            // Show a confirmation dialog to reset
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.feature_flags_confirm_reset_dialog_title)
                setMessage(R.string.feature_flags_confirm_reset_dialog_msg)
                setNegativeButton(R.string.dialog_action_cancel, null)
                setPositiveButton(R.string.dialog_action_confirm) { dialog, _ ->
                    featureFlagsUtils.resetFeatureFlags(
                        R.string.feature_flags_successfully_reset_toast_msg
                    )
                    dialog.dismiss()
                }
            }.show()
            true
        }
    }

}
