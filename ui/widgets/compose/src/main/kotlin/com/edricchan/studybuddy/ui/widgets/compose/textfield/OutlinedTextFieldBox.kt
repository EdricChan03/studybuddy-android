package com.edricchan.studybuddy.ui.widgets.compose.textfield

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.CheckCircle
import com.edricchan.studybuddy.core.resources.icons.outlined.Edit
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider

/**
 * Represents the colours of a [OutlinedTextFieldBox] in different states.
 * @property borderColor The default border colour.
 * @property containerColor The default container colour.
 * @property contentColor The default content colour.
 * @property leadingContentColor The default leading content colour.
 * @property trailingContentColor The default trailing content colour.
 * @property overlineContentColor The default overline content colour.
 * @property supportingContentColor The default supporting content colour.
 * @property disabledBorderColor The [border colour][borderColor] when disabled.
 * @property disabledContainerColor The [container colour][containerColor] when disabled.
 * @property disabledContentColor The [content colour][contentColor] when disabled.
 * @property disabledLeadingContentColor The [leading content colour][leadingContentColor]
 * when disabled.
 * @property disabledTrailingContentColor The [trailing content colour][trailingContentColor]
 * when disabled.
 * @property disabledOverlineContentColor The [overline content colour][overlineContentColor]
 * when disabled.
 * @property disabledSupportingContentColor The [supporting content colour][supportingContentColor]
 * when disabled.
 */
data class OutlinedTextFieldBoxColors(
    val borderColor: Color,
    val containerColor: Color,
    val contentColor: Color,
    val leadingContentColor: Color,
    val trailingContentColor: Color,
    val overlineContentColor: Color,
    val supportingContentColor: Color,
    val disabledBorderColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledLeadingContentColor: Color,
    val disabledTrailingContentColor: Color,
    val disabledOverlineContentColor: Color,
    val disabledSupportingContentColor: Color,
) {
    companion object {
        @Composable
        internal fun fromListItemColors(
            listItemColors: ListItemColors,
            borderColor: Color = MaterialTheme.colorScheme.outline,
            disabledBorderColor: Color = borderColor.disabled()
        ): OutlinedTextFieldBoxColors = OutlinedTextFieldBoxColors(
            borderColor = borderColor,
            containerColor = listItemColors.containerColor,
            contentColor = listItemColors.contentColor,
            leadingContentColor = listItemColors.leadingContentColor,
            trailingContentColor = listItemColors.trailingContentColor,
            overlineContentColor = listItemColors.overlineContentColor,
            supportingContentColor = listItemColors.supportingContentColor,
            disabledBorderColor = disabledBorderColor,
            disabledContainerColor = listItemColors.disabledContainerColor,
            disabledContentColor = listItemColors.disabledContentColor,
            disabledLeadingContentColor = listItemColors.disabledLeadingContentColor,
            disabledTrailingContentColor = listItemColors.disabledTrailingContentColor,
            disabledOverlineContentColor = listItemColors.disabledOverlineContentColor,
            disabledSupportingContentColor = listItemColors.disabledSupportingContentColor,
        )
    }

    @Composable
    internal fun asListItemColors(): ListItemColors = ListItemDefaults.colors(
        containerColor = containerColor,
        contentColor = contentColor,
        leadingContentColor = leadingContentColor,
        trailingContentColor = trailingContentColor,
        overlineContentColor = overlineContentColor,
        supportingContentColor = supportingContentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        disabledLeadingContentColor = disabledLeadingContentColor,
        disabledTrailingContentColor = disabledTrailingContentColor,
        disabledOverlineContentColor = disabledOverlineContentColor,
        disabledSupportingContentColor = disabledSupportingContentColor
    )
}

@Composable
private fun OutlinedTextFieldBoxContent(
    modifier: Modifier,
    colors: OutlinedTextFieldBoxColors,
    headlineText: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
) = ListItem(
    modifier = modifier,
    content = headlineText,
    supportingContent = supportingText,
    leadingContent = leadingIcon,
    trailingContent = trailingIcon,
    colors = colors.asListItemColors()
)

@Composable
private fun OutlinedTextFieldBoxContent(
    modifier: Modifier,
    enabled: Boolean = true,
    colors: OutlinedTextFieldBoxColors,
    onClick: () -> Unit,
    headlineText: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
) = ListItem(
    modifier = modifier,
    enabled = enabled,
    onClick = onClick,
    content = headlineText,
    supportingContent = supportingText,
    leadingContent = leadingIcon,
    trailingContent = trailingIcon,
    colors = colors.asListItemColors()
)

/**
 * A composable which aims to appear like a [androidx.compose.material3.OutlinedTextField],
 * but does not allow for editing.
 * @param shape The border [Shape] to be applied.
 * @param border The [BorderStroke] to be applied.
 * @param colors The [OutlinedTextFieldBoxColors] to be applied.
 * @param headlineText Composable to be shown for the text.
 * @param supportingText Composable to be shown for the supporting text.
 * @param leadingIcon Icon to be shown on the left side.
 * @param trailingIcon Icon to be shown on the right side.
 */
@Composable
fun OutlinedTextFieldBox(
    modifier: Modifier = Modifier,
    shape: Shape = OutlinedTextFieldBoxDefaults.shape,
    border: BorderStroke = OutlinedTextFieldBoxDefaults.border(),
    colors: OutlinedTextFieldBoxColors = OutlinedTextFieldBoxDefaults.colors(),
    headlineText: @Composable () -> Unit,
    supportingText: (@Composable () -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) = OutlinedTextFieldBoxContent(
    modifier = modifier.border(border, shape),
    colors = colors,
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
 * @param colors The [OutlinedTextFieldBoxColors] to be applied.
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
    border: BorderStroke = OutlinedTextFieldBoxDefaults.border(),
    colors: OutlinedTextFieldBoxColors = OutlinedTextFieldBoxDefaults.colors(),
    onClick: () -> Unit,
    enabled: Boolean = true,
    headlineText: @Composable () -> Unit,
    supportingText: @Composable (() -> Unit)? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) = OutlinedTextFieldBoxContent(
    modifier = modifier.border(border, shape),
    enabled = enabled,
    colors = colors,
    onClick = onClick,
    headlineText = headlineText,
    supportingText = supportingText,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon
)

@Preview
@PreviewDynamicColors
@PreviewLightDark
@PreviewFontScale
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun OutlinedTextFieldBoxPreview() {
    OutlinedTextFieldBox(
        headlineText = { Text(text = "Example content goes here") },
        leadingIcon = { Icon(AppIcons.Outlined.CheckCircle, contentDescription = null) },
        trailingIcon = { Icon(AppIcons.Outlined.Edit, contentDescription = null) }
    )
}

private const val DisabledAlpha = 0.38f

private fun Color.disabled(): Color = copy(alpha = DisabledAlpha)

/** Defaults to be applied to [OutlinedTextFieldBox] if not specified. */
@Stable
object OutlinedTextFieldBoxDefaults {
    /** The default outlined [Shape] to use. */
    val shape
        @Composable get() = OutlinedTextFieldDefaults.shape

    /** Expressive variant of [shape] to use. */
    val roundedShape
        @Composable get() = OutlinedTextFieldDefaults.roundedShape

    /** The default outlined [BorderStroke] to use. */
    @Composable
    fun border(color: Color = MaterialTheme.colorScheme.outline) = BorderStroke(
        OutlinedTextFieldDefaults.UnfocusedBorderThickness,
        color
    )

    /** The default tonal variant [BorderStroke] to use. */
    @Composable
    fun tonalBorder(color: Color = MaterialTheme.colorScheme.outlineVariant) = BorderStroke(
        OutlinedTextFieldDefaults.UnfocusedBorderThickness,
        color
    )

    /** The default [OutlinedTextFieldBoxColors] to use. */
    @Composable
    fun colors(): OutlinedTextFieldBoxColors =
        OutlinedTextFieldBoxColors.fromListItemColors(ListItemDefaults.colors())

    /**
     * Variant of [colors] which returns the tonal variant.
     * @see OutlinedTextFieldDefaults.tonalColors
     */
    @Composable
    fun tonalColors(): OutlinedTextFieldBoxColors {
        val borderColor = MaterialTheme.colorScheme.outlineVariant
        val containerColor = MaterialTheme.colorScheme.onPrimary
        val contentColor = MaterialTheme.colorScheme.onSurface
        val leadingContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val trailingContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val overlineContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        val supportingContentColor = MaterialTheme.colorScheme.onBackground

        return OutlinedTextFieldBoxColors(
            borderColor = borderColor,
            containerColor = containerColor,
            contentColor = contentColor,
            leadingContentColor = leadingContentColor,
            trailingContentColor = trailingContentColor,
            overlineContentColor = overlineContentColor,
            supportingContentColor = supportingContentColor,
            // disabled
            disabledBorderColor = borderColor,
            disabledContainerColor = containerColor.disabled(),
            disabledContentColor = contentColor.disabled(),
            disabledLeadingContentColor = leadingContentColor.disabled(),
            disabledTrailingContentColor = trailingContentColor.disabled(),
            disabledOverlineContentColor = overlineContentColor.disabled(),
            disabledSupportingContentColor = supportingContentColor.disabled()
        )
    }

}
