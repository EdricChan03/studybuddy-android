package com.edricchan.studybuddy.extensions

import java.util.*

/**
 * Converts a [Long] to a [Date]
 */
fun Long.toDate(): Date = Date(this)

/**
 * Converts a [Long] to a [Date]
 */
fun Long?.toDate(): Date? = this?.let { Date(it) }