package com.edricchan.studybuddy.ui.theming

import android.content.Context
import com.edricchan.studybuddy.ui.theming.common.dynamic.prefDynamicTheme
import com.google.android.material.R as MaterialR

/** Applies the dynamic theme based on whether [shouldApply] is `true`. */
fun Context.applyDynamicTheme(shouldApply: Boolean = prefDynamicTheme) {
    theme.applyStyle(
        if (shouldApply) MaterialR.style.ThemeOverlay_Material3_DynamicColors_DayNight
        else MaterialR.style.ThemeOverlay_Material3, true
    )
}
