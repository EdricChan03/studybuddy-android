package com.edricchan.studybuddy.core.settings.updates.source.store

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesMigration
import androidx.datastore.migrations.SharedPreferencesView
import com.edricchan.studybuddy.core.settings.updates.keyPrefCanDownloadMetered
import com.edricchan.studybuddy.core.settings.updates.keyPrefOnlyDownloadCharging
import com.edricchan.studybuddy.core.settings.updates.keyPrefUpdatesFrequency
import com.edricchan.studybuddy.core.settings.updates.proto.UpdateSettings
import com.edricchan.studybuddy.core.settings.updates.resources.R
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import kotlin.time.Duration.Companion.hours
import kotlin.time.toJavaDuration

private val KeysToMigrate = setOf(
    keyPrefUpdatesFrequency,
    keyPrefCanDownloadMetered,
    keyPrefOnlyDownloadCharging
)

private fun Context.migrateSettings(
    view: SharedPreferencesView, settings: UpdateSettings
): UpdateSettings {
    val defaultCheckFreqInt =
        resources.getInteger(R.integer.pref_check_for_updates_frequency_default_value)
    val defaultCheckFreq = defaultCheckFreqInt.hours

    // We use getString instead of getInt as ListPreference (the original underlying Preference)
    // stores its value in String form
    val prefCheckFreq = view.getString(
        keyPrefUpdatesFrequency,
        resources.getInteger(R.integer.pref_check_for_updates_frequency_default_value)
            .toString()
    )?.toInt()

    return settings.copy(
        check_freq = (prefCheckFreq?.hours ?: defaultCheckFreq).toJavaDuration(),
        can_download_metered = view.getBoolean(
            keyPrefCanDownloadMetered,
            false
        ),
        only_download_charging = view.getBoolean(
            keyPrefOnlyDownloadCharging,
            false
        )
    )
}

val Context.updatePrefsMigration
    get() = SharedPreferencesMigration(
        produceSharedPreferences = { defaultSharedPreferences },
        keysToMigrate = KeysToMigrate,
        migrate = ::migrateSettings
    )
