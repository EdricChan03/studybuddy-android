package com.edricchan.studybuddy.ui.widgets.compose.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

@Composable
private fun OutlinedTextFieldBoxContent(
    modifier: Modifier,
    headlineText: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
) = ListItem(
    modifier = modifier,
    headlineContent = headlineText,
    supportingContent = supportingText,
    leadingContent = leadingIcon,
    trailingContent = trailingIcon
)

/**
 * A composable which aims to appear like a [androidx.compose.material3.OutlinedTextField],
 * but does not allow for editing.
 * @param shape The border [Shape] to be applied.
 * @param border The [BorderStroke] to be applied.
 * @param headlineText Composable to be shown for the text.
 * @param supportingText Composable to be shown for the supporting text.
 * @param leadingIcon Icon to be shown on the left side.
 * @param trailingIcon Icon to be shown on the right side.
 */
@Composable
fun OutlinedTextFieldBox(
    modifier: Modifier = Modifier,
    shape: Shape = OutlinedTextFieldBoxDefaults.shape,
    border: BorderStroke = OutlinedTextFieldBoxDefaults.border,
    headlineText: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) = OutlinedTextFieldBoxContent(
    modifier = modifier.border(border, shape),
    headlineText = headlineText,
    supportingText = supportingText,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon
)

/**
 * A composable which aims to appear like a [androidx.compose.material3.OutlinedTextField],
 * but does not allow for editing.
 * @param shape The border [Shape] to be applied.
 * @param border The [BorderStroke] to be applied.
 * @param onClick Lambda that is invoked when the box is clicked on.
 * @param enabled Whether click events should be enabled.
 * @param headlineText Composable to be shown for the text.
 * @param supportingText Composable to be shown for the supporting text.
 * @param leadingIcon Icon to be shown on the left side.
 * @param trailingIcon Icon to be shown on the right side.
 */
@Composable
fun OutlinedTextFieldBox(
    modifier: Modifier = Modifier,
    shape: Shape = OutlinedTextFieldBoxDefaults.shape,
    border: BorderStroke = OutlinedTextFieldBoxDefaults.border,
    onClick: () -> Unit,
    enabled: Boolean = true,
    headlineText: @Composable () -> Unit,
    supportingText: @Composable (() -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) = OutlinedTextFieldBoxContent(
    modifier = modifier
        .border(border, shape)
        .clickable(enabled = enabled, onClick = onClick),
    headlineText = headlineText,
    supportingText = supportingText,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon
)

@Preview
@PreviewDynamicColors
@PreviewLightDark
@PreviewFontScale
@Composable
private fun OutlinedTextFieldBoxPreview() {
    StudyBuddyTheme {
        OutlinedTextFieldBox(
            headlineText = { Text(text = "Example content goes here") },
            leadingIcon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
            trailingIcon = { Icon(Icons.Outlined.Favorite, contentDescription = null) }
        )
    }
}

/** Defaults to be applied to [OutlinedTextFieldBox] if not specified. */
@Stable
object OutlinedTextFieldBoxDefaults {
    /** The default outlined [Shape] to use. */
    val shape
        @Composable get() = OutlinedTextFieldDefaults.shape

    /** The default outlined [BorderStroke] to use. */
    val border
        @Composable get() = BorderStroke(
            OutlinedTextFieldDefaults.UnfocusedBorderThickness,
            MaterialTheme.colorScheme.outline
        )
}
