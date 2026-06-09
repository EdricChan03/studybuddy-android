package com.edricchan.studybuddy.data.forms.compose

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/** Base validation error interface for all form fields to use. */
interface InputValidationError {
    /**
     * String resource for the message to be shown for this particular error type.
     *
     * [getMessage] should be used to retrieve the desired message to be displayed.
     */
    @get:StringRes
    val messageRes: Int

    /**
     * Retrieves the desired formatted message content to be used given the current
     * [user input][input] as supporting text for a text-field.
     *
     * Subclasses may override this method to format the message resource with additional
     * arguments, if any.
     */
    @Composable
    fun getMessage(input: CharSequence): String = stringResource(messageRes)

    /**
     * String resource for the semantics error message to be used for this particular
     * error type.
     *
     * Defaults to [messageRes] if not specified.
     *
     * [getSemanticsMessage] should be used to retrieve the desired message to be displayed.
     */
    @get:StringRes
    val semanticsMessageRes: Int get() = messageRes

    /**
     * Retrieves the desired formatted semantics error message to be used given the
     * current [user input][input].
     *
     * Subclasses may override this method to format the message resource with
     * additional arguments, if any.
     * @see androidx.compose.ui.semantics.error
     */
    @Composable
    fun getSemanticsMessage(input: CharSequence): String = stringResource(semanticsMessageRes)
}
