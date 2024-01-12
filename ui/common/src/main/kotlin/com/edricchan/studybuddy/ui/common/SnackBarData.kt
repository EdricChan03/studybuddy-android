package com.edricchan.studybuddy.ui.common

import com.edricchan.studybuddy.ui.common.SnackBarData.Action
import com.google.android.material.snackbar.Snackbar

/**
 * Data to be shown in a [Snackbar].
 * @property message The snack-bar's message.
 * @property action The snack-bar's [Action] button, if any.
 * @property duration How long the snack-bar should be shown for.
 */
data class SnackBarData(
    val message: String,
    val action: Action?,
    val duration: Duration
) {
    /**
     * The snack-bar's action button data.
     * @property text The action button's text.
     * @property onClick Lambda to be invoked when the action button is clicked.
     */
    data class Action(
        val text: String,
        val onClick: () -> Unit
    )

    enum class Duration(val value: Int) {
        Short(Snackbar.LENGTH_SHORT),
        Long(Snackbar.LENGTH_LONG),
        Indefinite(Snackbar.LENGTH_INDEFINITE)
    }
}
