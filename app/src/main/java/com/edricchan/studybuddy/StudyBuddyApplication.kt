package com.edricchan.studybuddy

import android.app.Application
import com.edricchan.studybuddy.utils.themeUtils
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions

class StudyBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply Android 12 theming
        DynamicColors.applyToActivitiesIfAvailable(
            this,
            DynamicColorsOptions.Builder()
                .setPrecondition { _, _ -> themeUtils.isUsingDynamicColor }
                .build()
        )
    }
}
