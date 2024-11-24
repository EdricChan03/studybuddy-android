package com.edricchan.studybuddy.ui.modules.settings.fragment.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.core.settings.updates.UpdateInfoPrefConstants
import com.edricchan.studybuddy.core.settings.updates.keyPrefCanDownloadMetered
import com.edricchan.studybuddy.core.settings.updates.keyPrefOnlyDownloadCharging
import com.edricchan.studybuddy.core.settings.updates.keyPrefUpdatesFrequency
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.features.settings.updates.model.CheckFrequencyCompat
import com.edricchan.studybuddy.features.settings.updates.model.hourValue
import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import com.fredporciuncula.flow.preferences.Preference
import com.fredporciuncula.flow.preferences.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class UpdateSettingsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    private val updateInfoPreferences = FlowSharedPreferences(
        context.getSharedPreferences(
            UpdateInfoPrefConstants.FILE_UPDATE_INFO,
            Context.MODE_PRIVATE
        )
    )

    private val appPreferences = FlowSharedPreferences(
        context.defaultSharedPreferences
    )

    val prefCheckFrequency: Preference<CheckFrequencyCompat> = appPreferences
        // ListPreference uses a string to persist its data, see
        // https://stackoverflow.com/q/11346916
        .getString(
            keyPrefUpdatesFrequency,
            defaultValue = CheckFrequencyCompat.SixHours.hourValue.toString()
        ).map(
            mapper = {
                CheckFrequencyCompat.fromHoursOrNull(it.toInt()) ?: CheckFrequencyCompat.SixHours
            },
            reverse = { it.hourValue.toString() }
        )

    val prefCanDownloadMetered: Preference<Boolean> =
        appPreferences.getBoolean(keyPrefCanDownloadMetered)

    val prefOnlyDownloadCharging: Preference<Boolean> =
        appPreferences.getBoolean(keyPrefOnlyDownloadCharging)

    val lastChecked: Preference<Instant?> = updateInfoPreferences.getLong(
        UpdateInfoPrefConstants.PREF_LAST_CHECKED_FOR_UPDATES_DATE,
        Long.MIN_VALUE
    ).map(
        mapper = { valLong ->
            valLong.takeIf { it >= Instant.EPOCH.toEpochMilli() }?.let { Instant.ofEpochMilli(it) }
        },
        reverse = { it?.toEpochMilli() ?: Long.MIN_VALUE }
    )

    val lastUpdated: Preference<Instant?> = updateInfoPreferences.getLong(
        UpdateInfoPrefConstants.PREF_LAST_UPDATED_DATE,
        Long.MIN_VALUE
    ).map(
        mapper = { valLong ->
            valLong.takeIf { it >= Instant.EPOCH.toEpochMilli() }?.let { Instant.ofEpochMilli(it) }
        },
        reverse = { it?.toEpochMilli() ?: Long.MIN_VALUE }
    )
}
