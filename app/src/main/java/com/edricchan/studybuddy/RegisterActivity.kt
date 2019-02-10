package com.edricchan.studybuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.isInvalidEmail
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

	private var auth: FirebaseAuth = FirebaseAuth.getInstance()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		auth = FirebaseAuth.getInstance()

		signInBtn.setOnClickListener {
			startActivity(Intent(this, LoginActivity::class.java))
			finish()
		}

		signUpBtn.setOnClickListener {
			val email = emailTextInputLayout.editTextStrValue
			val password = passwordTextInputLayout.editTextStrValue

			if (email.isNullOrBlank()) {
				if (
						emailTextInputLayout.error.isNullOrEmpty() ||
						emailTextInputLayout.error == getString(R.string.edittext_errors_invalid_email)
				) {
					emailTextInputLayout.error = getString(R.string.edittext_errors_empty_email)
				}
			} else if (email.isInvalidEmail()) {
				if (
						emailTextInputLayout.error.isNullOrEmpty() ||
						emailTextInputLayout.error == getString(R.string.edittext_errors_empty_email)
				) {
					emailTextInputLayout.error = getString(R.string.edittext_errors_invalid_email)
				}
			} else {
				if (emailTextInputLayout.error != null) {
					if (emailTextInputLayout.error!!.isNotEmpty()) {
						emailTextInputLayout.error = null
					}
				}
			}
			if (password.isNullOrEmpty()) {
				if (
						passwordTextInputLayout.error.isNullOrEmpty() ||
						passwordTextInputLayout.error == getString(R.string.edittext_errors_invalid_password)
				) {
					passwordTextInputLayout.error = getString(R.string.edittext_errors_empty_password)
				}
			} else if (password.length < 6) {
				if (
						passwordTextInputLayout.error.isNullOrEmpty() ||
						passwordTextInputLayout.error == getString(R.string.edittext_errors_empty_password)
				) {
					passwordTextInputLayout.error = getString(R.string.edittext_errors_invalid_password)
				}
			} else {
				if (passwordTextInputLayout.error != null) {
					if (passwordTextInputLayout.error!!.isNotEmpty()) {
						passwordTextInputLayout.error = null
					}
				}
			}
			if (passwordTextInputLayout.error!!.isNotEmpty() || emailTextInputLayout.error!!.isNotEmpty()) {
				return@setOnClickListener
			}

			progressBar?.visibility = View.VISIBLE
			// Assume that email and password are non-null
			auth.createUserWithEmailAndPassword(email!!, password!!)
					.addOnCompleteListener(this@RegisterActivity) { task ->
						progressBar?.visibility = View.GONE
						if (task.isSuccessful) {
							startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
							finish()
						} else {
							Snackbar.make(findViewById(R.id.registerActivity), "An error occurred while authenticating. Try again later.", Snackbar.LENGTH_LONG)
									.show()
							Log.e(TAG, "An error occurred while authenticating.", task.exception)
						}
					}

		}
		checkNetwork()
	}

	override fun onResume() {
		super.onResume()
		progressBar?.visibility = View.GONE
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

	private fun checkNetwork() {
		if (SharedUtils.isNetworkAvailable(this)) {
			setViewsEnabled(true)
		} else {
			setViewsEnabled(false)
			Snackbar.make(findViewById(R.id.registerActivity), "No internet connection available. Some actions are disabled", Snackbar.LENGTH_INDEFINITE)
					.setBehavior(NoSwipeBehavior())
					.setAction("Retry") { checkNetwork() }.show()
		}
	}

	/**
	 * Sets all views as shown/hidden
	 *
	 * @param enabled Whether to show the views
	 */
	private fun setViewsEnabled(enabled: Boolean) {
		signUpBtn.isEnabled = enabled
		signInBtn.isEnabled = enabled
		emailTextInputLayout.isEnabled = enabled
		passwordTextInputLayout.isEnabled = enabled
	}

	companion object {
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}