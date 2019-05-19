package com.edricchan.studybuddy.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts a [Long] to a [Date]
 */
fun Long.toDate(): Date = Date(this)

/**
 * Converts a [Long] to a [Date]
 */
fun Long?.toDate(): Date? = this?.let { Date(it) }

/**
 * Converts a [Long] to a [SimpleDateFormat]
 * @param format A format that [SimpleDateFormat] supports
 * @see SimpleDateFormat
 * @see Date.toFormat
 */
fun Long.toDateFormat(format: String) = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

/**
 * Converts a [Long] to a [SimpleDateFormat]
 * @param format A format that [SimpleDateFormat] supports
 * @see SimpleDateFormat
 * @see Date.toFormat
 */
fun Long?.toDateFormat(format: String) = this?.let { SimpleDateFormat(format, Locale.getDefault()).format(Date(it)) }