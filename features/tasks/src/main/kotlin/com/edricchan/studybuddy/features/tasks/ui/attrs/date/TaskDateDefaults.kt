package com.edricchan.studybuddy.features.tasks.ui.attrs.date

import androidx.compose.runtime.Stable
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Stable
object TaskDateDefaults {
    val LocalizedDateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)

    // Instant doesn't support the DayOfMonth temporal field which the
    // MEDIUM format requires, so we need to convert it to a LocalDateTime
    // which _does_ support the field
    val FormatInstantFn: (Instant) -> String =
        { it.toLocalDateTime().format(LocalizedDateTimeFormatter) }
}
