package com.edricchan.studybuddy.constants.sharedprefs

import androidx.annotation.StringDef

/**
 * Constants to be used for feature flags
 */
object FeatureFlagsPrefConstants {
	/**
	 * File to be used to store the information about the feature flags
	 */
	const val FILE_FEATURE_FLAGS = "feature_flags"

	/**
	 * Feature flag to indicate if the calendar UI should be enabled
	 */
	const val FEATURE_FLAG_CALENDAR_ENABLED = "feature_flags_calendar_enabled"

	/**
	 * Feature flag to indicate if v2 of the UI should be enabled
	 */
	const val FEATURE_FLAG_INTERFACE_V2_ENABLED = "feature_flags_interface_v2_enabled"

	/**
	 * Feature flag to indicate if v2 of the about app UI should be enabled
	 */
	const val FEATURE_FLAG_ABOUT_APP_V2_ENABLED = "feature_flags_about_app_v2_enabled"

	@StringDef(
			FeatureFlagsPrefConstants.FEATURE_FLAG_CALENDAR_ENABLED,
			FeatureFlagsPrefConstants.FEATURE_FLAG_INTERFACE_V2_ENABLED,
			FeatureFlagsPrefConstants.FEATURE_FLAG_ABOUT_APP_V2_ENABLED
	)
	annotation class FeatureFlag
}