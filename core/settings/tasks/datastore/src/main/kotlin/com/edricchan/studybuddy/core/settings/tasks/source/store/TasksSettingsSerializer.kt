package com.edricchan.studybuddy.core.settings.tasks.source.store

import com.edricchan.studybuddy.core.settings.tasks.proto.TasksSettings
import com.edricchan.studybuddy.utils.wire.datastore.WireSerializer

object TasksSettingsSerializer : WireSerializer<TasksSettings>(
    adapter = TasksSettings.ADAPTER,
    defaultValue = TasksSettings()
)
