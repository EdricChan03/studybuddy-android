package com.edricchan.studybuddy.ui.common.snackbar

import com.edricchan.studybuddy.ui.common.SnackBarData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Implementation of the [SnackBarController] interface to be used
 * by the host activity. It should listen to snack-bar requests by
 * [collecting][SharedFlow.collect] the [snackBarData] flow.
 */
class SnackBarHost : SnackBarController {
    private val _snackBarData = MutableSharedFlow<SnackBarData>()

    /**
     * [SharedFlow] of the current [SnackBarData] to be shown.
     *
     * Users of the [SnackBarHost] class should [collect][SharedFlow.collect] this flow
     * to receive the desired snack-bars to be shown from across the app.
     */
    val snackBarData: SharedFlow<SnackBarData> = _snackBarData.asSharedFlow()

    override suspend fun showSnackBar(
        data: SnackBarData
    ) {
        _snackBarData.emit(data)
    }
}
