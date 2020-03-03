package com.edricchan.studybuddy.extensions.firebase

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts a [Timestamp] to the specified [format]
 *
 * See the [`SimpleDateFormat` docs](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html) for more info
 * @param format A format that [SimpleDateFormat] supports
 * @param locale The locale to be used to convert the date
 */
fun Timestamp.toDateFormat(format: String, locale: Locale = Locale.getDefault()): String =
    SimpleDateFormat(format, locale)
        .format(this.toDate())