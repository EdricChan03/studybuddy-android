package com.edricchan.studybuddy.features.auth.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.features.auth.databinding.ActivityResetPasswordBinding
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@WebDeepLink(["/resetPassword", "/reset-password"])
@AppDeepLink(["/resetPassword", "/reset-password"])
class ResetPasswordActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityResetPasswordBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater).apply {
            setContentView(root)
            setSupportActionBar(toolbar)
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
            btnBack.setOnClickListener { finish() }

            btnResetPassword.setOnClickListener {

                val email = inputEmail.text.toString().trim()

                if (email.isEmpty()) {
                    showSnackbar(
                        mainView,
                        "Please enter an email address",
                        Snackbar.LENGTH_LONG
                    )
                    return@setOnClickListener
                }

                progressBar.isVisible = true
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showSnackbar(
                                mainView,
                                "An email has been sent to the email address that you have specified",
                                Snackbar.LENGTH_LONG
                            )
                        } else {
                            showSnackbar(
                                mainView,
                                "An error occurred while attempting to send an email. Please try again later",
                                Snackbar.LENGTH_LONG
                            )
                            Log.e(
                                TAG,
                                "An error occurred while attempting to send a reset password email.",
                                task.exception
                            )
                        }

                        progressBar.isVisible = false
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
