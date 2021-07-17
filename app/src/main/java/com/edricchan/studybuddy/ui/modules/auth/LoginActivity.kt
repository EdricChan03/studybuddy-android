package com.edricchan.studybuddy.ui.modules.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityLoginBinding
import com.edricchan.studybuddy.extensions.*
import com.edricchan.studybuddy.ui.modules.main.MainActivity
import com.edricchan.studybuddy.ui.widget.NoSwipeBehavior
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@WebDeepLink(["/login"])
@AppDeepLink(["/login"])
class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // Result code used to check for authentication
        RC_SIGN_IN = 9001
        // Check if there's already an authenticated user
        if (auth.currentUser != null && SharedUtils.isNetworkAvailable(this)) {
            // This activity (`LoginActivity`) shouldn't be shown to an already authenticated user
            // Instead, redirect the user to the main activity and close this activity
            startActivity<MainActivity>()
            finish()
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
                progressBar.visibility = View.VISIBLE
                // Authenticate the user
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.visibility = View.GONE
                        if (!task.isSuccessful) {
                            Log.e(
                                TAG,
                                "An error occurred while attempting to login using an email and password.",
                                task.exception
                            )
                            // there was an error
                            if (password.length < 6) {
                                passwordLogin.error = getString(R.string.minimum_password)
                            } else {
                                showSnackbar(binding.coordinatorLayoutLogin,
                                    "An error occurred while attempting to login. Please try again later",
                                    Snackbar.LENGTH_LONG
                                )
                            }
                        } else {
                            showLoginSnackBar()
                            // We only want to show the main activity if this activity was explicitly
                            // launched.
                            if (isTaskRoot) {
                                startActivity<MainActivity>()
                            }
                            finish()
                        }
                    }
            }
        }

        checkNetwork(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let { firebaseAuthWithGoogle(it) }
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showLoginSnackBar() {
        if (auth.currentUser != null) {
            showToast(
                getString(R.string.snackbar_user_login, auth.currentUser?.email),
                Toast.LENGTH_SHORT
            )
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun checkNetwork(snackbar: Snackbar?) {
        Log.d(TAG, "isNetworkAvailable: " + SharedUtils.isNetworkAvailable(this))
        if (SharedUtils.isNetworkAvailable(this)) {
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

    /**
     * Authorizes with Google
     *
     * @param acct The account
     */
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "Successfully signed in!")
                    showLoginSnackBar()
                    startActivity<MainActivity>()
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(
                        TAG,
                        "An error occurred while attempting to sign in with Google:",
                        task.exception
                    )
                    showToast("Authentication failed.", Toast.LENGTH_SHORT)
                }

                // ...
            }
    }

    companion object {
        private var RC_SIGN_IN: Int = 0
    }

}