package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

@Composable
private fun switchBarSurfaceColor(
    enabled: Boolean, checked: Boolean
): Color = if (enabled) {
    if (checked) MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.surfaceContainer
} else {
    if (checked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
    else MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.6f)
}

@Composable
private fun switchBarSurfaceContentColor(
    enabled: Boolean, checked: Boolean
): Color = if (enabled) {
    if (checked) MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.onSurface
} else {
    if (checked) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
}

/**
 * [Master setting](https://source.android.com/docs/core/settings/settings-guidelines#master_setting)
 * switch, used to indicate that an entire feature can be turned on/off.
 * @param text Text [Composable] to be displayed at the start.
 * [MainSwitchBarDefaults.TitleText] can be used which provides default title styling.
 * @param checked Whether the setting is checked.
 * @param onCheckedChange Invoked when the setting is clicked on, with the new `checked`
 * state passed as a parameter.
 * @param switch [Composable] used to indicate the bar's [checked] status.
 * [MainSwitchBarDefaults.Switch] can be used which provides a default [Switch] implementation.
 * @param enabled Whether the switch bar is enabled.
 */
@Composable
fun MainSwitchBar(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    switch: @Composable RowScope.() -> Unit = {
        MainSwitchBarDefaults.Switch(checked = checked, enabled = enabled)
    },
    text: @Composable RowScope.() -> Unit
) {
    val surfaceColor by animateColorAsState(
        switchBarSurfaceColor(enabled = enabled, checked = checked),
        label = "Surface colour"
    )
    val surfaceContentColor by animateColorAsState(
        switchBarSurfaceContentColor(enabled = enabled, checked = checked),
        label = "Surface content colour"
    )

    Surface(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        color = surfaceColor,
        contentColor = surfaceContentColor,
        shape = RoundedCornerShape(28.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            text()
            switch()
        }
    }
}

private data class MainSwitchBarPreviewData(
    val enabled: Boolean,
    val checked: Boolean
)

private class MainSwitchBarParameterProvider : PreviewParameterProvider<MainSwitchBarPreviewData> {
    override val values = sequenceOf(
        MainSwitchBarPreviewData(enabled = false, checked = false),
        MainSwitchBarPreviewData(enabled = true, checked = false),
        MainSwitchBarPreviewData(enabled = false, checked = true),
        MainSwitchBarPreviewData(enabled = true, checked = true)
    )
}

@Preview
@PreviewLightDark
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun MainSwitchBarInteractivePreview(
    @PreviewParameter(MainSwitchBarParameterProvider::class) data: MainSwitchBarPreviewData
) {
    var checked by remember { mutableStateOf(data.checked) }
    StudyBuddyTheme {
        MainSwitchBar(
            text = { Text(text = "Example switch") },
            checked = checked,
            onCheckedChange = { checked = it },
            enabled = data.enabled
        )
    }
}

/** Defaults for [MainSwitchBar]. */
@Stable
object MainSwitchBarDefaults {
    /**
     * The title composable for use with [MainSwitchBar].
     * @param text The title text to display.
     */
    @Composable
    fun TitleText(
        modifier: Modifier = Modifier,
        text: String
    ) = Text(
        modifier = modifier.padding(end = 24.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 20.sp
    )

    /**
     * The switch composable for use with [MainSwitchBar].
     * @param checked Whether the [androidx.compose.material3.Switch] is checked.
     * @param enabled Whether the [androidx.compose.material3.Switch] is enabled.
     */
    @Composable
    fun Switch(
        modifier: Modifier = Modifier,
        checked: Boolean,
        enabled: Boolean = true
    ) {
        // Icon isn't focusable, no need for content description
        val icon: (@Composable () -> Unit)? = checked.takeIf { it }?.let {
            {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        }

        Switch(
            modifier = modifier,
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            thumbContent = icon
        )
    }
}
