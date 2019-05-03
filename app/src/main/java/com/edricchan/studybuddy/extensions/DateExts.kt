package com.edricchan.studybuddy.extensions

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts a [Date] to its [Timestamp] equivalent
 * @return A [Timestamp]
 */
fun Date?.toTimestamp() = Timestamp(this)

/**
 * Converts a date to the specified [format]
 * @param format A format that [SimpleDateFormat] supports
 * @see [SimpleDateFormat docs](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)
 */
fun Date.toFormat(format: String): String {
	return SimpleDateFormat(format, Locale.getDefault()).format(this)
}