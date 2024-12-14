package com.edricchan.studybuddy.features.settings.task.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.core.settings.tasks.keyPrefTaskDefaultSort
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.features.settings.task.model.TaskSortOptionCompat
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import com.fredporciuncula.flow.preferences.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class TaskSettingsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val appPreferences = FlowSharedPreferences(
        context.defaultSharedPreferences
    )

    val prefDefaultSort: Preference<TaskSortOptionCompat> = appPreferences.getString(
        keyPrefTaskDefaultSort
    ).map(
        mapper = TaskSortOptionCompat::fromValue,
        reverse = TaskSortOptionCompat::value
    )
}
