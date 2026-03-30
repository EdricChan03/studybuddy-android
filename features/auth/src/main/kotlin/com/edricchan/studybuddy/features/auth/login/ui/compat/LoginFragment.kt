package com.edricchan.studybuddy.features.auth.login.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.credentials.Credential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PendingGetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.pendingGetCredentialRequest
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.auth.credentials.asGoogleIdTokenCredential
import com.edricchan.studybuddy.core.auth.credentials.googleBtnOption
import com.edricchan.studybuddy.core.auth.credentials.signInWithGoogleCredentials
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToRegister
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToResetPassword
import com.edricchan.studybuddy.core.compat.navigation.task.navigateToTaskRoot
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.awaitSignInWithEmailAndPassword
import com.edricchan.studybuddy.exts.firebase.auth.awaitSignInWithGoogle
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.FragLoginBinding
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : ViewBindingFragment<FragLoginBinding>(FragLoginBinding::inflate) {
    @Inject
    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if there's already an authenticated user
        if (auth.currentUser != null) {
            navController.popBackStack()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }

        binding.apply {
            googleSignInBtn.apply {
                setColorScheme(SignInButton.COLOR_DARK)
                setSize(SignInButton.SIZE_STANDARD)
                setOnClickListener { signInWithGoogle() }
            }

            signUpBtn.setOnClickListener { navController.navigateToRegister() }

            resetPasswordBtn.setOnClickListener { navController.navigateToResetPassword() }

            PendingGetCredentialRequest(
                request = GetCredentialRequest(
                    listOf(GetPasswordOption())
                ),
                callback = {
                    signInWithCredential(it.credential)
                }
            ).also {
                emailLogin.editText?.pendingGetCredentialRequest = it
                passwordLogin.editText?.pendingGetCredentialRequest = it
            }

            loginBtn.setOnClickListener {
                val email = emailLogin.editTextStrValue
                val password = passwordLogin.editTextStrValue

                // Clear any previous errors
                emailLogin.error = null
                passwordLogin.error = null
                if (email.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty()) {
                        emailLogin.error = "Please enter a valid email address"
                    }
                    if (password.isEmpty()) {
                        passwordLogin.error = "Please enter a password"
                    }
                    return@setOnClickListener
                }
                if (password.length < 6) {
                    passwordLogin.error = getString(R.string.minimum_password)
                    return@setOnClickListener
                }

                progressBar.isVisible = true

                lifecycleScope.launch {
                    signInEmail(email, password)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkNetwork()
    }

    private fun signInWithCredential(
        credential: Credential
    ) {
        lifecycleScope.launch {
            auth.awaitSignInWithGoogle(credential.asGoogleIdTokenCredential())
        }
    }

    private fun signInWithGoogle() {
        lifecycleScope.launch {
            try {
                signIn {
                    auth.signInWithGoogleCredentials(
                        requireContext(),
                        listOf(requireContext().googleBtnOption)
                    )
                }
            } catch (e: GetCredentialException) {
                Log.w(TAG, "Could not get credentials:", e)
                mainViewModel.showSnackBar(
                    e.errorMessage!!.toString(),
                    SnackBarData.Duration.Long
                )
            }
        }
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
                    R.string.login_internet_unavailable_snackbar_text,
                    SnackBarData.Duration.Indefinite
                )
            }
        }
    }

    private fun setViewsEnabled(enabled: Boolean) {
        binding.apply {
            signUpBtn.isEnabled = enabled
            loginBtn.isEnabled = enabled
            resetPasswordBtn.isEnabled = enabled
            googleSignInBtn.isEnabled = enabled
            emailLogin.isEnabled = enabled
            passwordLogin.isEnabled = enabled
        }
    }

    private suspend fun signInEmail(email: String, password: String) {
        signIn { auth.awaitSignInWithEmailAndPassword(email, password) }
    }

    private suspend fun signIn(
        signInFn: suspend () -> AuthResult
    ) {
        binding.progressBar.isVisible = true
        try {
            val result = signInFn()
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "Successfully signed in!")
            mainViewModel.showSnackBar(
                getString(R.string.login_success_snackbar_text, result.user?.email),
                SnackBarData.Duration.Long
            )
            navController.navigateToTaskRoot {
                popUpTo<CompatDestination.Task.Root>()
            }
        } catch (e: Exception) {
            // If sign in fails, display a message to the user.
            Log.e(
                TAG,
                "An error occurred while attempting to sign in with Google:",
                e
            )
            mainViewModel.showSnackBar(
                R.string.login_error_snackbar_text,
                SnackBarData.Duration.Short
            )
        } finally {
            binding.progressBar.isVisible = false
        }
    }
}
