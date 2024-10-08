package com.edricchan.studybuddy.ui.widgets.compose.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun RadioButtonRow(
    modifier: Modifier = Modifier,
    radioButtonModifier: Modifier = Modifier,
    selected: Boolean,
    onSelected: () -> Unit,
    enabled: Boolean = true,
    text: @Composable () -> Unit
) = Row(
    modifier = modifier.selectable(
        selected = selected, enabled = enabled, onClick = onSelected,
        role = Role.RadioButton
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    RadioButton(
        modifier = radioButtonModifier,
        selected = selected, onClick = null
    )

    text()
}

@Composable
fun RadioButtonListItem(
    modifier: Modifier = Modifier,
    radioButtonModifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    selected: Boolean,
    onSelected: () -> Unit,
    enabled: Boolean = true,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    text: @Composable () -> Unit
) = ListItem(
    modifier = modifier.selectable(
        selected = selected, enabled = enabled, onClick = onSelected,
        role = Role.RadioButton
    ),
    colors = colors.withDisabledColors(enabled),
    headlineContent = text,
    supportingContent = supportingContent,
    overlineContent = overlineContent,
    leadingContent = {
        RadioButton(
            modifier = radioButtonModifier,
            selected = selected,
            enabled = enabled,
            onClick = null
        )
    },
    trailingContent = trailingContent
)

@Composable
fun TrailingRadioButtonListItem(
    modifier: Modifier = Modifier,
    radioButtonModifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    selected: Boolean,
    onSelected: () -> Unit,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    text: @Composable () -> Unit
) = ListItem(
    modifier = modifier.selectable(
        selected = selected, enabled = enabled, onClick = onSelected,
        role = Role.RadioButton
    ),
    colors = colors.withDisabledColors(enabled),
    headlineContent = text,
    supportingContent = supportingContent,
    overlineContent = overlineContent,
    trailingContent = {
        RadioButton(
            modifier = radioButtonModifier,
            selected = selected,
            enabled = enabled,
            onClick = null
        )
    },
    leadingContent = leadingContent
)

@Composable
fun CheckboxRow(
    modifier: Modifier = Modifier,
    checkboxModifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    text: @Composable () -> Unit
) = Row(
    modifier = modifier.toggleable(
        value = checked,
        onValueChange = onCheckedChange,
        role = Role.Checkbox
    ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    Checkbox(
        modifier = checkboxModifier,
        checked = checked,
        onCheckedChange = null,
        enabled = enabled
    )

    text()
}

@Composable
fun CheckboxListItem(
    modifier: Modifier = Modifier,
    checkboxModifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    trailingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    text: @Composable () -> Unit
) = ListItem(
    modifier = modifier.toggleable(
        value = checked,
        onValueChange = onCheckedChange,
        role = Role.Checkbox,
        enabled = enabled
    ),
    colors = colors.withDisabledColors(enabled),
    headlineContent = text,
    supportingContent = supportingContent,
    overlineContent = overlineContent,
    leadingContent = {
        Checkbox(
            modifier = checkboxModifier,
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
    },
    trailingContent = trailingContent
)

@Composable
fun TrailingCheckboxListItem(
    modifier: Modifier = Modifier,
    checkboxModifier: Modifier = Modifier,
    colors: ListItemColors = ListItemDefaults.colors(),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    overlineContent: @Composable (() -> Unit)? = null,
    text: @Composable () -> Unit
) = ListItem(
    modifier = modifier.toggleable(
        value = checked,
        onValueChange = onCheckedChange,
        role = Role.Checkbox,
        enabled = enabled
    ),
    colors = colors.withDisabledColors(enabled),
    headlineContent = text,
    supportingContent = supportingContent,
    overlineContent = overlineContent,
    trailingContent = {
        Checkbox(
            modifier = checkboxModifier,
            checked = checked,
            onCheckedChange = null,
            enabled = enabled
        )
    },
    leadingContent = leadingContent
)
