package com.edricchan.studybuddy.features.auth.exts

import androidx.core.util.PatternsCompat

private val emailAddressRegex = PatternsCompat.EMAIL_ADDRESS.toRegex()

/**
 * Checks if the specified [String] is a valid email address
 * @return `true` if it is a valid email address, `false` otherwise
 */
fun String.isValidEmail() = emailAddressRegex matches this

/**
 * Checks if the specified [String] is not a valid email address
 * @return `true` if it is an invalid email address, `false` otherwise
 */
fun String.isInvalidEmail() = !isValidEmail()
