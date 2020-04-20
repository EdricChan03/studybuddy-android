package com.edricchan.studybuddy.extensions

import androidx.core.util.PatternsCompat

/**
 * Checks if the specified [String] is a valid email address
 * @return `true` if it is a valid email address, `false` otherwise
 */
fun String.isValidEmail() = PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Checks if the specified [String] is not a valid email address
 * @return `true` if it is an invalid email address, `false` otherwise
 */
fun String.isInvalidEmail() = !isValidEmail()