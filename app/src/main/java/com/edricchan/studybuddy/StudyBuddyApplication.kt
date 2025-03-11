package com.edricchan.studybuddy

import android.app.Application
import com.edricchan.studybuddy.ui.theming.common.dynamic.prefDynamicTheme
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StudyBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply Android 12 theming
        DynamicColors.applyToActivitiesIfAvailable(
            this,
            DynamicColorsOptions.Builder()
                .setPrecondition { _, _ -> prefDynamicTheme }
                .build()
        )
    }
}
