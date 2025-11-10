package com.edricchan.studybuddy.ui.common.snackbar

import androidx.annotation.StringRes
import com.edricchan.studybuddy.ui.common.SnackBarData

/**
 * Base interface to show snack-bars.
 * A concrete implementation is [SnackBarHost].
 */
interface SnackBarController {
    /**
     * Shows a snack-bar in the main UI with the specified parameters.
     * @param data Desired [SnackBarData] to be shown.
     */
    suspend fun showSnackBar(
        data: SnackBarData
    )

    /**
     * Shows a snack-bar in the main UI with the specified parameters.
     * @param messageRes The message to be shown, as a string resource.
     * @param duration How long the snack-bar should be shown for.
     * @param action The snack-bar's action data if any.
     */
    suspend fun showSnackBar(
        @StringRes
        messageRes: Int,
        duration: SnackBarData.Duration,
        action: SnackBarData.Action? = null
    ) {
        showSnackBar(
            SnackBarData(
                messageRes = messageRes,
                duration = duration,
                action = action
            )
        )
    }

    /**
     * Shows a snack-bar in the main UI with the specified parameters.
     * @param message The message to be shown.
     * @param duration How long the snack-bar should be shown for.
     * @param action The snack-bar's action data if any.
     */
    suspend fun showSnackBar(
        message: String,
        duration: SnackBarData.Duration,
        action: SnackBarData.Action? = null
    ) {
        showSnackBar(
            SnackBarData(
                message = message,
                duration = duration,
                action = action
            )
        )
    }
}
