package com.edricchan.studybuddy.features.auth.account.compat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.toRoute
import coil3.load
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination.Auth.Account.AccountAction
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToLogin
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.deleteAsync
import com.edricchan.studybuddy.exts.firebase.auth.reauthenticateAsync
import com.edricchan.studybuddy.exts.firebase.auth.reloadAsync
import com.edricchan.studybuddy.exts.firebase.auth.updateEmailAsync
import com.edricchan.studybuddy.exts.firebase.auth.updatePasswordAsync
import com.edricchan.studybuddy.exts.firebase.auth.updateProfileAsync
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.exts.material.textfield.strValue
import com.edricchan.studybuddy.features.auth.R
import com.edricchan.studybuddy.features.auth.databinding.FragAccountInfoBinding
import com.edricchan.studybuddy.features.auth.exts.isInvalidEmail
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.edricchan.studybuddy.ui.widget.prompt.showMaterialPromptDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import com.edricchan.studybuddy.core.resources.R as CommonR

class AccountFragment :
    ViewBindingFragment<FragAccountInfoBinding>(FragAccountInfoBinding::inflate),
    FirebaseAuth.AuthStateListener {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        auth.addAuthStateListener(this)
        user = auth.currentUser

        binding.apply {
            accountActionSignInButton.setOnClickListener {
                navController.navigateToLogin()
            }
            accountActionsButton.setOnClickListener {
                requireContext().showMaterialAlertDialog {
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

        // We can use this fragment without navigating to it via Jetpack Nav (in SettingsFragment),
        // so getBackStackEntry throws an IllegalArgumentException which we have to catch
        val action = runCatching {
            navController.getBackStackEntry<CompatDestination.Auth.Account>()
                .toRoute<CompatDestination.Auth.Account>()
                .action
        }.getOrNull()
        action?.let {
            when (it) {
                AccountAction.DeleteAccount -> showDeleteAccountDialog()
                AccountAction.SignOut -> showSignOutDialog()
                AccountAction.UpdateEmail -> showUpdateEmailDialog()
                AccountAction.UpdateName -> showUpdateNameDialog()
                AccountAction.UpdatePassword -> showUpdatePasswordDialog()
                AccountAction.UpdateProfilePicture -> showUpdateProfilePictureDialog()
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

    override val menuProvider = object : MenuProvider {
        override fun onCreateMenu(
            menu: Menu,
            menuInflater: MenuInflater
        ) {
//            menu.findItem(R.id.action_account)?.isVisible = false
            menuInflater.inflate(R.menu.menu_account, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_refresh_credentials -> {
                    refreshCredentials()
                    return true
                }

                else -> false
            }
        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {
        user = auth.currentUser
        updateSignedIn()
    }

    private fun showDeleteAccountDialog() {
        requireContext().showMaterialAlertDialog {
            setTitle(R.string.account_delete_account_dialog_title)
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
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
                                    GoogleSignIn.getLastSignedInAccount(requireContext())
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
        showSnackBar(
            R.string.account_refresh_creds_start_msg,
            SnackBarData.Duration.Short
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

                showSnackBar(
                    messageRes = R.string.account_refresh_creds_error_msg,
                    duration = SnackBarData.Duration.Long,
                    action = SnackBarData.Action(
                        text = getString(CommonR.string.dialog_action_retry),
                        onClick = ::refreshCredentials
                    )
                )
            }
        }
    }

    private fun showSignOutDialog() {
        requireContext().showMaterialAlertDialog {
            setTitle(R.string.account_sign_out_dialog_title)
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(R.string.dialog_action_sign_out) { dialog, _ ->
                auth.signOut()
                lifecycleScope.launch {
                    try {
                        val clearRequest = ClearCredentialStateRequest()
                        CredentialManager.create(requireContext())
                            .clearCredentialState(clearRequest)
                    } catch (e: ClearCredentialException) {
                        Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
                    }
                }
                showToast(R.string.account_log_out_success_msg, Toast.LENGTH_SHORT)
                dialog.dismiss()
            }
        }
    }

    private fun showUpdateEmailDialog() {
        requireContext().showMaterialPromptDialog {
            textInputLayout {
                setHint(R.string.account_new_email_dialog_edittext_title)
            }
            textInputLayout {
                textInputEditText.doAfterTextChanged {
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
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdateNameDialog() {
        requireContext().showMaterialPromptDialog {
            textInputLayout {
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
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdatePasswordDialog() {
        requireContext().showMaterialPromptDialog {
            textInputLayout {
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
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
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdateProfilePictureDialog() {
        // TODO: Add support for updating a profile picture
        requireContext().showMaterialAlertDialog {
            setTitle(R.string.account_new_profile_pic_dialog_title)
            setMessage(R.string.account_new_profile_pic_dialog_msg)
//            setPositiveButton(R.string.dialog_action_update_profile_picture) { dialog, _ ->
//                showToast(R.string.account_update_pic_success_msg, Toast.LENGTH_SHORT)
//                dialog.dismiss()
//            }
            setPositiveButton(CommonR.string.dialog_action_dismiss) { dialog, _ ->
                dialog.dismiss()
            }
        }
    }
}
