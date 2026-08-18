package com.edricchan.studybuddy.core.settings.general.temporal.format

import com.edricchan.studybuddy.core.settings.general.proto.TemporalFormatSettings

data class TemporalFormatOptions(
    val useRelativeTimestamps: Boolean = true
)

fun TemporalFormatSettings.toDomain(): TemporalFormatOptions = TemporalFormatOptions(
    useRelativeTimestamps = use_relative_timestamps
)

fun TemporalFormatOptions.toProto(): TemporalFormatSettings = TemporalFormatSettings(
    use_relative_timestamps = useRelativeTimestamps
)

