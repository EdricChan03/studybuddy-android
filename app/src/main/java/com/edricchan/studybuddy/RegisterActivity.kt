package com.edricchan.studybuddy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar

import androidx.appcompat.app.AppCompatActivity

import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

	private var inputEmail: TextInputLayout? = null
	private var inputPassword: TextInputLayout? = null
	private var btnSignIn: Button? = null
	private var btnSignUp: Button? = null
	private var progressBar: ProgressBar? = null
	private var auth: FirebaseAuth? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_register)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		//Get Firebase auth instance
		auth = FirebaseAuth.getInstance()

		btnSignIn = findViewById(R.id.signInBtn)
		btnSignUp = findViewById(R.id.signUpBtn)
		inputEmail = findViewById(R.id.emailRegister)
		inputPassword = findViewById(R.id.passwordRegister)
		progressBar = findViewById(R.id.progressBar)

		btnSignIn!!.setOnClickListener {
			startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
			finish()
		}

		btnSignUp!!.setOnClickListener {

			val email = SharedUtils.getEditTextString(inputEmail!!).trim { it <= ' ' }
			val password = SharedUtils.getEditTextString(inputPassword!!).trim { it <= ' ' }

			if (TextUtils.isEmpty(email)) {
				Snackbar.make(findViewById(R.id.registerActivity), "Please enter an email address", Snackbar.LENGTH_LONG)
						.show()
				return@setOnClickListener
			}

			if (TextUtils.isEmpty(password)) {
				Snackbar.make(findViewById(R.id.registerActivity), "Please enter a password!", Snackbar.LENGTH_LONG)
						.show()
				return@setOnClickListener
			}

			if (password.length < 6) {
				Snackbar.make(findViewById(R.id.registerActivity), "A minimum of 6 characters is required for a password", Snackbar.LENGTH_LONG)
						.show()
				return@setOnClickListener
			}

			progressBar!!.visibility = View.VISIBLE
			//create user
			auth!!.createUserWithEmailAndPassword(email, password)
					.addOnCompleteListener(this@RegisterActivity) { task ->
						progressBar!!.visibility = View.GONE
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
		progressBar!!.visibility = View.GONE
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
					.setAction("Retry") { view -> checkNetwork() }.show()
		}
	}

	/**
	 * Sets all views as shown/hidden
	 *
	 * @param enabled Whether to show the views
	 */
	private fun setViewsEnabled(enabled: Boolean) {
		btnSignUp!!.isEnabled = enabled
		btnSignIn!!.isEnabled = enabled
		inputEmail!!.isEnabled = enabled
		inputPassword!!.isEnabled = enabled
	}

	companion object {
		private val TAG = SharedUtils.getTag(RegisterActivity::class.java)
	}
}