package com.edricchan.studybuddy.features.tasks.ui.attrs.date

import androidx.compose.runtime.Stable
import com.edricchan.studybuddy.exts.datetime.format
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Stable
object TaskDateDefaults {
    val LocalizedDateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    val FormatInstantFn: (Instant) -> String = { it.format(LocalizedDateTimeFormatter) }
}
