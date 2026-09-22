package com.edricchan.studybuddy.data.forms

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Collects and returns the [ValidationError] of the receiver [TextFieldState]'s text
 * as a Compose [State].
 * @param debounceTimeout Timeout to be passed to the [debounce] operator.
 * @param validator Validator to be used to validate the [TextFieldState]'s text with.
 */
@OptIn(FlowPreview::class)
@Composable
fun <ValidationError : InputValidationError> TextFieldState.validationErrorAsState(
    validator: InputValidator<ValidationError>,
    debounceTimeout: Duration = 200.milliseconds
): State<ValidationError?> = remember {
    snapshotFlow { text }
        .debounce(debounceTimeout)
        .map(validator::validate)
}.collectAsStateWithLifecycle(null)

/**
 * Collects and returns the [ValidationError] of the receiver [TextFieldState]'s text
 * as a Compose [State], emitting new [ValidationError]s only when the [interactionSource]
 * receives a focus interaction.
 * @param debounceTimeout Timeout to be passed to the [debounce] operator.
 * @param validator Validator to be used to validate the [TextFieldState]'s text with.
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@Composable
fun <ValidationError : InputValidationError> TextFieldState.validationErrorAsState(
    validator: InputValidator<ValidationError>,
    interactionSource: InteractionSource,
    debounceTimeout: Duration = 200.milliseconds,
): State<ValidationError?> = remember {
    interactionSource.interactions.filterIsInstance<FocusInteraction.Unfocus>()
        .debounce(debounceTimeout)
        .mapLatest { validator.validate(text) }
}.collectAsStateWithLifecycle(null)

/**
 * Collects and returns whether there were any validation errors for the
 * receiver [TextFieldState]'s text as a Compose [State].
 * @param debounceTimeout Timeout to be passed to the [debounce] operator.
 * @param initialValue Initial value for the Compose [State].
 * @param validator Validator to be used to validate the [TextFieldState]'s text with.
 * @see validationErrorAsState
 */
@OptIn(FlowPreview::class)
@Composable
fun <ValidationError : InputValidationError> TextFieldState.isValidState(
    debounceTimeout: Duration = 200.milliseconds,
    initialValue: Boolean = false,
    validator: InputValidator<ValidationError>
): State<Boolean> = remember {
    snapshotFlow { text }
        .debounce(debounceTimeout)
        .map(validator::hasValidationError)
}.collectAsStateWithLifecycle(initialValue)
