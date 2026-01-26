package com.edricchan.studybuddy.features.auth.account.compat

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.toRoute
import coil3.load
import com.edricchan.studybuddy.core.auth.service.AuthService
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination.Auth.Account.AccountAction
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToLogin
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.firebase.auth.deleteAsync
import com.edricchan.studybuddy.exts.firebase.auth.reauthenticateAsync
import com.edricchan.studybuddy.exts.firebase.auth.reloadAsync
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
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
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.recordException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.edricchan.studybuddy.core.resources.R as CommonR

@AndroidEntryPoint
class AccountFragment :
    ViewBindingFragment<FragAccountInfoBinding>(FragAccountInfoBinding::inflate),
    FirebaseAuth.AuthStateListener {
    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    private var user: FirebaseUser? = null

    @Inject
    lateinit var authService: AuthService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            menuInflater.inflate(R.menu.menu_account, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_refresh_credentials -> {
                    refreshCredentials()
                    true
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
        lifecycleScope.launch {
            showSnackBar(
                R.string.account_refresh_creds_start_msg,
                SnackBarData.Duration.Short
            ).join()
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
                ).join()
            }
        }
    }

    private fun showSignOutDialog() {
        requireContext().showMaterialAlertDialog {
            setTitle(R.string.account_sign_out_dialog_title)
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
            setPositiveButton(R.string.dialog_action_sign_out) { dialog, _ ->
                with(authService) {
                    lifecycleScope.launch {
                        requireContext().signOut()
                        showToast(R.string.account_log_out_success_msg, Toast.LENGTH_SHORT)
                    }
                }
                dialog.dismiss()
            }
        }
    }

    private fun showUpdateEmailDialog() {
        requireContext().showMaterialPromptDialog(
            defaultIsEnabled = false
        ) {
            textInputLayout {
                setHint(R.string.account_new_email_dialog_edittext_title)
                textInputEditText.doAfterTextChanged {
                    error = if (it?.toString()?.isInvalidEmail() == true) {
                        getString(R.string.account_new_email_dialog_edittext_err_invalid_email)
                    } else {
                        null
                    }
                }
            }
            textInputEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setTitle(R.string.account_new_email_dialog_title)
            setPositiveButton(R.string.dialog_action_update_email) { dialog, _ ->
                onProfileUpdate(
                    successMsgRes = R.string.account_update_email_success_msg,
                    updateTypeKeyValue = "email",
                    onDismissRequest = dialog::dismiss
                ) {
                    inputText?.takeIf(String::isNotBlank)
                        ?.let { authService.updateEmail(it) }
                }
            }
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdateNameDialog() {
        requireContext().showMaterialPromptDialog(
            defaultIsEnabled = false
        ) {
            textInputLayout {
                setHint(R.string.account_new_name_dialog_edittext_title)
            }
            setTitle(R.string.account_new_name_dialog_title)
            setPositiveButton(R.string.dialog_action_update_name) { dialog, _ ->
                onProfileUpdate(
                    successMsgRes = R.string.account_update_name_success_msg,
                    updateTypeKeyValue = "display_name",
                    onDismissRequest = dialog::dismiss
                ) {
                    inputText?.takeIf(String::isNotBlank)
                        ?.let { authService.updateDisplayName(it) }
                }
            }
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    private fun showUpdatePasswordDialog() {
        requireContext().showMaterialPromptDialog(
            defaultIsEnabled = false
        ) {
            textInputLayout {
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                setHint(R.string.account_new_password_dialog_edittext_title)
            }
            textInputEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setTitle(R.string.account_new_password_dialog_title)
            setPositiveButton(R.string.dialog_action_update_password) { dialog, _ ->
                onProfileUpdate(
                    successMsgRes = R.string.account_update_password_success_msg,
                    updateTypeKeyValue = "password",
                    onDismissRequest = dialog::dismiss
                ) {
                    inputText?.takeIf(String::isNotBlank)
                        ?.let { authService.updatePassword(it) }
                }
            }
            setNegativeButton(CommonR.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
        }
    }

    @SuppressLint("DiscouragedApi") // For showSnackBar call that accepts a String
    private fun onProfileUpdate(
        @StringRes successMsgRes: Int,
        updateTypeKeyValue: String,
        onDismissRequest: () -> Unit,
        updateFn: suspend () -> AuthService.UpdateProfileResult?
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = updateFn() ?: return@launch run(onDismissRequest)) {
                is AuthService.UpdateProfileResult.Error -> {
                    Log.e(
                        this@AccountFragment.TAG,
                        "Could not update profile information:",
                        result.error
                    )
                    crashlytics.recordException(result.error) {
                        key("profile_update_type", updateTypeKeyValue)
                    }
                    result.error.localizedMessage?.let {
                        showSnackBar(
                            it,
                            SnackBarData.Duration.Long
                        )
                    }
                }

                // This case should not happen, ideally...
                AuthService.UpdateProfileResult.NotSignedIn -> {
                    Log.w(
                        this@AccountFragment.TAG,
                        "User not signed in, unable to perform profile update ($updateTypeKeyValue)"
                    )
                }

                AuthService.UpdateProfileResult.Success -> {
                    showSnackBar(successMsgRes)
                }
            }
            onDismissRequest()
        }
    }
}
