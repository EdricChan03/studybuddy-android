package com.edricchan.studybuddy.ui.modules.settings.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.utils.WebUtils
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.takisoft.preferencex.PreferenceFragmentCompat
import kotlin.properties.Delegates

class AboutSettingsFragment : PreferenceFragmentCompat() {

    private lateinit var preferences: SharedPreferences
    private lateinit var devModeOptions: SharedPreferences
    private lateinit var webUtils: WebUtils
    // Number of times the version code has been clicked
    private var devHitCountdown by Delegates.notNull<Int>()
    // Max number of clicks to unlock
    private val tapsToDev: Int = 7

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.pref_about, rootKey)
        val context = activity
        preferences = PreferenceManager.getDefaultSharedPreferences(context!!)
        devModeOptions =
            context.getSharedPreferences(DevModePrefConstants.FILE_DEV_MODE, Context.MODE_PRIVATE)
        webUtils = WebUtils.getInstance(context)

        devHitCountdown = if (devModeOptions
                .getBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)
        ) {
            -1
        } else {
            tapsToDev
        }
        findPreference<Preference>(Constants.prefAboutAppVersion)?.apply {
            summary = BuildConfig.VERSION_NAME
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
                        MaterialAlertDialogBuilder(context).apply {
                            setTitle(R.string.dev_mode_confirm_enable_dialog_title)
                            setMessage(R.string.dev_mode_confirm_enable_dialog_msg)
                            setNeutralButton(R.string.dialog_action_cancel, null)
                            setNegativeButton(R.string.dialog_action_disable) { dialog, _ ->
                                devModeOptions.edit {
                                    putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, false)
                                    Toast.makeText(
                                        context,
                                        R.string.dev_mode_disabled,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                dialog.dismiss()
                            }
                            setPositiveButton(R.string.dialog_action_enable) { dialog, _ ->
                                devModeOptions.edit {
                                    putBoolean(DevModePrefConstants.DEV_MODE_ENABLED, true)
                                    Toast.makeText(
                                        context,
                                        R.string.dev_mode_on,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                dialog.dismiss()
                            }
                        }.show()
                    } else if (devHitCountdown > 0 && devHitCountdown < (tapsToDev - 2)) {
                        Toast.makeText(
                            context, context.resources.getQuantityString(
                                R.plurals.dev_mode_countdown, devHitCountdown,
                                devHitCountdown
                            ), Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (devHitCountdown < 0) {
                    Toast.makeText(context, R.string.dev_mode_already, Toast.LENGTH_LONG).show()
                }
                true
            }
        }
        findPreference<Preference>(Constants.prefAboutAppVersionCode)?.summary =
            BuildConfig.VERSION_CODE.toString()
        findPreference<Preference>(Constants.prefAboutAppBuildVariant)?.summary =
            BuildConfig.BUILD_TYPE
//		findPreference<Preference>(Constants.prefUpdates)?.intent = Intent(activity, UpdatesActivity::class.java)
        findPreference<Preference>(Constants.prefAboutSourceCode)?.setOnPreferenceClickListener {
            webUtils.launchUri(Constants.uriSrcCode)
            true
        }
        findPreference<Preference>(Constants.prefAboutAppAuthor)?.setOnPreferenceClickListener {
            webUtils.launchUri(Constants.uriAuthorWebsite)
            true
        }
        findPreference<Preference>(Constants.prefAboutAppInfo)?.intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID))
        findPreference<Preference>(Constants.prefAboutLicenses)?.intent =
            Intent(activity, OssLicensesMenuActivity::class.java)
    }
}
