package com.edricchan.studybuddy.utils

import android.content.Context
import android.provider.Settings
import androidx.core.net.toUri
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.exts.android.buildIntent
import com.edricchan.studybuddy.ui.common.licenses.OssLicensesCompatActivity

/** Creates an [android.content.Intent] that opens the given [packageName]'s app details. */
fun appDetailsIntent(packageName: String = BuildConfig.APPLICATION_ID) = buildIntent {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    data = "package:$packageName".toUri()
}

/**
 * Creates an [android.content.Intent] that opens the given receiver string's app details.
 * @receiver The package name to use.
 */
val String.appDetailsIntent get() = appDetailsIntent(this)

/**
 * Creates an [android.content.Intent] that opens the
 * [licenses activity][OssLicensesCompatActivity].
 */
@Suppress("DeprecatedCallableAddReplaceWith")
@Deprecated("Use the LibrariesContainer composable if possible")
val Context.licenseIntent
    get() = buildIntent<OssLicensesCompatActivity>(this)
