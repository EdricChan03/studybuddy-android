package com.edricchan.studybuddy.ui.common.dialogs

import android.content.Context
import android.content.DialogInterface
import androidx.navigation.NavController
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToLogin
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToRegister
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.ui.common.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.edricchan.studybuddy.core.resources.R as CoreResR

/**
 * Shows a sign in dialog, navigating the user to the login/register activities
 * respectively.
 * @param onNavigateToLogin Invoked when a request to navigate to the login UI is made.
 * @param onNavigateToRegister Invoked when a request to navigate to the register UI is made.
 * @param isCancelable Whether this dialog should be cancelable. (See
 * [MaterialAlertDialogBuilder.setCancelable])
 * @param showCancelButton Whether a cancel button should be shown.
 * @param dialogInit Additional [MaterialAlertDialogBuilder] configuration. This is called
 * **after** the default options have been set.
 * @return The shown [android.app.AlertDialog].
 */
fun Context.showAuthRequiredDialog(
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    isCancelable: Boolean = false,
    showCancelButton: Boolean = false,
    dialogInit: MaterialAlertDialogBuilder.() -> Unit = {}
) = showMaterialAlertDialog {
    setTitle(R.string.auth_required_dialog_title)
    setMessage(R.string.auth_required_dialog_msg)
    setCancelable(isCancelable)
    val onLoginClick = DialogInterface.OnClickListener { dialog, _ ->
        onNavigateToLogin()
        dialog.dismiss()
    }
    val onSignUpClick = DialogInterface.OnClickListener { dialog, _ ->
        onNavigateToRegister()
        dialog.dismiss()
    }

    if (showCancelButton) {
        setPositiveButton(R.string.dialog_action_login, onLoginClick)
        setNeutralButton(R.string.dialog_action_sign_up, onSignUpClick)
        setNegativeButton(CoreResR.string.dialog_action_cancel) { dialog, _ ->
            dialog.cancel()
        }
    } else {
        setPositiveButton(R.string.dialog_action_login, onLoginClick)
        setNegativeButton(R.string.dialog_action_sign_up, onSignUpClick)
    }

    dialogInit()
}

/**
 * Shows a sign in dialog, navigating the user to the login/register activities
 * respectively.
 * @param navController [NavController] to be used when clicking on the Register or
 * Login buttons.
 * @param isCancelable Whether this dialog should be cancelable. (See
 * [MaterialAlertDialogBuilder.setCancelable])
 * @param showCancelButton Whether a cancel button should be shown.
 * @param dialogInit Additional [MaterialAlertDialogBuilder] configuration. This is called
 * **after** the default options have been set.
 * @return The shown [android.app.AlertDialog].
 */
fun Context.showAuthRequiredDialog(
    navController: NavController,
    isCancelable: Boolean = false,
    showCancelButton: Boolean = false,
    dialogInit: MaterialAlertDialogBuilder.() -> Unit = {}
) = showAuthRequiredDialog(
    onNavigateToLogin = navController::navigateToLogin,
    onNavigateToRegister = navController::navigateToRegister,
    isCancelable = isCancelable,
    showCancelButton = showCancelButton,
    dialogInit = dialogInit
)
