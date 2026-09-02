package com.edricchan.studybuddy.features.auth.register.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.textfield.inputValue
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.FragRegisterBinding
import com.edricchan.studybuddy.features.auth.exts.isInvalidEmail
import com.edricchan.studybuddy.features.auth.navigation.AuthDestination
import com.edricchan.studybuddy.features.auth.navigation.navigateToLogin
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
                    popUpTo<AuthDestination.Login>()
                }
            }

            signUpBtn.setOnClickListener {
                val email = emailTextInputLayout.inputValue
                val password = passwordTextInputLayout.inputValue

                if (email.isNullOrBlank() || password.isNullOrBlank()) {
                    emailTextInputLayout.error = when {
                        email.isNullOrBlank() -> getString(R.string.edittext_errors_empty_email)
                        email.isInvalidEmail() -> getString(R.string.edittext_errors_invalid_email)
                        else -> null
                    }
                    passwordTextInputLayout.apply {
                        error = when {
                            password.isNullOrBlank() ->
                                getString(R.string.edittext_errors_empty_password)

                            password.length < 6 -> getString(R.string.edittext_errors_invalid_password)
                            else -> null
                        }
                    }

                    return@setOnClickListener
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
            navController.navigateToLogin()
        } catch (e: Exception) {
            mainViewModel.showSnackBar(
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
