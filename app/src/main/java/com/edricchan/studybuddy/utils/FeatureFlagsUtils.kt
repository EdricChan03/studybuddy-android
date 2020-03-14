package com.edricchan.studybuddy.utils

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.edit
import com.edricchan.studybuddy.constants.sharedprefs.FeatureFlagsPrefConstants
import com.edricchan.studybuddy.extensions.showToast

/**
 * Initialises an instance of the utilities
 * @param context The context to retrieve the [android.content.SharedPreferences] from
 */
class FeatureFlagsUtils(
    val context: Context
) {
    val featureFlagsPrefs: SharedPreferences = context.getSharedPreferences(
        FeatureFlagsPrefConstants.FILE_FEATURE_FLAGS, Context.MODE_PRIVATE
    )

    /**
     * Whether the specified feature flag is enabled
     * @param featureFlag The feature flag as a key (See [FeatureFlagsPrefConstants] for all
     * available keys)
     * @return `true` if the feature flag is enabled, `false` otherwise
     */
    fun hasFeatureFlagEnabled(@FeatureFlagsPrefConstants.FeatureFlag featureFlag: String): Boolean =
        featureFlagsPrefs.getBoolean(featureFlag, false)

    /**
     * Resets the feature flags with an optional [messageRes] argument specifying the message to be
     * shown when the feature flags have been reset.
     * @param messageRes A string resource ID to the message to be shown on reset.
     */
    fun resetFeatureFlags(@StringRes messageRes: Int) {
        resetFeatureFlags(context.getString(messageRes))
    }

    /**
     * Resets the feature flags. Optionally, the [message] parameter can be specified to override
     * the default message which would be used for the toast.
     * @param message The message to be shown as a [Toast] when the feature flags have been reset.
     */
    fun resetFeatureFlags(message: String? = null) {
        featureFlagsPrefs.edit {
            clear()
            if (message != null) {
                context.showToast(message, Toast.LENGTH_LONG)
            }
        }
    }
}