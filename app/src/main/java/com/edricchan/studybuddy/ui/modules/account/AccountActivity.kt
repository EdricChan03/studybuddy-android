package com.edricchan.studybuddy.ui.modules.account

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import coil.load
import com.airbnb.deeplinkdispatch.DeepLink
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.deeplink.AppDeepLink
import com.edricchan.studybuddy.core.deeplink.WebDeepLink
import com.edricchan.studybuddy.databinding.ActivityAccountBinding
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.isInvalidEmail
import com.edricchan.studybuddy.extensions.strValue
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.deleteAsync
import com.edricchan.studybuddy.exts.firebase.auth.reauthenticateAsync
import com.edricchan.studybuddy.exts.firebase.auth.reloadAsync
import com.edricchan.studybuddy.exts.firebase.auth.updateEmailAsync
import com.edricchan.studybuddy.exts.firebase.auth.updatePasswordAsync
import com.edricchan.studybuddy.exts.firebase.auth.updateProfileAsync
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.widget.prompt.showMaterialPromptDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@WebDeepLink(["/account"])
@AppDeepLink(["/account"])
class AccountActivity : BaseActivity(), FirebaseAuth.AuthStateListener {
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

        binding.apply {
            accountActionSignInButton.setOnClickListener {
                startActivity<LoginActivity>()
                finish()
            }
            accountActionsButton.setOnClickListener {
                showMaterialAlertDialog {
                    setTitle(R.string.account_account_actions_button_title)
                    setItems(R.array.account_activity_account_actions_array) { _, which ->
                        when (which) {
                            0 -> showDeleteAccountDialog()
                            1 -> showSignOutDialog()
                            2 -> showUpdateEmailDialog()
                            3 -> showUpdateNameDialog()
                            4 -> showUpdatePasswordDialog()
                            5 -> showUpdateProfilePictureDialog()
                            else -> Log.w(TAG, "Unknown item clicked! Index was at $which")
                        }
                    }
                }
            }
        }

        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            intent.extras?.getString("action")?.let {
                when (it) {
                    "deleteAccount", "delete-account" -> showDeleteAccountDialog()
                    "signOut", "sign-out" -> showSignOutDialog()
                    "updateEmail", "update-email" -> showUpdateEmailDialog()
                    "updateName", "update-name" -> showUpdateNameDialog()
                    "updatePassword", "update-password" -> showUpdatePasswordDialog()
                    "updateProfilePicture", "update-profile-picture" -> showUpdateProfilePictureDialog()
                    else -> Log.w(TAG, "Action \"$it\" is not supported")
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
                onBackPressedDispatcher.onBackPressed()
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

    private fun showDeleteAccountDialog() {
        showMaterialAlertDialog {
            setTitle(R.string.account_delete_account_dialog_title)
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(R.string.dialog_action_delete_account) { _, _ ->
                lifecycleScope.launch {
                    try {
                        user?.deleteAsync()
                        showToast(
                            R.string.account_delete_account_success_msg,
                            Toast.LENGTH_SHORT
                        )
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "An error occurred when attempting to delete the current user.",
                            e
                        )
                        when (e) {
                            is FirebaseAuthRecentLoginRequiredException -> {
                                val account =
                                    GoogleSignIn.getLastSignedInAccount(this@AccountActivity)
                                val credential =
                                    GoogleAuthProvider.getCredential(account?.idToken, null)
                                try {
                                    user?.reauthenticateAsync(credential)
                                    showToast(
                                        R.string.account_delete_account_reauth_success_msg,
                                        Toast.LENGTH_SHORT
                                    )
                                    showDeleteAccountDialog()
                                } catch (e: Exception) {
                                    Log.e(
                                        TAG,
                                        "An error occurred while attempting to reauthenticate:",
                                        e
                                    )
                                }
                            }

                            is FirebaseAuthInvalidUserException -> {
                                showToast(
                                    R.string.account_delete_account_invalid_msg,
                                    Toast.LENGTH_LONG
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateSignedIn() {
        val isSignedIn = user != null
        // TODO: Show different UI if user isn't signed in
        binding.apply {
            accountActionsButton.isVisible = isSignedIn
            accountActionSignInButton.isVisible = !isSignedIn
            accountNameTextView.apply {
                text = user?.displayName
                isVisible = isSignedIn
            }
            accountEmailTextView.apply {
                text = user?.email
                isVisible = isSignedIn
            }
            accountAvatarImageView.apply {
                isVisible = isSignedIn
                if (isSignedIn && user?.photoUrl != null) load(user?.photoUrl)
            }
        }
    }

    private fun refreshCredentials() {
        showSnackbar(
            binding.constraintLayout,
            R.string.account_refresh_creds_start_msg,
            Snackbar.LENGTH_SHORT
        )

        lifecycleScope.launch {
            try {
                user?.reloadAsync()
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "An error occurred while attempting to refresh the credentials:",
                    e
                )

                showSnackbar(
                    binding.constraintLayout,
                    R.string.account_refresh_creds_error_msg,
                    Snackbar.LENGTH_LONG
                ) {
                    setAction(R.string.dialog_action_retry) { refreshCredentials() }
                }
            }
        }
    }

    private fun showSignOutDialog() {
        showMaterialAlertDialog {
            setTitle(R.string.account_sign_out_dialog_title)
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(R.string.dialog_action_sign_out) { dialog, _ ->
                auth.signOut()
                showToast(R.string.account_log_out_success_msg, Toast.LENGTH_SHORT)
                dialog.dismiss()
            }
        }
    }

    private fun showUpdateEmailDialog() {
        showMaterialPromptDialog {
            textInputEditText {
                setHint(R.string.account_new_email_dialog_edittext_title)
                doAfterTextChanged {
                    error = if (it?.toString()?.isInvalidEmail() == true) {
                        getString(R.string.account_new_email_dialog_edittext_err_invalid_email)
                    } else {
                        null
                    }
                }
            }
            setTitle(R.string.account_new_email_dialog_title)
            setPositiveButton(R.string.dialog_action_update_email) { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        textInputEditText.strValue?.let { user?.updateEmailAsync(it) }
                        showToast(R.string.account_update_email_success_msg, Toast.LENGTH_SHORT)
                        dialog.dismiss()
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "An error occurred when attempting to update the email address",
                            e
                        )
                    }
                }
            }
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdateNameDialog() {
        showMaterialPromptDialog {
            textInputEditText {
                setHint(R.string.account_new_name_dialog_edittext_title)
            }
            setTitle(R.string.account_new_name_dialog_title)
            setPositiveButton(R.string.dialog_action_update_name) { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        user?.updateProfileAsync {
                            displayName = textInputLayout.editTextStrValue
                        }
                        showToast(R.string.account_update_name_success_msg, Toast.LENGTH_SHORT)
                        dialog.dismiss()
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "An error occurred when attempting to update the name",
                            e
                        )
                    }
                }
            }
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdatePasswordDialog() {
        showMaterialPromptDialog {
            textInputLayout {
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }
            textInputEditText {
                setHint(R.string.account_new_password_dialog_edittext_title)
            }
            setTitle(R.string.account_new_password_dialog_title)
            setPositiveButton(R.string.dialog_action_update_password) { dialog, _ ->
                lifecycleScope.launch {
                    try {
                        textInputEditText.strValue?.let { user?.updatePasswordAsync(it) }
                        showToast(
                            R.string.account_update_password_success_msg, Toast.LENGTH_SHORT
                        )
                        dialog.dismiss()
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "An error occurred when attempting to update the password",
                            e
                        )
                    }
                }
            }
            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdateProfilePictureDialog() {
        // TODO: Add support for updating a profile picture
        showMaterialAlertDialog {
            setTitle(R.string.account_new_profile_pic_dialog_title)
            setMessage(R.string.account_new_profile_pic_dialog_msg)
//            setPositiveButton(R.string.dialog_action_update_profile_picture) { dialog, _ ->
//                showToast(R.string.account_update_pic_success_msg, Toast.LENGTH_SHORT)
//                dialog.dismiss()
//            }
            setPositiveButton(R.string.dialog_action_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
        }
    }
}
