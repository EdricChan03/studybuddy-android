package com.edricchan.studybuddy.ui.modules.auth

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityResetPasswordBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.showSnackbar
import com.edricchan.studybuddy.ui.modules.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@WebDeepLink(["/resetPassword", "/reset-password"])
@AppDeepLink(["/resetPassword", "/reset-password"])
class ResetPasswordActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = ActivityResetPasswordBinding.inflate(layoutInflater)

        auth = Firebase.auth

        binding.apply {
            btnBack.setOnClickListener { finish() }

            btnResetPassword.setOnClickListener {

                val email = inputEmail.text.toString().trim { it <= ' ' }

                if (email.isEmpty()) {
                    showSnackbar(
                        mainView,
                        "Please enter an email address",
                        Snackbar.LENGTH_LONG
                    )
                    return@setOnClickListener
                }

                progressBar.visibility = View.VISIBLE
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

                        progressBar.visibility = View.GONE
                    }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
