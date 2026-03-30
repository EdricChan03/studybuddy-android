package com.edricchan.studybuddy.ui.common

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import com.edricchan.studybuddy.ui.common.content.TextContent
import com.google.android.material.snackbar.Snackbar

/**
 * Data to be shown in a [Snackbar].
 * @property message The snack-bar's message.
 * @property action The snack-bar's [Action] button, if any.
 * @property duration How long the snack-bar should be shown for.
 */
data class SnackBarData(
    val message: TextContent,
    val action: Action?,
    val duration: Duration
) {
    constructor(
        message: String,
        action: Action?,
        duration: Duration
    ) : this(
        message = TextContent.Str(message),
        action = action,
        duration = duration
    )

    constructor(
        @StringRes
        messageRes: Int,
        action: Action?,
        duration: Duration
    ) : this(
        message = TextContent.Res(messageRes),
        action = action,
        duration = duration
    )

    /**
     * The snack-bar's action button data.
     * @property text The action button's text.
     * @property onClick Lambda to be invoked when the action button is clicked.
     */
    data class Action(
        val text: TextContent,
        val onClick: () -> Unit
    ) {
        constructor(
            text: String,
            onClick: () -> Unit
        ) : this(
            text = TextContent.Str(text),
            onClick = onClick
        )

        constructor(
            @StringRes
            textRes: Int,
            onClick: () -> Unit
        ) : this(
            text = TextContent.Res(textRes),
            onClick = onClick
        )
    }

    enum class Duration(val value: Int) {
        Short(Snackbar.LENGTH_SHORT),
        Long(Snackbar.LENGTH_LONG),
        Indefinite(Snackbar.LENGTH_INDEFINITE);

        fun toComposeDuration() = when (this) {
            Short -> SnackbarDuration.Short
            Long -> SnackbarDuration.Long
            Indefinite -> SnackbarDuration.Indefinite
        }
    }
}
