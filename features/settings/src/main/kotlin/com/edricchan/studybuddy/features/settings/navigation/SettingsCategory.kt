package com.edricchan.studybuddy.features.settings.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
import com.edricchan.studybuddy.features.settings.R
import com.edricchan.studybuddy.core.settings.appearance.resources.R as AppearanceR
import com.edricchan.studybuddy.core.settings.tasks.resources.R as TaskR
import com.edricchan.studybuddy.core.settings.updates.resources.R as UpdateR

enum class SettingsCategory(
    @field:StringRes val nameResId: Int,
    @field:StringRes val descResId: Int? = null,
    val icon: @Composable (isSelected: Boolean) -> Unit
) {
    General(
        nameResId = AppearanceR.string.pref_header_appearance_title,
        descResId = AppearanceR.string.pref_header_appearance_summary,
        icon = {
            Icon(
                imageVector = if (it) Icons.Filled.Settings else Icons.Outlined.Settings,
                contentDescription = null
            )
        }
    ),

    Task(
        nameResId = TaskR.string.pref_header_task_title,
        descResId = TaskR.string.pref_header_task_summary,
        icon = {
            Icon(
                imageVector = if (it) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                contentDescription = null
            )
        }
    ),

    Updates(
        nameResId = UpdateR.string.pref_header_updates_title,
        descResId = UpdateR.string.pref_header_updates_summary,
        icon = {
            Icon(
                painterResource(R.drawable.ic_system_update_24dp),
                contentDescription = null
            )
        }
    )
}

@Composable
fun SettingsCategory.SettingsListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    selected: Boolean = false
) {
    val transition = updateTransition(selected, label = "Settings list item selection state")
    val containerColor by transition.animateColor(label = "Settings list item container colour") {
        if (it) MaterialTheme.colorScheme.secondaryContainer else ListItemDefaults.containerColor
    }
    val contentColor by transition.animateColor(label = "Settings list item content colour") {
        if (it) MaterialTheme.colorScheme.onSecondaryContainer else ListItemDefaults.contentColor
    }
    val fontWeight by transition.animateInt(label = "Settings list item font weight") {
        if (it) 600 else 400
    }

    ListItem(
        modifier = modifier
            .clip(CardDefaults.outlinedShape)
            .selectable(selected = selected, onClick = onClick),
        headlineContent = {
            Text(
                text = stringResource(nameResId),
                fontWeight = FontWeight(fontWeight)
            )
        },
        supportingContent = descResId?.letComposable { Text(text = stringResource(it)) },
        leadingContent = { icon(selected) },
        colors = ListItemDefaults.colors(
            containerColor = containerColor,
            headlineColor = contentColor,
            leadingIconColor = contentColor,
            trailingIconColor = contentColor,
            overlineColor = contentColor,
            supportingColor = contentColor
        )
    )
}
