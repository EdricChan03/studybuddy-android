package com.edricchan.studybuddy.ui.preference.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
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
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Check
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme

/**
 * [Master setting](https://source.android.com/docs/core/settings/settings-guidelines#master_setting)
 * switch, used to indicate that an entire feature can be turned on/off.
 * @param text Text [Composable] to be displayed at the start.
 * [MainSwitchBarDefaults.TitleText] can be used which provides default title styling.
 * @param includePadding Whether to include padding on the content.
 * @param checked Whether the setting is checked.
 * @param onCheckedChange Invoked when the setting is clicked on, with the new `checked`
 * state passed as a parameter.
 * @param switch [Composable] used to indicate the bar's [checked] status.
 * [MainSwitchBarDefaults.Switch] can be used which provides a default [Switch] implementation.
 * @param enabled Whether the switch bar is enabled.
 * @param colors Colour configuration for the switch-bar - see [MainSwitchBarColors] for additional
 * information.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainSwitchBar(
    modifier: Modifier = Modifier,
    includePadding: Boolean = false,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    colors: MainSwitchBarColors = MainSwitchBarDefaults.colors(),
    switch: @Composable RowScope.() -> Unit = {
        MainSwitchBarDefaults.Switch(checked = checked, enabled = enabled)
    },
    text: @Composable RowScope.() -> Unit
) {
    val surfaceColor by animateColorAsState(
        colors.containerColor(enabled = enabled, checked = checked),
        label = "Surface colour"
    )
    val surfaceContentColor by animateColorAsState(
        colors.contentColor(enabled = enabled, checked = checked),
        label = "Surface content colour"
    )

    val paddingModifier = if (includePadding) Modifier.padding(16.dp) else Modifier

    Surface(
        modifier = modifier
            .then(paddingModifier)
            .fillMaxWidth(),
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        color = surfaceColor,
        contentColor = surfaceContentColor,
        shape = MaterialTheme.shapes.extraLarge
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

@Immutable
data class MainSwitchBarColors(
    val containerColorList: ColorStateList,
    val contentColorList: ColorStateList
) {
    @Immutable
    data class ColorStateList(
        val baseColor: Color,
        val checkedColor: Color,
        val disabledColor: Color,
        val disabledCheckedColor: Color
    ) {
        fun color(enabled: Boolean, checked: Boolean): Color = when {
            enabled && checked -> checkedColor
            !enabled && checked -> disabledCheckedColor
            !enabled && !checked -> disabledColor
            else -> baseColor
        }
    }

    fun containerColor(enabled: Boolean, checked: Boolean): Color = containerColorList.color(
        enabled = enabled, checked = checked
    )

    fun contentColor(enabled: Boolean, checked: Boolean): Color = contentColorList.color(
        enabled = enabled, checked = checked
    )
}

private data class MainSwitchBarPreviewData(
    val enabled: Boolean,
    val checked: Boolean
)

private class MainSwitchBarParameterProvider : PreviewParameterProvider<MainSwitchBarPreviewData> {
    val previews = listOf(
        MainSwitchBarPreviewData(enabled = false, checked = false),
        MainSwitchBarPreviewData(enabled = true, checked = false),
        MainSwitchBarPreviewData(enabled = false, checked = true),
        MainSwitchBarPreviewData(enabled = true, checked = true)
    )
    override val values = previews.asSequence()

    override fun getDisplayName(index: Int): String =
        previews[index].let { "enabled = ${it.enabled}, checked = ${it.checked}" }
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

private const val DisabledAlpha = 0.38f

private val Color.disabled: Color get() = copy(alpha = DisabledAlpha)

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
                    imageVector = AppIcons.Outlined.Check,
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

    /**
     * Creates a [MainSwitchBarColors] object with the given colours.
     * @param containerColor Container colour when the switch-bar is enabled but not checked.
     * @param contentColor Content colour when the switch-bar is enabled but not checked.
     * @param containerCheckedColor Container colour when the switch-bar is enabled and checked.
     * @param contentCheckedColor Content colour when the switch-bar is enabled and checked.
     * @param disabledContainerColor Container colour when the switch-bar is disabled and unchecked.
     * @param disabledContentColor Content colour when the switch-bar is disabled and unchecked.
     * @param disabledContainerCheckedColor Container colour when the switch-bar is disabled but
     * checked.
     * @param disabledContentCheckedColor Content colour when the switch-bar is disabled but
     * checked.
     */
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor: Color = contentColorFor(containerColor),
        containerCheckedColor: Color = MaterialTheme.colorScheme.primaryContainer,
        contentCheckedColor: Color = contentColorFor(containerCheckedColor),
        disabledContainerColor: Color = containerColor.disabled,
        disabledContentColor: Color = contentColor.disabled,
        disabledContainerCheckedColor: Color = containerCheckedColor.disabled,
        disabledContentCheckedColor: Color = contentCheckedColor.disabled
    ): MainSwitchBarColors = colors(
        containerColorList = MainSwitchBarColors.ColorStateList(
            baseColor = containerColor,
            checkedColor = containerCheckedColor,
            disabledColor = disabledContainerColor,
            disabledCheckedColor = disabledContainerCheckedColor
        ),
        contentColorList = MainSwitchBarColors.ColorStateList(
            baseColor = contentColor,
            checkedColor = contentCheckedColor,
            disabledColor = disabledContentColor,
            disabledCheckedColor = disabledContentCheckedColor
        )
    )

    fun colors(
        containerColorList: MainSwitchBarColors.ColorStateList,
        contentColorList: MainSwitchBarColors.ColorStateList
    ): MainSwitchBarColors = MainSwitchBarColors(
        containerColorList = containerColorList,
        contentColorList = contentColorList
    )
}
