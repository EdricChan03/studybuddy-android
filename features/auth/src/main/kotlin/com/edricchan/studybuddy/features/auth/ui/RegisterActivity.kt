package com.edricchan.studybuddy.features.auth.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.awaitCreateUserWithEmailAndPassword
import com.edricchan.studybuddy.exts.material.snackbar.createSnackbar
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.ActivityRegisterBinding
import com.edricchan.studybuddy.features.auth.exts.isInvalidEmail
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.widget.NoSwipeBehavior
import com.edricchan.studybuddy.utils.network.isNetworkConnected
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import com.edricchan.studybuddy.core.resources.R as CoreResR

@WebDeepLink(["/register"])
@AppDeepLink(["/register"])
class RegisterActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
            setContentView(root)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }

        binding.apply {
            signInBtn.setOnClickListener {
                startActivity<LoginActivity>()
                finish()
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
            auth.awaitCreateUserWithEmailAndPassword(email, password)
            binding.progressBar.isVisible = false
            finish()
        } catch (e: Exception) {
            // TODO: i18n message
            showSnackbar(
                binding.coordinatorLayoutRegister,
                "An error occurred while authenticating. Please try again later.",
                Snackbar.LENGTH_LONG
            )
            Log.e(TAG, "An error occurred while authenticating.", e)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private val noInternetSnackbar by lazy {
        createSnackbar(
            binding.coordinatorLayoutRegister,
            // TODO: i18n message
            "No internet connection available. Some actions are disabled",
            Snackbar.LENGTH_INDEFINITE
        ) {
            behavior = NoSwipeBehavior()
            setAction(CoreResR.string.dialog_action_retry) { checkNetwork() }
        }
    }

    // TODO: Use NetworkCallback
    private fun checkNetwork() {
        val isConnected = isNetworkConnected
        setViewsEnabled(isConnected)

        with(noInternetSnackbar) {
            if (!isConnected) show()
            else dismiss()
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
