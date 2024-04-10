package com.edricchan.studybuddy.ui.dialogs

import android.content.Context
import android.content.DialogInterface
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.auth.ui.LoginActivity
import com.edricchan.studybuddy.features.auth.ui.RegisterActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Shows a sign in dialog, navigating the user to the login/register activities
 * respectively.
 * @param isCancelable Whether this dialog should be cancelable. (See
 * [MaterialAlertDialogBuilder.setCancelable])
 * @param showCancelButton Whether a cancel button should be shown.
 * @param dialogInit Additional [MaterialAlertDialogBuilder] configuration. This is called
 * **after** the default options have been set.
 * @return The shown [android.app.AlertDialog].
 */
fun Context.showAuthRequiredDialog(
    isCancelable: Boolean = false,
    showCancelButton: Boolean = false,
    dialogInit: MaterialAlertDialogBuilder.() -> Unit = {}
) = showMaterialAlertDialog {
    setTitle(R.string.auth_required_dialog_title)
    setMessage(R.string.auth_required_dialog_msg)
    setCancelable(isCancelable)
    val onLoginClick = DialogInterface.OnClickListener { dialog, _ ->
        startActivity<LoginActivity>()
        dialog.dismiss()
    }
    val onSignUpClick = DialogInterface.OnClickListener { dialog, _ ->
        startActivity<RegisterActivity>()
        dialog.dismiss()
    }

    if (showCancelButton) {
        setPositiveButton(R.string.dialog_action_login, onLoginClick)
        setNeutralButton(R.string.dialog_action_sign_up, onSignUpClick)
        setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
            dialog.cancel()
        }
    } else {
        setPositiveButton(R.string.dialog_action_login, onLoginClick)
        setNegativeButton(R.string.dialog_action_sign_up, onSignUpClick)
    }

    dialogInit()
}
