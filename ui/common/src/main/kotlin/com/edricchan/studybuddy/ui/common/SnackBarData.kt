package com.edricchan.studybuddy.ui.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.snackbar.Snackbar

/**
 * Data to be shown in a [Snackbar].
 * @property message The snack-bar's message.
 * @property action The snack-bar's [Action] button, if any.
 * @property duration How long the snack-bar should be shown for.
 */
data class SnackBarData(
    val message: Text,
    val action: Action?,
    val duration: Duration
) {
    constructor(
        message: String,
        action: Action?,
        duration: Duration
    ) : this(
        message = Text.Str(message),
        action = action,
        duration = duration
    )

    constructor(
        @StringRes
        messageRes: Int,
        action: Action?,
        duration: Duration
    ) : this(
        message = Text.Res(messageRes),
        action = action,
        duration = duration
    )

    sealed interface Text {
        @JvmInline
        value class Str(val value: String) : Text {
            override fun asString(context: Context): String = value
        }

        @JvmInline
        value class Res(@field:StringRes val stringRes: Int) : Text {
            override fun asString(context: Context): String = context.getString(stringRes)
        }

        fun asString(context: Context): String

        @Composable
        fun asString(): String = asString(LocalContext.current)
    }

    /**
     * The snack-bar's action button data.
     * @property text The action button's text.
     * @property onClick Lambda to be invoked when the action button is clicked.
     */
    data class Action(
        val text: Text,
        val onClick: () -> Unit
    ) {
        constructor(
            text: String,
            onClick: () -> Unit
        ) : this(
            text = Text.Str(text),
            onClick = onClick
        )

        constructor(
            @StringRes
            textRes: Int,
            onClick: () -> Unit
        ) : this(
            text = Text.Res(textRes),
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
