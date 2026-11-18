package com.edricchan.studybuddy.data.common.temporal

import com.edricchan.studybuddy.data.common.temporal.proto.TimeOfDay
import java.time.LocalTime

fun TimeOfDay.toLocalTime(): LocalTime = LocalTime.of(
    /* hour = */ hours.coerceIn(0..23),
    /* minute = */ minutes.coerceIn(0..59),
    /* second = */ seconds.coerceIn(0..59),
    /* nanoOfSecond = */ nanos.coerceIn(0..999_999_999)
)

fun LocalTime.toTimeOfDay(): TimeOfDay = TimeOfDay(
    hours = hour,
    minutes = minute,
    seconds = second,
    nanos = nano
)
