package com.edricchan.studybuddy.utils.dev.flow

import android.content.Context
import com.edricchan.studybuddy.utils.dev.BuildConfig
import com.edricchan.studybuddy.utils.dev.DevModePrefConstants
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference

class DevModePreferences(context: Context) {
    private val devModePreferences = FlowSharedPreferences(
        sharedPreferences = context.getSharedPreferences(
            DevModePrefConstants.FILE_DEV_MODE,
            Context.MODE_PRIVATE
        )
    )

    val devModeEnabled: Preference<Boolean> = devModePreferences.getBoolean(
        DevModePrefConstants.DEV_MODE_ENABLED,
        BuildConfig.DEBUG
    )
}

val Context.devModePreferences: DevModePreferences
    get() = DevModePreferences(this)
