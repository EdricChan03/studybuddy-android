package com.edricchan.studybuddy.ui.common.snackbar

import com.edricchan.studybuddy.ui.common.SnackBarData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Implementation of the [SnackBarController] interface to be used
 * by the host activity. It should listen to snack-bar requests by
 * [collecting][SharedFlow.collect] the [snackBarData] flow.
 */
class SnackBarHost : SnackBarController {
    /**
     * [SharedFlow] of the current [SnackBarData] to be shown.
     *
     * Users of the [SnackBarHost] class should [collect][SharedFlow.collect] this flow
     * to receive the desired snack-bars to be shown from across the app.
     */
    val snackBarData: SharedFlow<SnackBarData>
        field = MutableSharedFlow()

    override suspend fun showSnackBar(
        data: SnackBarData
    ) {
        snackBarData.emit(data)
    }

    /**
     * [Channel] consisting of any dismissal requests to be made on the currently shown
     * snack-bar, if any.
     */
    val dismissChannel: Channel<Unit> = Channel()

    override suspend fun dismissCurrentSnackBar() {
        dismissChannel.send(Unit)
    }
}
