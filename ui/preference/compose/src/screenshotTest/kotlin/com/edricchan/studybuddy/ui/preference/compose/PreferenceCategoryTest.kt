package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.android.tools.screenshot.PreviewTest
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

private class CheckedParameterProvider :
    CollectionPreviewParameterProvider<Boolean>(listOf(false, true)) {
    override fun getDisplayName(index: Int): String? = when {
        index > 1 -> null
        else -> "is checked = ${index == 1}"
    }
}

@Preview
@PreviewTest
@Composable
fun KitchenSinkPreferenceCategory(@PreviewParameter(CheckedParameterProvider::class) initialChecked: Boolean) {
    var checked by rememberSaveable(initialChecked) { mutableStateOf(initialChecked) }

    StudyBuddyTheme {
        PreferenceCategory {
            // Base
            Preference(
                title = { Text(text = "Title text") },
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) }
            )
            // With subtitle
            Preference(
                title = { Text(text = "Title text") },
                subtitle = { Text(text = "Subtitle text") },
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) }
            )
            // With action (switch)
            Preference(
                title = { Text(text = "Title text") },
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
                showActionDivider = true,
                action = {
                    Switch(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
            )
            // With action (checkbox)
            Preference(
                title = { Text(text = "Title text") },
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
                showActionDivider = true,
                action = {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
            )
            // With subtitle + action (switch)
            Preference(
                title = { Text(text = "Title text") },
                subtitle = { Text(text = "Subtitle text") },
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
                showActionDivider = true,
                action = {
                    Switch(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
            )
            // With subtitle + action (checkbox)
            Preference(
                title = { Text(text = "Title text") },
                subtitle = { Text(text = "Subtitle text") },
                icon = { Icon(Icons.Outlined.Build, contentDescription = null) },
                showActionDivider = true,
                action = {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                    )
                }
            )
        }
    }
}
