package com.edricchan.studybuddy.data.common.temporal

import com.edricchan.studybuddy.data.common.temporal.proto.TimeOfDay
import java.time.LocalTime

fun TimeOfDay.toLocalTime(): LocalTime = LocalTime.of(
    /* hour = */ hours,
    /* minute = */ minutes,
    /* second = */ seconds,
    /* nanoOfSecond = */ nanos
)

fun LocalTime.toTimeOfDay(): TimeOfDay = TimeOfDay(
    hours = hour,
    minutes = minute,
    seconds = second,
    nanos = nano
)
