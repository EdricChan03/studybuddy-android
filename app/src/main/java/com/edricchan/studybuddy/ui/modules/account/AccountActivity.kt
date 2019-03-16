package com.edricchan.studybuddy.ui.modules.account

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ContentView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*

@ContentView(R.layout.activity_account)
class AccountActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
	private var mAuth: FirebaseAuth? = null
	private var mUser: FirebaseUser? = null
	private val TAG = SharedUtils.getTag(AccountActivity::class.java)
	private var mAccountActions: Button? = null
	private var mActionSignInButton: Button? = null
	private var mAvatarImageView: ImageView? = null
	private var mDisplayNameTextView: TextView? = null
	private var mEmailTextView: TextView? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		mAvatarImageView = findViewById(R.id.accountAvatar)
		mDisplayNameTextView = findViewById(R.id.accountName)
		mEmailTextView = findViewById(R.id.accountEmail)
		mActionSignInButton = findViewById(R.id.accountActionSignIn)
		mAccountActions = findViewById(R.id.accountActions)
		mAuth = FirebaseAuth.getInstance()

		mActionSignInButton!!.setOnClickListener {
			val signInIntent = Intent(this, LoginActivity::class.java)
			startActivity(signInIntent)
			finish()
		}
		mAccountActions!!
				.setOnClickListener {
					val builder = MaterialAlertDialogBuilder(this)
					builder.setTitle(R.string.account_activity_account_actions)
							.setItems(R.array.account_activity_account_actions_array) { dialog, which ->
								when (which) {
									0 -> deleteAccount(dialog)
									1 -> signOut(dialog)
									2 -> updateEmail(dialog)
									3 -> updateName(dialog)
									4 -> updatePassword(dialog)
									5 -> updateProfilePicture(dialog)
									else -> Log.w(TAG, "Unknown item clicked! Index was at $which")
								}
							}
							.show()
				}
	}

	override fun onStart() {
		super.onStart()
		mUser = mAuth!!.currentUser
		if (mUser == null) {
			mDisplayNameTextView!!.visibility = View.GONE
			mEmailTextView!!.visibility = View.GONE
			Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show()
			mAccountActions!!.visibility = View.GONE
			mActionSignInButton!!.visibility = View.VISIBLE
			mAvatarImageView!!.visibility = View.GONE
		} else {
			mAccountActions!!.visibility = View.VISIBLE
			mActionSignInButton!!.visibility = View.GONE
			mDisplayNameTextView!!.text = mUser!!.displayName
			mEmailTextView!!.text = mUser!!.email
			mAvatarImageView!!.visibility = View.VISIBLE
			Glide.with(this)
					.load(mUser!!.photoUrl)
					.into(mAvatarImageView!!)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			R.id.action_debug -> {
				val debugIntent = Intent(this, DebugActivity::class.java)
				startActivity(debugIntent)
				return true
			}
			R.id.action_refresh_credentials -> {
				refreshCredentials()
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	override fun onAuthStateChanged(auth: FirebaseAuth) {
		val user = auth.currentUser
		if (user == null) {
			mDisplayNameTextView!!.visibility = View.GONE
			mEmailTextView!!.visibility = View.GONE
			Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show()
			mAccountActions!!.visibility = View.GONE
			mActionSignInButton!!.visibility = View.VISIBLE
			mAvatarImageView!!.visibility = View.GONE
		} else {
			mAccountActions!!.visibility = View.VISIBLE
			mActionSignInButton!!.visibility = View.GONE
			mDisplayNameTextView!!.text = user.displayName
			mEmailTextView!!.text = user.email
			mAvatarImageView!!.visibility = View.VISIBLE
			Glide.with(this)
					.load(user.photoUrl)
					.into(mAvatarImageView!!)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_account, menu)
		// Check if build is debug
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug)
		}
		return true
	}

	private fun deleteAccount(parentDialog: DialogInterface) {
		val confirmBuilder = MaterialAlertDialogBuilder(this)
		confirmBuilder.setTitle(R.string.account_activity_delete_account_dialog_title)
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
				.setPositiveButton(R.string.dialog_action_delete_account) { _, _ ->
					mUser!!.delete()
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Toast.makeText(this, "Successfully deleted account!", Toast.LENGTH_SHORT).show()
									parentDialog.dismiss()
								} else {
									Log.e(TAG, "An error occurred when attempting to delete the current user.", task.exception)
									if (task.exception is FirebaseAuthRecentLoginRequiredException) {
										// Reauthenticate the user to continue
										// TODO: Implement reauthentication
										val account = GoogleSignIn.getLastSignedInAccount(this)
										val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
										mUser!!.reauthenticate(credential)
												.addOnCompleteListener { reAuthTask ->
													if (reAuthTask.isSuccessful) {
														Toast.makeText(this, "Successfully reauthenticated account!", Toast.LENGTH_SHORT).show()
														deleteAccount(parentDialog)
													} else {
														Log.e(TAG, "An error occurred while attempting to reauthenticate:", reAuthTask.exception)
													}
												}
									} else if (task.exception is FirebaseAuthInvalidUserException) {
										Toast.makeText(this, "Current user is either disabled, deleted, or has invalid credentials", Toast.LENGTH_LONG).show()
										parentDialog.dismiss()
									}
								}
							}
				}
				.show()
	}

	private fun refreshCredentials() {
		Snackbar.make(findViewById(R.id.constraintLayout), "Refreshing credentials...", Snackbar.LENGTH_SHORT).show()
		mUser!!.reload()
				.addOnCompleteListener { task ->
					if (!task.isSuccessful) {
						Log.e(TAG, "An error occurred while attempting to refresh the credentials:", task.exception)
						Snackbar.make(findViewById(R.id.constraintLayout), "An error occurred while attempting to refresh the credentials", Snackbar.LENGTH_LONG)
								.setAction("Retry") { refreshCredentials() }
								.show()
					}
				}
	}

	private fun signOut(parentDialog: DialogInterface) {
		val confirmBuilder = MaterialAlertDialogBuilder(this)
		confirmBuilder.setTitle(R.string.account_activity_sign_out_dialog_title)
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
				.setPositiveButton(R.string.dialog_action_sign_out) { dialog, _ ->
					mAuth!!.signOut()
					Toast.makeText(this, "Successfully signed out!", Toast.LENGTH_SHORT).show()
					dialog.dismiss()
					parentDialog.dismiss()
				}
				.show()
	}

	private fun updateEmail(parentDialog: DialogInterface) {
		val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
		val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
		textInputLayout.editText?.setHint(R.string.account_activity_new_email_dialog_edittext_title)
		// TODO: Add check for email address
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_activity_new_email_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_email) { dialog, which ->
					mUser!!.updateEmail(textInputLayout.editTextStrValue!!)
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Toast.makeText(this, "Successfully updated email address!", Toast.LENGTH_SHORT).show()
									dialog.dismiss()
									parentDialog.dismiss()
								} else {
									Log.e(TAG, "An error occurred when attempting to update the email address", task.exception)
								}
							}
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
				.show()
	}

	private fun updateName(parentDialog: DialogInterface) {
		val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
		val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
		textInputLayout.editText?.setHint(R.string.account_activity_new_name_dialog_edittext_title)
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_activity_new_name_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_name) { dialog, _ ->
					val requestBuilder = UserProfileChangeRequest.Builder()
					requestBuilder.setDisplayName(SharedUtils.getEditTextString(textInputLayout))
					mUser!!.updateProfile(requestBuilder.build())
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Toast.makeText(this, "Successfully updated name!", Toast.LENGTH_SHORT).show()
									dialog.dismiss()
									parentDialog.dismiss()
								} else {
									Log.e(TAG, "An error occurred when attempting to update the name", task.exception)
								}
							}
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
				.show()
	}

	private fun updatePassword(parentDialog: DialogInterface) {
		val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
		val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
		textInputLayout.editText?.setHint(R.string.account_activity_new_password_dialog_edittext_title)
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_activity_new_password_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_password) { dialog, _ ->
					mUser!!.updatePassword(textInputLayout.editTextStrValue!!)
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Toast.makeText(this, "Successfully updated password!", Toast.LENGTH_SHORT).show()
									dialog.dismiss()
									parentDialog.dismiss()
								} else {
									Log.e(TAG, "An error occurred when attempting to update the password", task.exception)
								}
							}
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
				.show()
	}

	private fun updateProfilePicture(parentDialog: DialogInterface) {
		// TODO: Add support for updating a profile picture
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setTitle(R.string.account_activity_new_profile_pic_dialog_title)
				.setMessage(R.string.account_activity_new_profile_pic_dialog_msg)
				.setPositiveButton(R.string.dialog_action_update_profile_picture) { dialog, _ ->
					Toast.makeText(this, "Successfully updated profile picture!", Toast.LENGTH_SHORT).show()
					dialog.dismiss()
					parentDialog.dismiss()
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
				.show()
	}
}
