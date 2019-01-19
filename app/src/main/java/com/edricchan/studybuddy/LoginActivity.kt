package com.edricchan.studybuddy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
	private var inputEmail: TextInputLayout? = null
	private var inputPassword: TextInputLayout? = null
	private var auth: FirebaseAuth? = null
	private var progressBar: ProgressBar? = null
	private var btnSignup: Button? = null
	private var btnLogin: Button? = null
	private var btnReset: Button? = null
	private var signInButton: SignInButton? = null
	private var googleSignInClient: GoogleSignInClient? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Result code used to check for authentication
		RC_SIGN_IN = 9001
		auth = FirebaseAuth.getInstance()
		// Check if there's already an authenticated user
		if (auth!!.currentUser != null && SharedUtils.isNetworkAvailable(this)) {
			// This activity (`LoginActivity`) shouldn't be shown to an already authenticated user
			// Instead, redirect the user to the main activity and close this activity
			startActivity(Intent(this@LoginActivity, MainActivity::class.java))
			finish()
		}
		setContentView(R.layout.activity_login)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)

		inputEmail = findViewById(R.id.emailLogin)
		inputPassword = findViewById(R.id.passwordLogin)
		progressBar = findViewById(R.id.progressBar)
		btnSignup = findViewById(R.id.signUpBtn)
		btnLogin = findViewById(R.id.loginBtn)
		btnReset = findViewById(R.id.resetPasswordBtn)
		signInButton = findViewById(R.id.googleSignInBtn)
		signInButton!!.setColorScheme(SignInButton.COLOR_DARK)
		signInButton!!.setSize(SignInButton.SIZE_STANDARD)
		signInButton!!.setOnClickListener { view -> signInWithGoogle() }
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.web_client_id))
				.requestEmail()
				.build()
		googleSignInClient = GoogleSignIn.getClient(this, gso)

		btnSignup!!.setOnClickListener { v -> startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }

		btnReset!!.setOnClickListener { v -> startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java)) }

		btnLogin!!.setOnClickListener { v ->
			val email = SharedUtils.getEditTextString(inputEmail!!)
			val password = SharedUtils.getEditTextString(inputPassword!!)
			// Clear any previous errors
			inputEmail!!.error = null
			inputPassword!!.error = null
			if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
				if (TextUtils.isEmpty(email)) {

					inputEmail!!.error = "Please enter an email address"
				}
				if (TextUtils.isEmpty(password)) {
					inputPassword!!.error = "Please enter password"
				}
			}

			progressBar!!.visibility = View.VISIBLE
			// Authenticate the user
			auth!!.signInWithEmailAndPassword(email, password)
					.addOnCompleteListener(this@LoginActivity) { task ->
						// If sign in fails, display a message to the user. If sign in succeeds
						// the auth state listener will be notified and logic to handle the
						// signed in user can be handled in the listener.
						progressBar!!.visibility = View.GONE
						if (!task.isSuccessful) {
							Log.e(TAG, "An error occurred while attempting to login using an email and password.", task.exception)
							// there was an error
							if (password.length < 6) {
								inputPassword!!.error = getString(R.string.minimum_password)
							} else {
								Snackbar.make(findViewById(R.id.loginActivity), "An error occurred while attempting to login. Please try again later", Snackbar.LENGTH_LONG)
										.show()
							}
						} else {
							showLoginSnackBar()
							val intent = Intent(this@LoginActivity, MainActivity::class.java)
							startActivity(intent)
							finish()
						}
					}
		}
		checkNetwork()
	}

	public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == RC_SIGN_IN) {
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			try {
				val account = task.getResult(ApiException::class.java)
				firebaseAuthWithGoogle(account!!)
			} catch (e: ApiException) {
				Log.w(TAG, "Google sign in failed", e)
			}

		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	private fun showLoginSnackBar() {
		if (auth!!.currentUser != null) {
			// TODO(Edric): Figure out a way to show this snackbar before the main activity shows
			//			Snackbar.make(findViewById(android.R.id.content), String.format(getString(R.string.snackbar_user_login), auth.getCurrentUser().getEmail()), Snackbar.LENGTH_LONG).show();
			Toast.makeText(applicationContext, getString(R.string.snackbar_user_login, auth!!.currentUser!!.email), Toast.LENGTH_SHORT).show()
		}
	}

	private fun signInWithGoogle() {
		val signInIntent = googleSignInClient!!.signInIntent
		startActivityForResult(signInIntent, RC_SIGN_IN)
	}

	/**
	 * A method used to disable all functionality if no internet connection is available
	 *
	 *
	 * TODO: Bring this to SharedUtils instead
	 */
	private fun checkNetwork() {
		Log.d(TAG, "isNetworkAvailable: " + SharedUtils.isNetworkAvailable(this))
		if (SharedUtils.isNetworkAvailable(this)) {
			setViewsEnabled(true)
		} else {
			setViewsEnabled(false)
			Snackbar.make(findViewById(R.id.loginActivity), R.string.snackbar_internet_unavailable_login, Snackbar.LENGTH_INDEFINITE)
					.setBehavior(NoSwipeBehavior())
					.setAction(R.string.snackbar_internet_unavailable_action) { view -> checkNetwork() }.show()
		}
	}

	/**
	 * Sets all views as shown/hidden
	 *
	 * @param enabled Whether to show the views
	 */
	private fun setViewsEnabled(enabled: Boolean) {
		btnSignup!!.isEnabled = enabled
		btnLogin!!.isEnabled = enabled
		btnReset!!.isEnabled = enabled
		signInButton!!.isEnabled = enabled
		inputEmail!!.isEnabled = enabled
		inputPassword!!.isEnabled = enabled
	}

	/**
	 * Authorizes with Google
	 *
	 * @param acct The account
	 */
	private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
		Log.d(TAG, "firebaseAuthWithGoogle: called")

		val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
		auth!!.signInWithCredential(credential)
				.addOnCompleteListener(this) { task ->
					if (task.isSuccessful) {
						// Sign in success, update UI with the signed-in user's information
						Log.d(TAG, "Successfully signed in!")
						showLoginSnackBar()
						val intent = Intent(this@LoginActivity, MainActivity::class.java)
						startActivity(intent)
						finish()
					} else {
						// If sign in fails, display a message to the user.
						Log.e(TAG, "An error occurred", task.exception)
						Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
					}

					// ...
				}
				.addOnFailureListener { e ->
					// If sign in fails, display a message to the user.
					Log.e(TAG, "An error occured", e)
					Toast.makeText(applicationContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
				}
	}

	companion object {

		private val TAG = SharedUtils.getTag(LoginActivity::class.java)
		private var RC_SIGN_IN: Int = 0
	}

}