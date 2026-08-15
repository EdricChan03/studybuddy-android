package com.edricchan.studybuddy.core.settings.general.store

import com.edricchan.studybuddy.core.settings.general.proto.GeneralSettings
import com.edricchan.studybuddy.utils.wire.datastore.WireSerializer

object GeneralSettingsSerializer : WireSerializer<GeneralSettings>(
    adapter = GeneralSettings.ADAPTER,
    defaultValue = GeneralSettings()
)
