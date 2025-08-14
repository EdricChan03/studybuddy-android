package com.edricchan.studybuddy.ui.dialogs

import android.content.Context
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.features.auth.ui.LoginActivity
import com.edricchan.studybuddy.features.auth.ui.RegisterActivity
import com.edricchan.studybuddy.ui.common.dialogs.showAuthRequiredDialog
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
@Deprecated(
    "Use the common variant of this function which requires specifying " +
        "how to navigate to the register and login end-points respectively",
    ReplaceWith(
        "this.showAuthRequiredDialog(\n" +
            "  onNavigateToRegister = { this.startActivity<RegisterActivity>() },\n" +
            "  onNavigateToLogin = { this.startActivity<LoginActivity>() },\n" +
            "  isCancelable = isCancelable, showCancelButton = showCancelButton,\n" +
            "  dialogInit = dialogInit\n" +
            ")",
        "com.edricchan.studybuddy.exts.android.startActivity",
        "com.edricchan.studybuddy.features.auth.ui.LoginActivity",
        "com.edricchan.studybuddy.features.auth.ui.RegisterActivity",
        "com.edricchan.studybuddy.ui.common.dialogs.showAuthRequiredDialog"
    )
)
fun Context.showAuthRequiredDialog(
    isCancelable: Boolean = false,
    showCancelButton: Boolean = false,
    dialogInit: MaterialAlertDialogBuilder.() -> Unit = {}
) = showAuthRequiredDialog(
    onNavigateToRegister = { startActivity<RegisterActivity>() },
    onNavigateToLogin = { startActivity<LoginActivity>() },
    isCancelable = isCancelable,
    showCancelButton = showCancelButton,
    dialogInit = dialogInit
)
