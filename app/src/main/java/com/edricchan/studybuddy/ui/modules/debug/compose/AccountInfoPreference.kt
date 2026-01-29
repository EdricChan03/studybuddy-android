package com.edricchan.studybuddy.ui.modules.debug.compose

import android.content.ClipData
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.AccountCircle
import com.edricchan.studybuddy.data.common.Result
import com.edricchan.studybuddy.data.common.isSuccess
import com.edricchan.studybuddy.data.common.runCatching
import com.edricchan.studybuddy.exts.datetime.formatISO
import com.edricchan.studybuddy.exts.firebase.auth.creationInstant
import com.edricchan.studybuddy.exts.firebase.auth.lastSignInInstant
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.edricchan.studybuddy.core.resources.R as CoreResR

@Composable
private fun loadMessagingToken(
    messaging: FirebaseMessaging
) = produceState<Result<String>>(Result.Loading, messaging) {
    value = runCatching {
        messaging.token.await()
    }
}

@Composable
private fun AccountInfoDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCopyClick: (String) -> Unit,
    user: FirebaseUser?,
    token: Result<String>
) {
    val infoText = remember(user, token) {
        """
        Display name: ${user?.displayName ?: "<not set>"}
        Email: ${user?.email ?: "<not set>"}
        Metadata:
        - Creation timestamp: ${
            user?.metadata?.creationInstant
                ?.formatISO() ?: "<not set>"
        }
        - Last sign in timestamp: ${
            user?.metadata?.lastSignInInstant
                ?.formatISO() ?: "<not set>"
        }
        Phone number: ${user?.phoneNumber ?: "<not set>"}
        Photo URL: ${user?.photoUrl ?: "<not set>"}
        UID: ${user?.uid ?: "<not set>"}
        Is anonymous: ${user?.isAnonymous}
        ------
        Messaging token: ${if (token.isSuccess()) token.value else "Loading..."}
        """.trimIndent()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.debug_activity_account_info_title))
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                SelectionContainer {
                    Text(text = infoText, fontFamily = FontFamily.Monospace)
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (token.isSuccess()) {
                    TextButton(onClick = {
                        Log.d(
                            "AccountInfoDialog",
                            "Token: ${token.value}"
                        )
                    }) {
                        Text(text = "Log token")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onCopyClick(infoText)
            }) {
                Text(text = stringResource(android.R.string.copy))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(CoreResR.string.dialog_action_dismiss))
            }
        }
    )
}

@Composable
fun PreferenceCategoryScope.AccountInfoPreference(
    modifier: Modifier = Modifier,
    user: FirebaseUser?
) {
    var isAcctInfoShown by rememberSaveable { mutableStateOf(false) }

    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val tokenResult by loadMessagingToken(Firebase.messaging)

    Preference(
        modifier = modifier,
        onClick = { isAcctInfoShown = true },
        icon = { Icon(AppIcons.Outlined.AccountCircle, contentDescription = null) },
        title = { Text(text = stringResource(R.string.debug_activity_account_info_title)) },
        subtitle = { Text(text = stringResource(R.string.debug_activity_account_info_summary)) }
    )

    if (isAcctInfoShown) {
        AccountInfoDialog(
            onDismissRequest = { isAcctInfoShown = false },
            onCopyClick = {
                scope.launch {
                    clipboardManager.setClipEntry(ClipData.newPlainText(it, it).toClipEntry())
                }
            },
            user = user,
            token = tokenResult
        )
    }
}
