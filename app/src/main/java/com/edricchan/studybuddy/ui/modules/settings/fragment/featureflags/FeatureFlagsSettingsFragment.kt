package com.edricchan.studybuddy.ui.modules.settings.fragment.featureflags

import android.os.Bundle
import androidx.preference.Preference
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.FeatureFlagsPrefConstants
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.utils.FeatureFlagsManager

class FeatureFlagsSettingsFragment : MaterialPreferenceFragment() {
    private lateinit var featureFlagsManager: FeatureFlagsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        featureFlagsManager = FeatureFlagsManager(requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = FeatureFlagsPrefConstants.FILE_FEATURE_FLAGS
        setPreferencesFromResource(R.xml.pref_feature_flags, rootKey)
        findPreference<Preference>(Constants.featureFlagsReset)?.setOnPreferenceClickListener {
            // Show a confirmation dialog to reset
            requireContext().showMaterialAlertDialog {
                setTitle(R.string.feature_flags_confirm_reset_dialog_title)
                setMessage(R.string.feature_flags_confirm_reset_dialog_msg)
                setNegativeButton(R.string.dialog_action_cancel, null)
                setPositiveButton(R.string.dialog_action_confirm) { dialog, _ ->
                    featureFlagsManager.resetFeatureFlags(
                        R.string.feature_flags_successfully_reset_toast_msg
                    )
                    dialog.dismiss()
                }
            }
            true
        }
    }

}
