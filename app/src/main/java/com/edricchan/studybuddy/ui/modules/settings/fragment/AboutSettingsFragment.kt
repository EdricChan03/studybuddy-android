package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.Preference
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.ui.preference.MaterialPreferenceFragment
import com.edricchan.studybuddy.utils.appDetailsIntent
import com.edricchan.studybuddy.utils.dev.DevModePrefConstants
import com.edricchan.studybuddy.utils.licenseIntent
import com.edricchan.studybuddy.utils.web.launchUri
import kotlin.properties.Delegates

class AboutSettingsFragment : MaterialPreferenceFragment() {

    private lateinit var preferences: SharedPreferences
    private lateinit var devModeOptions: SharedPreferences

    // Number of times the version code has been clicked
    private var devHitCountdown by Delegates.notNull<Int>()

    // Max number of clicks to unlock
    private val tapsToDev: Int = 7

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_about, rootKey)
        preferences = requireContext().defaultSharedPreferences
        devModeOptions =
            requireContext().getSharedPreferences(
                DevModePrefConstants.FILE_DEV_MODE,
                Context.MODE_PRIVATE
            )

        devHitCountdown = if (devModeOptions
                .getBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)
        ) {
            -1
        } else {
            tapsToDev
        }
        findPreference<Preference>(Constants.prefAboutAppVersion)?.apply {
            summary = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
            setOnPreferenceClickListener {
                if (!devModeOptions.getBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)) {
                    Log.d(TAG, "User has disabled overriding of developer mode.")
                    return@setOnPreferenceClickListener false
                }
                // See https://jaredrummler.com/2016/05/01/tutorial-easter-eggs/ for more info
                // Based on implementation from Android's Build Number preference
                // (See https://github.com/aosp-mirror/platform_packages_apps_settings/blob/master/src/com/android/settings/deviceinfo/BuildNumberPreferenceController.java)
                if (devHitCountdown > 0) {
                    devHitCountdown--
                    if (devHitCountdown == 0) {
                        // Add 1 count back
                        devHitCountdown++
                        requireContext().showMaterialAlertDialog {
                            setTitle(R.string.dev_mode_confirm_enable_dialog_title)
                            setMessage(R.string.dev_mode_confirm_enable_dialog_msg)
                            setNeutralButton(R.string.dialog_action_cancel, null)
                            setNegativeButton(R.string.dialog_action_disable) { dialog, _ ->
                                devModeOptions.edit {
                                    putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)
                                    showToast(
                                        R.string.dev_mode_disabled,
                                        Toast.LENGTH_SHORT
                                    )
                                }
                                dialog.dismiss()
                            }
                            setPositiveButton(R.string.dialog_action_enable) { dialog, _ ->
                                devModeOptions.edit {
                                    putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, true)
                                    showToast(
                                        R.string.dev_mode_on,
                                        Toast.LENGTH_SHORT
                                    )
                                }
                                dialog.dismiss()
                            }
                        }
                    } else if (devHitCountdown > 0 && devHitCountdown < (tapsToDev - 2)) {
                        showToast(
                            context.resources.getQuantityString(
                                R.plurals.dev_mode_countdown, devHitCountdown,
                                devHitCountdown
                            ), Toast.LENGTH_SHORT
                        )
                    }
                } else if (devHitCountdown < 0) {
                    showToast(R.string.dev_mode_already, Toast.LENGTH_LONG)
                }
                true
            }
        }
        findPreference<Preference>(Constants.prefAboutAppVersionCode)?.summary =
            BuildConfig.VERSION_CODE.toString()
        findPreference<Preference>(Constants.prefAboutAppBuildVariant)?.summary =
            BuildConfig.BUILD_TYPE
        findPreference<Preference>(Constants.prefAboutSourceCode)?.setOnPreferenceClickListener {
            requireContext().launchUri(Constants.uriSrcCode)
            true
        }
        findPreference<Preference>(Constants.prefAboutAppAuthor)?.setOnPreferenceClickListener {
            requireContext().launchUri(Constants.uriAuthorWebsite)
            true
        }
        findPreference<Preference>(Constants.prefAboutAppInfo)?.intent = appDetailsIntent()
        findPreference<Preference>(Constants.prefAboutLicenses)?.intent =
            requireContext().licenseIntent
    }
}
