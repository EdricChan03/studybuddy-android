package com.edricchan.studybuddy.exts.datetime

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.Locale

/**
 * Creates a [buildDateTimeFormatter] using the specified [DateTimeFormatterBuilder]
 * options.
 * @see DateTimeFormatterBuilder.toFormatter
 */
fun buildDateTimeFormatter(init: DateTimeFormatterBuilder.() -> Unit): DateTimeFormatter =
    DateTimeFormatterBuilder().apply(init).toFormatter()

/**
 * Creates a [buildDateTimeFormatter] using the specified [DateTimeFormatterBuilder]
 * options and the [locale].
 * @see DateTimeFormatterBuilder.toFormatter
 */
fun buildDateTimeFormatter(
    locale: Locale,
    init: DateTimeFormatterBuilder.() -> Unit
): DateTimeFormatter = DateTimeFormatterBuilder().apply(init).toFormatter(locale)
