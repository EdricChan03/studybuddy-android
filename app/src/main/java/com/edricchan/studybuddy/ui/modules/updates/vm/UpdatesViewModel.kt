package com.edricchan.studybuddy.ui.modules.updates.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edricchan.studybuddy.core.metadata.updates.source.UpdateMetadataDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class UpdatesViewModel @Inject constructor(
    private val metadataSource: UpdateMetadataDataSource
) : ViewModel() {
    fun setLastUpdated(lastUpdated: Instant) = viewModelScope.launch {
        metadataSource.setLastUpdated(lastUpdated)
    }

    fun setLastChecked(lastChecked: Instant) = viewModelScope.launch {
        metadataSource.setLastChecked(lastChecked)
    }
}
