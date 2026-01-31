package com.edricchan.studybuddy.ui.modules.debug.compose.device_info

import android.Manifest
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Colorize
import com.edricchan.studybuddy.ui.preference.compose.Preference
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategory
import com.edricchan.studybuddy.ui.preference.compose.PreferenceCategoryScope
import com.edricchan.studybuddy.ui.theming.common.dynamic.isDynamicColorAvailable
import com.edricchan.studybuddy.ui.theming.common.dynamic.prefDynamicTheme
import com.edricchan.studybuddy.core.resources.R as CoreResR

private val String.orUnset get() = ifEmpty { "<Unset>" }
private val Any?.orUnavailable get() = this?.toString() ?: "<Not available>"

@ChecksSdkIntAtLeast(parameter = 0, lambda = 1)
private inline fun fromApi(minVersion: Int, action: () -> Any) =
    if (Build.VERSION.SDK_INT >= minVersion) action().toString() else "<Not available>"

@Composable
private fun DeviceSdkInfoDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCopyClick: (String) -> Unit
) {
    val infoText = remember {
        """
            Device SDK: ${Build.VERSION.SDK_INT} (${
            fromApi(Build.VERSION_CODES.R) { Build.VERSION.RELEASE_OR_CODENAME }
        })
            Preview SDK: ${
            fromApi(Build.VERSION_CODES.M) {
                Build.VERSION.PREVIEW_SDK_INT
            }
        }
            Build fingerprint: ${Build.FINGERPRINT.orUnset}
            Model: ${Build.MODEL.orUnset}
            Board: ${Build.BOARD.orUnset}
            Bootloader version: ${Build.BOOTLOADER.orUnset}
            Brand: ${Build.BRAND.orUnset}
            Device: ${Build.DEVICE.orUnset}
            Display: ${Build.DISPLAY.orUnset}
            Fingerprint: ${Build.FINGERPRINT.orUnset}
            Hardware: ${Build.HARDWARE.orUnset}
            Host: ${Build.HOST.orUnset}
            Id: ${Build.ID.orUnset}
            Manufacturer: ${Build.MANUFACTURER.orUnset}
            Product: ${Build.PRODUCT.orUnset}
            Supported ABIs: ${Build.SUPPORTED_ABIS.joinToString()}
            Tags: ${Build.TAGS.orUnset}
            Time: ${Build.TIME}
            Type: ${Build.TYPE.orUnset}
            User: ${Build.USER}
            Radio firmware version: ${Build.getRadioVersion().orUnset}
            """.trimIndent()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.debug_activity_device_sdk_info_dialog_title))
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                SelectionContainer {
                    Text(text = infoText, fontFamily = FontFamily.Monospace)
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
private fun PreferenceCategoryScope.DeviceSdkInfoPreference(modifier: Modifier = Modifier) {
    var isDeviceInfoShown by rememberSaveable { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    Preference(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.debug_activity_device_sdk_info_dialog_title)) },
        icon = {
            Icon(
                painterResource(R.drawable.ic_perm_device_information_outline_24dp),
                contentDescription = null
            )
        },
        onClick = { isDeviceInfoShown = true }
    )

    if (isDeviceInfoShown) {
        DeviceSdkInfoDialog(
            onDismissRequest = { isDeviceInfoShown = false },
            onCopyClick = {
                clipboardManager.setText(AnnotatedString(it))
            }
        )
    }
}

@Composable
private fun DeviceNetworkInfoDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCopyClick: (String) -> Unit
) {
    val context = LocalContext.current

    val connectivityManager = remember(context) { context.getSystemService<ConnectivityManager>() }
    val isNetworkMetered =
        remember(connectivityManager) { connectivityManager?.isActiveNetworkMetered }
    val isNetworkActive =
        remember(connectivityManager) { connectivityManager?.isDefaultNetworkActive }
    val isNetworkPermGranted = remember(context) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_NETWORK_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    val infoText = remember {
        """
        Is network metered: ${isNetworkMetered.orUnavailable}
        Is network active: ${isNetworkActive.orUnavailable}
        Is network permission granted: $isNetworkPermGranted
        """.trimIndent()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.debug_activity_connectivity_info_dialog_title))
        },
        text = {
            SelectionContainer {
                Text(text = infoText)
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
private fun PreferenceCategoryScope.DeviceNetworkInfoPreference(modifier: Modifier = Modifier) {
    var isNetworkInfoShown by rememberSaveable { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    Preference(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.debug_activity_connectivity_info_dialog_title)) },
        icon = {
            Icon(
                painterResource(R.drawable.ic_network_cell_24dp),
                contentDescription = null
            )
        },
        onClick = { isNetworkInfoShown = true }
    )

    if (isNetworkInfoShown) {
        DeviceNetworkInfoDialog(
            onDismissRequest = { isNetworkInfoShown = false },
            onCopyClick = {
                clipboardManager.setText(AnnotatedString(it))
            }
        )
    }
}

@Composable
private fun DeviceDynamicThemeInfoDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onCopyClick: (String) -> Unit
) {
    val context = LocalContext.current

    val infoText = remember {
        """
            Is dynamic colour supported? $isDynamicColorAvailable
            Is dynamic colour preference checked? ${context.prefDynamicTheme}
        """.trimIndent()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.debug_activity_device_sdk_info_dialog_title))
        },
        text = {
            SelectionContainer {
                Text(text = infoText)
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
private fun PreferenceCategoryScope.DeviceDynamicThemePreference(modifier: Modifier = Modifier) {
    var isInfoShown by rememberSaveable { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    Preference(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.debug_activity_dynamic_theme_info_dialog_title)) },
        icon = {
            Icon(
                AppIcons.Outlined.Colorize,
                contentDescription = null
            )
        },
        onClick = { isInfoShown = true }
    )

    if (isInfoShown) {
        DeviceDynamicThemeInfoDialog(
            onDismissRequest = { isInfoShown = false },
            onCopyClick = {
                clipboardManager.setText(AnnotatedString(it))
            }
        )
    }
}

@Composable
fun DeviceInfoCategory(modifier: Modifier = Modifier) {
    PreferenceCategory(
        modifier = modifier,
        title = { Text(text = stringResource(R.string.debug_activity_category_device)) }
    ) {
        DeviceSdkInfoPreference()
        DeviceNetworkInfoPreference()
        DeviceDynamicThemePreference()
    }
}
