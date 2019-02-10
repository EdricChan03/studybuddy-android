package com.edricchan.studybuddy.extensions

/**
 * Checks if a value is [null]
 * @return [true] if the value is [null], [false] otherwise
 */
fun Any?.isNull() = this == null

/**
 * Checks if a value is not [null]
 * @return [true] if the value is not [null], [false] otherwise
 */
fun Any?.isNotNull() = this != null