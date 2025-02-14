package com.edricchan.studybuddy.features.auth.ui

import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.auth.gms.contracts.GoogleAuth
import com.edricchan.studybuddy.core.auth.gms.defaultSignInOptions
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.signInWithEmailAndPasswordAsync
import com.edricchan.studybuddy.exts.firebase.auth.signInWithGoogleAsync
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.ActivityLoginBinding
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.widget.NoSwipeBehavior
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@WebDeepLink(["/login"])
@AppDeepLink(["/login"])
class LoginActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override val isEdgeToEdgeEnabled = true

    private val googleLogin = registerForActivityResult(
        GoogleAuth()
    ) { acct ->
        acct?.let {
            lifecycleScope.launch {
                signInGoogle(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        // Check if there's already an authenticated user
        if (auth.currentUser != null) {
            finish()
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

            signUpBtn.setOnClickListener { startActivity<RegisterActivity>() }

            resetPasswordBtn.setOnClickListener { startActivity<ResetPasswordActivity>() }

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

        checkNetwork(null)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressedDispatcher.onBackPressed()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun signInWithGoogle() {
        googleLogin.launch(defaultSignInOptions)
    }

    private fun checkNetwork(snackbar: Snackbar?) {
        val connectivityManager = getSystemService<ConnectivityManager>()
        Log.d(TAG, "isNetworkAvailable: " + connectivityManager?.activeNetworkInfo?.isConnected)
        if (connectivityManager?.activeNetworkInfo?.isConnected == true) {
            setViewsEnabled(true)
            snackbar?.dismiss()
        } else {
            setViewsEnabled(false)
            showSnackbar(
                binding.coordinatorLayoutLogin,
                R.string.snackbar_internet_unavailable_login,
                Snackbar.LENGTH_INDEFINITE
            ) {
                behavior = NoSwipeBehavior()
                setAction(R.string.snackbar_internet_unavailable_action) { checkNetwork(this) }
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
        signIn { auth.signInWithEmailAndPasswordAsync(email, password) }
    }

    private suspend fun signInGoogle(acct: GoogleSignInAccount) {
        signIn { auth.signInWithGoogleAsync(acct) }
    }

    private suspend fun signIn(
        signInFn: suspend () -> AuthResult
    ) {
        binding.progressBar.isVisible = true
        try {
            val result = signInFn()
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "Successfully signed in!")
            showToast(
                getString(R.string.snackbar_user_login, result.user?.email),
                Toast.LENGTH_LONG
            )
            finish()
        } catch (e: Exception) {
            // If sign in fails, display a message to the user.
            Log.e(
                TAG,
                "An error occurred while attempting to sign in with Google:",
                e
            )
            showToast("Authentication failed.", Toast.LENGTH_SHORT)
        } finally {
            binding.progressBar.isVisible = false
        }
    }
}
