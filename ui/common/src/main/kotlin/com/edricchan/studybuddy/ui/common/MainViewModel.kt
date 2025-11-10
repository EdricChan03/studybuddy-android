package com.edricchan.studybuddy.ui.common

import androidx.lifecycle.ViewModel
import com.edricchan.studybuddy.ui.common.snackbar.SnackBarController
import com.edricchan.studybuddy.ui.common.snackbar.SnackBarHost
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val host: SnackBarHost
) : ViewModel(), SnackBarController by host {
    /** The [SnackBarData] to be shown, if any. */
    val snackBarData = host.snackBarData
}
