package com.edricchan.studybuddy.data.forms.compose

import androidx.compose.foundation.text.input.TextFieldState
import com.edricchan.studybuddy.data.forms.InputValidator

/**
 * Returns the [ValidationError] for the given receiver [TextFieldState]'s [TextFieldState.text],
 * or `null` if there were no validation errors.
 * @see InputValidator.validate
 */
fun <ValidationError : InputValidationError> TextFieldState.validate(
    validator: InputValidator<ValidationError>
): ValidationError? = validator.validate(text)

/**
 * Returns whether there were any validation errors for the given receiver
 * [TextFieldState]'s [TextFieldState.text].
 * @see InputValidator.hasValidationError
 */
fun <ValidationError : InputValidationError> TextFieldState.isValid(
    validator: InputValidator<ValidationError>
): Boolean = validator.hasValidationError(text)

/**
 * Returns the receiver [TextFieldState]'s [TextFieldState.text] if it
 * satisfies the given [validator] or `null`, if it doesn't.
 * @see takeIf
 */
fun TextFieldState.takeTextIfValid(validator: InputValidator<*>): CharSequence? =
    text.takeIf(validator::hasValidationError)

/**
 * Returns the receiver [TextFieldState]'s [TextFieldState.text] if it
 * *does not* satisfy the given [validator] or `null`, if it does.
 * @see takeUnless
 */
fun TextFieldState.takeTextUnlessValid(validator: InputValidator<*>): CharSequence? =
    text.takeUnless(validator::hasValidationError)
