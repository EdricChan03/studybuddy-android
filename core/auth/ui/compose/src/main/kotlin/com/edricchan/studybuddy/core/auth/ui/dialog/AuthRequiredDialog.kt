package com.edricchan.studybuddy.core.auth.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.DialogProperties
import com.edricchan.studybuddy.core.auth.common.R
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Info

/** Shows a sign-in dialog prompting the user to log in to continue. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthRequiredDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onRegisterClick: () -> Unit,
    onSignInClick: () -> Unit
) = AlertDialog(
    modifier = modifier,
    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    onDismissRequest = onDismissRequest,
    icon = { Icon(AppIcons.Outlined.Info, contentDescription = null) },
    title = {
        Text(
            text = stringResource(R.string.dialog_requires_auth_title),
            textAlign = TextAlign.Center
        )
    },
    text = {
        Text(text = stringResource(R.string.dialog_requires_auth_msg))
    },
    dismissButton = {
        TextButton(
            onClick = onRegisterClick,
            shapes = ButtonDefaults.shapes()
        ) {
            Text(text = stringResource(R.string.account_action_register))
        }
    },
    confirmButton = {
        TextButton(
            onClick = onSignInClick,
            shapes = ButtonDefaults.shapes()
        ) {
            Text(text = stringResource(R.string.account_action_sign_in))
        }
    }
)
