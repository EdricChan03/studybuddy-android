package com.edricchan.studybuddy.utils

import android.provider.Settings
import androidx.core.net.toUri
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.exts.android.buildIntent

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
