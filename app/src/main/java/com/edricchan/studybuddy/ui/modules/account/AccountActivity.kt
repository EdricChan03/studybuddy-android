package com.edricchan.studybuddy.ui.modules.account

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLink
import com.bumptech.glide.Glide
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_account.*


@WebDeepLink(["/account"])
@AppDeepLink(["/account"])
class AccountActivity : AppCompatActivity(R.layout.activity_account), FirebaseAuth.AuthStateListener {
	private var mAuth: FirebaseAuth? = null
	private var mUser: FirebaseUser? = null
	private val TAG = SharedUtils.getTag(AccountActivity::class.java)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mAuth = FirebaseAuth.getInstance()

		accountActionSignInButton.setOnClickListener {
			startActivity<LoginActivity>()
			finish()
		}
		accountActionsButton
				.setOnClickListener {
					val builder = MaterialAlertDialogBuilder(this)
					builder.setTitle(R.string.account_account_actions_button_title)
							.setItems(R.array.account_activity_account_actions_array) { dialog, which ->
								when (which) {
									0 -> deleteAccount()
									1 -> signOut()
									2 -> updateEmail()
									3 -> updateName()
									4 -> updatePassword()
									5 -> updateProfilePicture()
									else -> Log.w(TAG, "Unknown item clicked! Index was at $which")
								}
							}
							.show()
				}
		if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
			val parameters = intent.extras
			if (parameters?.getString("action") != null) {
				val action = parameters.getString("action")
				when (action) {
					"deleteAccount", "delete-account" -> deleteAccount()
					"signOut", "sign-out" -> signOut()
					"updateEmail", "update-email" -> updateEmail()
					"updateName", "update-name" -> updateName()
					"updatePassword", "update-password" -> updatePassword()
					"updateProfilePicture", "update-profile-picture" -> updateProfilePicture()
					else -> Log.w(TAG, "\"$action\" is not supported")
				}
			}
		}
	}

	override fun onStart() {
		super.onStart()
		mUser = mAuth?.currentUser
		if (mUser == null) {
			accountNameTextView.visibility = View.GONE
			accountEmailTextView.visibility = View.GONE
			Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show()
			accountActionsButton.visibility = View.GONE
			accountActionSignInButton.visibility = View.VISIBLE
			accountAvatarImageView.visibility = View.GONE
		} else {
			accountActionsButton.visibility = View.VISIBLE
			accountActionSignInButton.visibility = View.GONE
			accountNameTextView.text = mUser?.displayName
			accountEmailTextView.text = mUser?.email
			accountAvatarImageView.visibility = View.VISIBLE
			Glide.with(this)
					.load(mUser?.photoUrl)
					.into(accountAvatarImageView)
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				true
			}
			R.id.action_debug -> {
				startActivity<DebugActivity>()
				true
			}
			R.id.action_refresh_credentials -> {
				refreshCredentials()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}

	override fun onAuthStateChanged(auth: FirebaseAuth) {
		val user = auth.currentUser
		if (user == null) {
			accountNameTextView.visibility = View.GONE
			accountEmailTextView.visibility = View.GONE
			Toast.makeText(this, "Not signed in!", Toast.LENGTH_SHORT).show()
			accountActionsButton.visibility = View.GONE
			accountActionSignInButton.visibility = View.VISIBLE
			accountAvatarImageView.visibility = View.GONE
		} else {
			accountActionsButton.visibility = View.VISIBLE
			accountActionSignInButton.visibility = View.GONE
			accountNameTextView.text = user.displayName
			accountEmailTextView.text = user.email
			accountAvatarImageView.visibility = View.VISIBLE
			Glide.with(this)
					.load(user.photoUrl)
					.into(accountAvatarImageView)
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

	private fun deleteAccount() {
		val confirmBuilder = MaterialAlertDialogBuilder(this)
		confirmBuilder.setTitle(R.string.account_delete_account_dialog_title)
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
				.setPositiveButton(R.string.dialog_action_delete_account) { _, _ ->
					mUser?.delete()
							?.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Toast.makeText(this, "Successfully deleted account!", Toast.LENGTH_SHORT).show()
								} else {
									Log.e(TAG, "An error occurred when attempting to delete the current user.", task.exception)
									when (task.exception) {
										is FirebaseAuthRecentLoginRequiredException -> {
											val account = GoogleSignIn.getLastSignedInAccount(this)
											val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
											mUser?.reauthenticate(credential)
													?.addOnCompleteListener { reAuthTask ->
														if (reAuthTask.isSuccessful) {
															Toast.makeText(this, "Successfully reauthenticated account!", Toast.LENGTH_SHORT).show()
															deleteAccount()
														} else {
															Log.e(TAG, "An error occurred while attempting to reauthenticate:", reAuthTask.exception)
														}
													}
										}
										is FirebaseAuthInvalidUserException -> {
											Toast.makeText(this, "Current user is either disabled, deleted, or has invalid credentials", Toast.LENGTH_LONG).show()
										}
									}
								}
							}
				}.show()
	}

	private fun refreshCredentials() {
		Snackbar.make(findViewById(R.id.constraintLayout), "Refreshing credentials...", Snackbar.LENGTH_SHORT).show()
		mUser?.reload()
				?.addOnCompleteListener { task ->
					if (!task.isSuccessful) {
						Log.e(TAG, "An error occurred while attempting to refresh the credentials:", task.exception)
						Snackbar.make(findViewById(R.id.constraintLayout), "An error occurred while attempting to refresh the credentials", Snackbar.LENGTH_LONG)
								.setAction("Retry") { refreshCredentials() }
								.show()
					}
				}
	}

	private fun signOut() {
		val confirmBuilder = MaterialAlertDialogBuilder(this)
		confirmBuilder.setTitle(R.string.account_sign_out_dialog_title)
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
				.setPositiveButton(R.string.dialog_action_sign_out) { dialog, _ ->
					mAuth?.signOut()
					Toast.makeText(this, "Successfully signed out!", Toast.LENGTH_SHORT).show()
					dialog.dismiss()
				}
				.show()
	}

	private fun updateEmail() {
		val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
		val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
		textInputLayout.editText?.setHint(R.string.account_new_email_dialog_edittext_title)
		// TODO: Add check for email address
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_new_email_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_email) { dialog, which ->
					textInputLayout.editTextStrValue?.let {
						mUser?.updateEmail(it)
								?.addOnCompleteListener { task ->
									if (task.isSuccessful) {
										Toast.makeText(this, "Successfully updated email address!", Toast.LENGTH_SHORT).show()
										dialog.dismiss()
									} else {
										Log.e(TAG, "An error occurred when attempting to update the email address", task.exception)
									}
								}
					}
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
				.show()
	}

	private fun updateName() {
		val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
		val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
		textInputLayout.editText?.setHint(R.string.account_new_name_dialog_edittext_title)
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_new_name_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_name) { dialog, _ ->
					val requestBuilder = UserProfileChangeRequest.Builder()
					requestBuilder.setDisplayName(textInputLayout.editTextStrValue)
					mUser?.updateProfile(requestBuilder.build())
							?.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Toast.makeText(this, "Successfully updated name!", Toast.LENGTH_SHORT).show()
									dialog.dismiss()
								} else {
									Log.e(TAG, "An error occurred when attempting to update the name", task.exception)
								}
							}
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
				.show()
	}

	private fun updatePassword() {
		val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
		val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
		textInputLayout.editText?.setHint(R.string.account_new_password_dialog_edittext_title)
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setView(promptDialogView)
				.setTitle(R.string.account_new_password_dialog_title)
				.setPositiveButton(R.string.dialog_action_update_password) { dialog, _ ->
					textInputLayout.editTextStrValue?.let {
						mUser?.updatePassword(it)
								?.addOnCompleteListener { task ->
									if (task.isSuccessful) {
										Toast.makeText(this, "Successfully updated password!", Toast.LENGTH_SHORT).show()
										dialog.dismiss()
									} else {
										Log.e(TAG, "An error occurred when attempting to update the password", task.exception)
									}
								}
					}
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
				.show()
	}

	private fun updateProfilePicture() {
		// TODO: Add support for updating a profile picture
		val promptBuilder = MaterialAlertDialogBuilder(this)
		promptBuilder.setTitle(R.string.account_new_profile_pic_dialog_title)
				.setMessage(R.string.account_new_profile_pic_dialog_msg)
				.setPositiveButton(R.string.dialog_action_update_profile_picture) { dialog, _ ->
					Toast.makeText(this, "Successfully updated profile picture!", Toast.LENGTH_SHORT).show()
					dialog.dismiss()
				}
				.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
				.show()
	}
}
