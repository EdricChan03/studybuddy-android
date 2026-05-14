package com.edricchan.studybuddy.core.settings.appearance.store

import com.edricchan.studybuddy.core.settings.appearance.proto.AppearanceSettings
import com.edricchan.studybuddy.utils.wire.datastore.WireSerializer

object AppearanceSettingsSerializer : WireSerializer<AppearanceSettings>(
    adapter = AppearanceSettings.ADAPTER,
    defaultValue = AppearanceSettings()
)
