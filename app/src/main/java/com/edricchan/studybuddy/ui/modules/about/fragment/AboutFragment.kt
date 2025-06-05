package com.edricchan.studybuddy.ui.modules.about.fragment

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.danielstone.materialaboutlibrary.ConvenienceBuilder
import com.danielstone.materialaboutlibrary.MaterialAboutFragment
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.compat.navigation.about.navigateToLicenses
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.formatISO
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.utils.appDetailsIntent
import com.edricchan.studybuddy.utils.dev.DevModePrefConstants
import com.edricchan.studybuddy.utils.web.launchUri
import java.time.Instant
import kotlin.properties.Delegates

internal fun MaterialAboutCard(init: MaterialAboutCard.Builder.() -> Unit): MaterialAboutCard =
    MaterialAboutCard.Builder().apply(init).build()

internal fun MaterialAboutActionItem(init: MaterialAboutActionItem.Builder.() -> Unit): MaterialAboutActionItem =
    MaterialAboutActionItem.Builder().apply(init).build()

internal fun MaterialAboutList(init: MaterialAboutList.Builder.() -> Unit): MaterialAboutList =
    MaterialAboutList.Builder().apply(init).build()

internal fun MaterialAboutList.Builder.addCard(init: MaterialAboutCard.Builder.() -> Unit): MaterialAboutList.Builder =
    addCard(MaterialAboutCard(init))

internal fun MaterialAboutCard.Builder.addItem(init: MaterialAboutActionItem.Builder.() -> Unit): MaterialAboutCard.Builder =
    addItem(MaterialAboutActionItem(init))

class AboutFragment : MaterialAboutFragment() {
    private lateinit var devModeOptions: SharedPreferences

    // Number of times the version code has been clicked
    private var devHitCountdown by Delegates.notNull<Int>()

    // Max number of clicks to unlock
    private val tapsToDev: Int = 7

    private lateinit var navController: NavController

    override fun getMaterialAboutList(context: Context?): MaterialAboutList {
        initFragment()
        val buildTime = Instant.ofEpochMilli(BuildConfig.BUILD_TIME).formatISO()

        return MaterialAboutList {
            addCard {
                addItem(ConvenienceBuilder.createAppTitleItem(context))
                addItem {
                    text(R.string.about_frag_app_info_card_version_title)
                    subText(
                        """
                        ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})
                        Build type: ${BuildConfig.BUILD_TYPE}
                        Build time: $buildTime
                        """.trimIndent()
                    )
                    icon(R.drawable.ic_info_outline_24dp)
                    setOnClickAction(::onInfoClick)
                }
                addItem {
                    text(R.string.about_frag_app_info_card_open_app_info_title)
                    subText(R.string.about_frag_app_info_card_open_app_info_subtitle)
                    icon(R.drawable.ic_info_outline_24dp)
                    setOnClickAction {
                        startActivity(appDetailsIntent())
                    }
                }
                addItem(
                    createWebsiteActionItem(
                        Constants.uriSrcCode,
                        R.string.about_frag_app_info_card_view_source_code_title,
                        R.string.about_frag_app_info_card_view_source_code_subtitle,
                        R.drawable.ic_code_24dp
                    )
                )
                addItem {
                    text(R.string.about_frag_app_info_card_open_source_licenses_title)
                    icon(R.drawable.ic_file_document_box_outline_24dp)
                    setOnClickAction(navController::navigateToLicenses)
                }
            }

            addCard {
                title(R.string.about_frag_about_author_card_title)
                addItem {
                    text(R.string.about_frag_about_author_card_author_name_title)
                    icon(R.drawable.ic_account_circle_outline_24dp)
                }
                addItem(
                    createWebsiteActionItem(
                        Constants.uriAuthorWebsite,
                        R.string.about_frag_about_author_card_view_author_website_title
                    )
                )
            }
        }
    }

    private fun onInfoClick() {
        if (!devModeOptions.getBoolean(
                DevModePrefConstants.DEV_MODE_ENABLED,
                false
            )
        ) {
            Log.d(TAG, "User has disabled overriding of developer mode.")
        }
        // See https://jaredrummler.com/blog/android/2016-05-01-tutorial-easter-eggs/
        // for more info
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
                            putBoolean(
                                DevModePrefConstants.DEV_MODE_ENABLED,
                                false
                            )
                            showToast(
                                R.string.dev_mode_disabled,
                                Toast.LENGTH_SHORT
                            )
                        }
                        dialog.dismiss()
                    }
                    setPositiveButton(R.string.dialog_action_enable) { dialog, _ ->
                        devModeOptions.edit {
                            putBoolean(
                                DevModePrefConstants.DEV_MODE_ENABLED,
                                true
                            )
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
                    requireContext().resources.getQuantityString(
                        R.plurals.dev_mode_countdown, devHitCountdown,
                        devHitCountdown
                    ), Toast.LENGTH_SHORT
                )
            }
        } else if (devHitCountdown < 0) {
            showToast(
                R.string.dev_mode_already,
                Toast.LENGTH_LONG
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
    }

    private fun createWebsiteActionItem(
        websiteUri: Uri, @StringRes textRes: Int,
        @StringRes subTextRes: Int? = null,
        @DrawableRes iconRes: Int = R.drawable.ic_open_in_new_24dp
    ): MaterialAboutActionItem = MaterialAboutActionItem {
        icon(iconRes)
        text(textRes)
        subText(subTextRes ?: 0)
        setOnClickAction {
            requireContext().launchUri(websiteUri)
        }
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
