package com.edricchan.studybuddy.ui.modules.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

@ContentView(R.layout.activity_reset_password)
class ResetPasswordActivity : AppCompatActivity() {

	private var inputEmail: EditText? = null
	private var btnReset: Button? = null
	private var btnBack: Button? = null
	private var auth: FirebaseAuth? = null
	private var progressBar: ProgressBar? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		inputEmail = findViewById(R.id.email)
		btnReset = findViewById(R.id.btn_reset_password)
		btnBack = findViewById(R.id.btn_back)
		progressBar = findViewById(R.id.progressBar)

		auth = FirebaseAuth.getInstance()

		btnBack!!.setOnClickListener { finish() }

		btnReset!!.setOnClickListener {

			val email = inputEmail!!.text.toString().trim { it <= ' ' }

			if (TextUtils.isEmpty(email)) {
				Snackbar.make(findViewById(R.id.mainView), "Please enter an email address", Snackbar.LENGTH_LONG).show()
				return@setOnClickListener
			}

			progressBar!!.visibility = View.VISIBLE
			auth!!.sendPasswordResetEmail(email)
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							Snackbar.make(findViewById(R.id.mainView), "An email has been sent to the email address that you have specified", Snackbar.LENGTH_LONG)
									.show()
						} else {
							Snackbar.make(findViewById(R.id.mainView), "An error occurred while attempting to send an email. Please try again later", Snackbar.LENGTH_LONG)
									.show()
							Log.e(TAG, "An error occurred while attempting to send a reset password email.", task.exception)
						}

						progressBar!!.visibility = View.GONE
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

	companion object {
		private val TAG = SharedUtils.getTag(ResetPasswordActivity::class.java)
	}
}