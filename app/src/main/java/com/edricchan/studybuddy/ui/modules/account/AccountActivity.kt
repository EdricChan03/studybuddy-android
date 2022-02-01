package com.edricchan.studybuddy.ui.modules.account

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.airbnb.deeplinkdispatch.DeepLink
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.annotations.AppDeepLink
import com.edricchan.studybuddy.annotations.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityAccountBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.showSnackbar
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@WebDeepLink(["/account"])
@AppDeepLink(["/account"])
class AccountActivity : AppCompatActivity(),
    FirebaseAuth.AuthStateListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAccountBinding
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        auth.addAuthStateListener(this)
        user = auth.currentUser

        binding.accountActionSignInButton.setOnClickListener {
            startActivity<LoginActivity>()
            finish()
        }
        binding.accountActionsButton
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
                when (val action = parameters.getString("action")) {
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
        updateSignedIn()
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.removeAuthStateListener(this)
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
        user = auth.currentUser
        updateSignedIn()
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
                user?.delete()
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully deleted account!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.e(
                                TAG,
                                "An error occurred when attempting to delete the current user.",
                                task.exception
                            )
                            when (task.exception) {
                                is FirebaseAuthRecentLoginRequiredException -> {
                                    val account = GoogleSignIn.getLastSignedInAccount(this)
                                    val credential =
                                        GoogleAuthProvider.getCredential(account?.idToken, null)
                                    user?.reauthenticate(credential)
                                        ?.addOnCompleteListener { reAuthTask ->
                                            if (reAuthTask.isSuccessful) {
                                                Toast.makeText(
                                                        this,
                                                        "Successfully reauthenticated account!",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                    .show()
                                                deleteAccount()
                                            } else {
                                                Log.e(
                                                    TAG,
                                                    "An error occurred while attempting to reauthenticate:",
                                                    reAuthTask.exception
                                                )
                                            }
                                        }
                                }
                                is FirebaseAuthInvalidUserException -> {
                                    Toast.makeText(
                                            this,
                                            "Current user is either disabled, deleted, or has invalid credentials",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            }
                        }
                    }
            }.show()
    }

    private fun updateSignedIn() {
        val isSignedIn = user != null
        // TODO: Show different UI if user isn't signed in
        binding.apply {
            accountActionsButton.visibility = if (isSignedIn) View.VISIBLE else View.GONE
            accountActionSignInButton.visibility = if (isSignedIn) View.GONE else View.VISIBLE
            accountNameTextView.apply {
                text = user?.displayName
                visibility = if (isSignedIn) View.VISIBLE else View.GONE
            }
            accountEmailTextView.apply {
                text = user?.email
                visibility = if (isSignedIn) View.VISIBLE else View.GONE
            }
            accountAvatarImageView.apply {
                visibility = if (isSignedIn) View.VISIBLE else View.GONE
                if (isSignedIn && user?.photoUrl != null) load(user?.photoUrl)
            }
        }
    }

    private fun refreshCredentials() {
        showSnackbar(binding.constraintLayout, "Refreshing credentials", Snackbar.LENGTH_SHORT)

        user?.reload()
            ?.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(
                        TAG,
                        "An error occurred while attempting to refresh the credentials:",
                        task.exception
                    )

                    showSnackbar(binding.constraintLayout, "An error occurred while attempting " +
                            "to refresh the credentials", Snackbar.LENGTH_LONG) {
                        setAction(R.string.dialog_action_retry) { refreshCredentials() }
                    }
                }
            }
    }

    private fun signOut() {
        val confirmBuilder = MaterialAlertDialogBuilder(this)
        confirmBuilder.setTitle(R.string.account_sign_out_dialog_title)
            .setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.dialog_action_sign_out) { dialog, _ ->
                auth.signOut()
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
            .setPositiveButton(R.string.dialog_action_update_email) { dialog, _ ->
                user?.updateEmail(textInputLayout.editTextStrValue)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully updated email address!",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        } else {
                            Log.e(
                                TAG,
                                "An error occurred when attempting to update the email address",
                                task.exception
                            )
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
                val requestBuilder = UserProfileChangeRequest.Builder().apply {
                    displayName = textInputLayout.editTextStrValue
                }
                user?.updateProfile(requestBuilder.build())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Successfully updated name!", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        } else {
                            Log.e(
                                TAG,
                                "An error occurred when attempting to update the name",
                                task.exception
                            )
                        }
                    }
            }
            .setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }

    private fun updatePassword() {
        val promptDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
        val textInputLayout = promptDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
        textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        textInputLayout.editText?.setHint(R.string.account_new_password_dialog_edittext_title)
        val promptBuilder = MaterialAlertDialogBuilder(this)
        promptBuilder.setView(promptDialogView)
            .setTitle(R.string.account_new_password_dialog_title)
            .setPositiveButton(R.string.dialog_action_update_password) { dialog, _ ->
                user?.updatePassword(textInputLayout.editTextStrValue)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Successfully updated password!",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        } else {
                            Log.e(
                                TAG,
                                "An error occurred when attempting to update the password",
                                task.exception
                            )
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
                Toast.makeText(this, "Successfully updated profile picture!", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
            .show()
    }
}
