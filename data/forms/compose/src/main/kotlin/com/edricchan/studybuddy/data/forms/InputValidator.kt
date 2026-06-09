package com.edricchan.studybuddy.data.forms

import com.edricchan.studybuddy.data.forms.compose.InputValidationError

/** Validator for [validating][validate] a given input. */
fun interface InputValidator<ValidationError : InputValidationError> {
    /**
     * Returns a specific [ValidationError] if the specified [input] is invalid, or
     * `null` otherwise.
     */
    fun validate(input: CharSequence): ValidationError?

    /** Returns whether there were any validation errors for the given [input]. */
    fun hasValidationError(input: CharSequence): Boolean = validate(input) != null
}

/**
 * Returns the receiver's value if it satisfies the given [validator] or `null`,
 * if it doesn't.
 * @see takeIf
 */
fun CharSequence.takeIfValid(validator: InputValidator<*>): CharSequence? =
    takeIf(validator::hasValidationError)

/**
 * Returns the receiver's value if it *does not* satisfy the given [validator] or `null`,
 * if it does.
 * @see takeUnless
 */
fun CharSequence.takeUnlessValid(validator: InputValidator<*>): CharSequence? =
    takeUnless(validator::hasValidationError)
