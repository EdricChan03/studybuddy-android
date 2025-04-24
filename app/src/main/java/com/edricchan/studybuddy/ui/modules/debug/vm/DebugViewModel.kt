package com.edricchan.studybuddy.ui.modules.debug.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.settings.updates.UpdateInfoPrefConstants
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.utils.dev.flow.devModePreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import com.fredporciuncula.flow.preferences.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    companion object {
        private const val INSTANT_UNSET = -1L
    }

    private val appPreferences = FlowSharedPreferences(
        sharedPreferences = context.defaultSharedPreferences
    )

    private val updatePreferences = FlowSharedPreferences(
        sharedPreferences = context.getSharedPreferences(
            UpdateInfoPrefConstants.FILE_UPDATE_INFO,
            Context.MODE_PRIVATE
        )
    )

    val customUpdateJsonUrl: Preference<String> =
        appPreferences.getString(Constants.debugSetCustomJsonUrl)

    val lastCheckedUpdates: Preference<Instant?> = updatePreferences.getLong(
        key = UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
        defaultValue = -1
    ).map(
        mapper = { millis ->
            millis.takeIf { it != INSTANT_UNSET }?.let { Instant.ofEpochMilli(it) }
        },
        reverse = { it?.toEpochMilli() ?: -1 }
    )

    val lastUpdated: Preference<Instant?> = updatePreferences.getLong(
        key = UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
        defaultValue = -1
    ).map(
        mapper = { millis ->
            millis.takeIf { it != INSTANT_UNSET }?.let { Instant.ofEpochMilli(it) }
        },
        reverse = { it?.toEpochMilli() ?: -1 }
    )

    private val devModePreferences = context.devModePreferences

    val devModeEnabled: Preference<Boolean> = devModePreferences.devModeEnabled

    fun clearAppPrefs() {
        appPreferences.clear()
    }
}
