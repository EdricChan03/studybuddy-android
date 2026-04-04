package com.edricchan.studybuddy.core.settings.updates.source.store

import com.edricchan.studybuddy.core.settings.updates.proto.UpdateSettings
import com.edricchan.studybuddy.utils.wire.datastore.WireSerializer

object UpdatesSettingsSerializer : WireSerializer<UpdateSettings>(
    defaultValue = UpdateSettings(),
    adapter = UpdateSettings.ADAPTER
)
