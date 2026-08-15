package com.edricchan.studybuddy.core.settings.general.store.migration

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesMigration
import com.edricchan.studybuddy.core.settings.general.keyPrefUseCustomTabs
import com.edricchan.studybuddy.core.settings.general.proto.GeneralSettings
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences

val GeneralPrefsKeys = setOf(
    keyPrefUseCustomTabs
)

val Context.GeneralPrefsMigration: SharedPreferencesMigration<GeneralSettings>
    get() = SharedPreferencesMigration(
        produceSharedPreferences = { defaultSharedPreferences },
        keysToMigrate = GeneralPrefsKeys
    ) { view, settings ->
        settings.copy(
            open_links_in_app = view.getBoolean(keyPrefUseCustomTabs, true)
        )
    }
