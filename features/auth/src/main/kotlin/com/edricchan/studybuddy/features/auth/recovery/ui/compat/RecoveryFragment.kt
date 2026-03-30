package com.edricchan.studybuddy.features.auth.recovery.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.FragRecoveryBinding
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecoveryFragment :
    ViewBindingFragment<FragRecoveryBinding>(FragRecoveryBinding::inflate) {
    @Inject
    lateinit var authService: AuthService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnBack.setOnClickListener { navController.popBackStack() }

            inputEmail.doAfterTextChanged {
                btnResetPassword.isEnabled = !it.isNullOrBlank()
            }
            btnResetPassword.setOnClickListener {
                val email = inputEmail.text?.toString()?.trim() ?: return@setOnClickListener

                progressBar.isVisible = true
                lifecycleScope.launch {
                    try {
                        authService.requestPasswordReset(email)
                        mainViewModel.showSnackBar(
                            R.string.forgot_pwd_confirmed_msg,
                            SnackBarData.Duration.Long
                        )
                        progressBar.isVisible = false
                    } catch (e: Exception) {
                        mainViewModel.showSnackBar(
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
    }
}
