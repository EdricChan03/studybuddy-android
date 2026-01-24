package com.edricchan.studybuddy.core.settings.appearance.font

/**
 * The list of desired [TypefaceSetting]s to use for the display and body styles.
 * @property displayStyle Desired [TypefaceSetting] for heading-like text.
 * @property bodyStyle Desired [TypefaceSetting] for body text.
 */
data class TypefaceConfig(
    val displayStyle: TypefaceSetting,
    val bodyStyle: TypefaceSetting
)
