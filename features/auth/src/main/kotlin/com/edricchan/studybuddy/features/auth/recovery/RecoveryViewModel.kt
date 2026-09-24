package com.edricchan.studybuddy.features.auth.recovery

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.snackbar.SnackBarController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authService: AuthService,
    private val snackBarController: SnackBarController
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    val emailState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = ::TextFieldState
    )

    fun onRequestSubmitClick() {
        viewModelScope.launch {
            try {
                authService.requestPasswordReset(emailState.text.toString())
                snackBarController.showSnackBar(
                    R.string.forgot_pwd_confirmed_msg,
                    SnackBarData.Duration.Long
                )
            } catch (e: Exception) {
                snackBarController.showSnackBar(
                    R.string.forgot_pwd_error_msg,
                    SnackBarData.Duration.Long
                )
                Log.e(
                    TAG,
                    "An error occurred while attempting to send a reset password email.",
                    e
                )
            }
        }
    }
}
