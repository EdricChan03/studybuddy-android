package com.edricchan.studybuddy.extensions

// This file contains the definitions for all global extensions

/**
 * Checks if a value is `null`
 * @return `true` if the value is `null`, `false` otherwise
 */
fun Any?.isNull() = this == null

/**
 * Checks if a value is not `null`
 * @return `true` if the value is not `null`, `false` otherwise
 */
fun Any?.isNotNull() = this != null

/**
 * The TAG constant which should ideally be used with one of the [android.util.Log] methods.
 *
 * See https://stackoverflow.com/a/53510106/6782707 for more info
 */
val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }