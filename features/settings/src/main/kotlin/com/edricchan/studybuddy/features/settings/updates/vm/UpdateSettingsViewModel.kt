package com.edricchan.studybuddy.features.settings.updates.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.core.metadata.updates.source.UpdateMetadataDataSource
import com.edricchan.studybuddy.core.settings.updates.source.UpdateSettingsDataSource
import com.edricchan.studybuddy.features.settings.updates.model.CheckFrequencyCompat
import com.edricchan.studybuddy.features.settings.updates.model.asDuration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class UpdateSettingsViewModel @Inject constructor(
    metadataSource: UpdateMetadataDataSource,
    private val settingsSource: UpdateSettingsDataSource
) : ViewModel() {
    val prefCheckFrequency: Flow<Duration> = settingsSource.checkFrequency
    fun setPrefCheckFrequency(frequency: Duration) = viewModelScope.launch {
        settingsSource.setCheckFrequency(frequency)
    }

    fun setPrefCheckFrequency(frequency: CheckFrequencyCompat) = viewModelScope.launch {
        setPrefCheckFrequency(frequency.asDuration())
    }

    val prefCanDownloadMetered: Flow<Boolean> = settingsSource.canDownloadMetered

    fun setPrefCanDownloadMetered(value: Boolean) = viewModelScope.launch {
        settingsSource.setCanDownloadMetered(value)
    }

    val prefOnlyDownloadCharging: Flow<Boolean> = settingsSource.onlyDownloadCharging
    fun setPrefOnlyDownloadCharging(value: Boolean) = viewModelScope.launch {
        settingsSource.setOnlyDownloadCharging(value)
    }

    val lastChecked: Flow<Instant?> = metadataSource.lastChecked

    val lastUpdated: Flow<Instant?> = metadataSource.lastUpdated
}
