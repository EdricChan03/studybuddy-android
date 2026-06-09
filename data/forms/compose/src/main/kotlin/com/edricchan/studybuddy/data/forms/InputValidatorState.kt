package com.edricchan.studybuddy.data.forms

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edricchan.studybuddy.data.forms.compose.InputValidationError
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
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
    debounceTimeout: Duration = 200.milliseconds,
    validator: InputValidator<ValidationError>
): State<ValidationError?> = remember {
    snapshotFlow { text }
        .debounce(debounceTimeout)
        .map(validator::validate)
}.collectAsStateWithLifecycle(null)
