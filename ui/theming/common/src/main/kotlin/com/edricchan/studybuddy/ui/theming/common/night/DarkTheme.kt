package com.edricchan.studybuddy.ui.theming.common.night

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

/** Checks if the receiver [Resources] has dark theme enabled. */
val Resources.isDarkThemeEnabled: Boolean
    get() = (configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

/**
 * Checks if the receiver [Context] is using dark theme.
 * @see [Resources.isDarkThemeEnabled]
 */
val Context.isDarkThemeEnabled: Boolean get() = resources.isDarkThemeEnabled
