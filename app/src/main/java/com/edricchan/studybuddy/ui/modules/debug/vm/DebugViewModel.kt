package com.edricchan.studybuddy.ui.modules.debug.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.core.metadata.updates.source.UpdateMetadataDataSource
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.utils.dev.flow.devModePreferences
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    metadataSource: UpdateMetadataDataSource
) : ViewModel() {
    private val appPreferences = FlowSharedPreferences(
        sharedPreferences = context.defaultSharedPreferences
    )

    val customUpdateJsonUrl: Preference<String> =
        appPreferences.getString(Constants.debugSetCustomJsonUrl)

    val lastCheckedUpdates: Flow<Instant?> = metadataSource.lastChecked

    val lastUpdated: Flow<Instant?> = metadataSource.lastUpdated

    private val devModePreferences = context.devModePreferences

    val devModeEnabled: Preference<Boolean> = devModePreferences.devModeEnabled

    fun clearAppPrefs() {
        appPreferences.clear()
    }
}
