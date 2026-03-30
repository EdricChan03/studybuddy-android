package com.edricchan.studybuddy.features.auth.register.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToLogin
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.FragRegisterBinding
import com.edricchan.studybuddy.features.auth.exts.isInvalidEmail
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : ViewBindingFragment<FragRegisterBinding>(FragRegisterBinding::inflate) {
    @Inject
    lateinit var authService: AuthService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            signInBtn.setOnClickListener {
                navController.navigateToLogin {
                    popUpTo<CompatDestination.Auth.Login>()
                }
            }

            signUpBtn.setOnClickListener {
                val email = emailTextInputLayout.editTextStrValue
                val password = passwordTextInputLayout.editTextStrValue

                emailTextInputLayout.apply {
                    if (email.isBlank()) {
                        if (
                            error.isNullOrEmpty() ||
                            error == getString(R.string.edittext_errors_invalid_email)
                        ) {
                            error = getString(R.string.edittext_errors_empty_email)
                        }
                    } else if (email.isInvalidEmail()) {
                        if (
                            error.isNullOrEmpty() ||
                            error == getString(R.string.edittext_errors_empty_email)
                        ) {
                            error =
                                getString(R.string.edittext_errors_invalid_email)
                        }
                    } else {
                        error?.let {
                            if (it.isNotEmpty()) error = null
                        }
                    }
                }
                passwordTextInputLayout.apply {
                    if (password.isEmpty()) {
                        if (
                            error.isNullOrEmpty() ||
                            error == getString(R.string.edittext_errors_invalid_password)
                        ) {
                            error =
                                getString(R.string.edittext_errors_empty_password)
                        }
                    } else if (password.length < 6) {
                        if (
                            error.isNullOrEmpty() ||
                            error == getString(R.string.edittext_errors_empty_password)
                        ) {
                            error =
                                getString(R.string.edittext_errors_invalid_password)
                        }
                    } else {
                        error?.let {
                            if (it.isNotEmpty()) error = null
                        }
                    }
                    if (
                        !passwordTextInputLayout.error.isNullOrEmpty() ||
                        !emailTextInputLayout.error.isNullOrEmpty()
                    ) {
                        return@setOnClickListener
                    }
                }

                progressBar.isVisible = true
                // Assume that email and password are non-null
                register(email, password)
            }
        }
        checkNetwork()
    }

    private fun register(email: String, password: String) = lifecycleScope.launch {
        try {
            authService.register(email = email, password = password)
            binding.progressBar.isVisible = false
            navController.popBackStack()
        } catch (e: Exception) {
            // TODO: i18n message
            showSnackBar(
                R.string.register_error_snackbar_text,
                SnackBarData.Duration.Long
            )
            Log.e(TAG, "An error occurred while authenticating.", e)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.isVisible = false
    }

    private fun checkNetwork() {
        viewLifecycleOwner.lifecycleScope.launch {
            observeNetworkState().flowWithLifecycle(lifecycle).collect {
                setViewsEnabled(it.isOnline)
                if (it.isOnline) {
                    mainViewModel.dismissCurrentSnackBar()
                    return@collect
                }
                mainViewModel.showSnackBar(
                    R.string.register_internet_unavailable_snackbar_text,
                    SnackBarData.Duration.Indefinite
                )
            }
        }
    }

    /**
     * Sets all views as shown/hidden
     *
     * @param enabled Whether to show the views
     */
    private fun setViewsEnabled(enabled: Boolean) {
        binding.apply {
            signUpBtn.isEnabled = enabled
            signInBtn.isEnabled = enabled
            emailTextInputLayout.isEnabled = enabled
            passwordTextInputLayout.isEnabled = enabled
        }
    }
}
