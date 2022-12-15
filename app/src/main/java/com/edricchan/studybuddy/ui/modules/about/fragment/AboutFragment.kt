package com.edricchan.studybuddy.ui.modules.about.fragment

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.edit
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.MaterialAboutFragment
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.DevModePrefConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.formatISO
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.utils.appDetailsIntent
import com.edricchan.studybuddy.utils.launchUri
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.time.Instant
import kotlin.properties.Delegates

class AboutFragment : MaterialAboutFragment() {
    private lateinit var devModeOptions: SharedPreferences

    // Number of times the version code has been clicked
    private var devHitCountdown by Delegates.notNull<Int>()

    // Max number of clicks to unlock
    private val tapsToDev: Int = 7

    override fun getMaterialAboutList(context: Context?): MaterialAboutList {
        initFragment()
        val buildTime = Instant.ofEpochMilli(BuildConfig.BUILD_TIME).formatISO()

        val appInfoCard = MaterialAboutCard.Builder()
            .addItem(ConvenienceBuilder.createAppTitleItem(context))
            .addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_frag_app_info_card_version_title)
                .subText(
                    """
                    ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})
                    Build type: ${BuildConfig.BUILD_TYPE}
                    Build time: $buildTime
                """.trimIndent()
                )
                .icon(R.drawable.ic_info_outline_24dp)
                .setOnClickAction {
                    if (!devModeOptions.getBoolean(
                            DevModePrefConstants.DEV_MODE_ENABLED,
                            false
                        )
                    ) {
                        Log.d(TAG, "User has disabled overriding of developer mode.")
                    }
                    // See https://jaredrummler.com/2016/05/01/tutorial-easter-eggs/ for more info
                    // Based on implementation from Android's Build Number preference
                    // (See https://github.com/aosp-mirror/platform_packages_apps_settings/blob/master/src/com/android/settings/deviceinfo/BuildNumberPreferenceController.java)
                    if (devHitCountdown > 0) {
                        devHitCountdown--
                        if (devHitCountdown == 0) {
                            // Add 1 count back
                            devHitCountdown++
                            MaterialAlertDialogBuilder(requireContext()).apply {
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
                                context, requireContext().resources.getQuantityString(
                                    R.plurals.dev_mode_countdown, devHitCountdown,
                                    devHitCountdown
                                ), Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else if (devHitCountdown < 0) {
                        Toast.makeText(context, R.string.dev_mode_already, Toast.LENGTH_LONG).show()
                    }
                }
                .build())
            .addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_frag_app_info_card_open_app_info_title)
                .subText(R.string.about_frag_app_info_card_open_app_info_subtitle)
                .icon(R.drawable.ic_info_outline_24dp)
                .setOnClickAction {
                    startActivity(appDetailsIntent())
                }
                .build())
            .addItem(
                createWebsiteActionItem(
                    Constants.uriSrcCode,
                    R.string.about_frag_app_info_card_view_source_code_title,
                    R.string.about_frag_app_info_card_view_source_code_subtitle,
                    R.drawable.ic_code_24dp
                )
            )
            .addItem(MaterialAboutActionItem.Builder()
                .text(R.string.about_frag_app_info_card_open_source_licenses_title)
                .icon(R.drawable.ic_file_document_box_outline_24dp)
                .setOnClickAction {
                    startActivity<OssLicensesMenuActivity>()
                }
                .build())
            .build()

        val aboutAuthorCard = MaterialAboutCard.Builder()
            .title(R.string.about_frag_about_author_card_title)
            .addItem(
                MaterialAboutActionItem.Builder()
                    .text(R.string.about_frag_about_author_card_author_name_title)
                    .icon(R.drawable.ic_account_circle_outline_24dp)
                    .build()
            )
            .addItem(
                createWebsiteActionItem(
                    Constants.uriAuthorWebsite,
                    R.string.about_frag_about_author_card_view_author_website_title
                )
            )
            .build()

        return MaterialAboutList.Builder()
            .addCard(appInfoCard)
            .addCard(aboutAuthorCard)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
    }

    private fun createWebsiteActionItem(
        websiteUri: Uri, @StringRes textRes: Int,
        @StringRes subTextRes: Int? = null,
        @DrawableRes iconRes: Int = R.drawable.ic_open_in_new_24dp
    ):
        MaterialAboutActionItem {
        return MaterialAboutActionItem.Builder()
            .icon(iconRes)
            .text(textRes)
            .subText(subTextRes ?: 0)
            .setOnClickAction {
                requireContext().launchUri(websiteUri)
            }
            .build()
    }

    private fun initFragment() {
        devModeOptions = requireContext().getSharedPreferences(
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
    }
}
