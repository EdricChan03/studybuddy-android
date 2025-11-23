package com.edricchan.studybuddy.features.auth.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.ActivityResetPasswordBinding
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@WebDeepLink(["/resetPassword", "/reset-password"])
@AppDeepLink(["/resetPassword", "/reset-password"])
@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityResetPasswordBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setSupportActionBar(toolbar)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }

        binding.apply {
            btnBack.setOnClickListener { finish() }

            inputEmail.doAfterTextChanged {
                btnResetPassword.isEnabled = !it.isNullOrBlank()
            }
            btnResetPassword.setOnClickListener {

                val email = inputEmail.text?.toString()?.trim() ?: return@setOnClickListener

                progressBar.isVisible = true
                lifecycleScope.launch {
                    try {
                        auth.sendPasswordResetEmail(email).await()
                        showSnackbar(
                            mainView,
                            R.string.forgot_pwd_confirmed_msg,
                            Snackbar.LENGTH_LONG
                        )
                        progressBar.isVisible = false
                    } catch (e: Exception) {
                        showSnackbar(
                            mainView,
                            R.string.forgot_pwd_error_msg,
                            Snackbar.LENGTH_LONG
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
