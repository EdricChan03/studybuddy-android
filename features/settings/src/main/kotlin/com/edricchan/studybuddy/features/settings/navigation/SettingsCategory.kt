package com.edricchan.studybuddy.features.settings.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemShapes
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.filled.Settings
import com.edricchan.studybuddy.core.resources.icons.filled.Task
import com.edricchan.studybuddy.core.resources.icons.outlined.Settings
import com.edricchan.studybuddy.core.resources.icons.outlined.SystemUpdateAlt
import com.edricchan.studybuddy.core.resources.icons.outlined.Task
import com.edricchan.studybuddy.exts.androidx.compose.runtime.letComposable
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
                imageVector = if (it) AppIcons.Filled.Settings else AppIcons.Outlined.Settings,
                contentDescription = null
            )
        }
    ),

    Task(
        nameResId = TaskR.string.pref_header_task_title,
        descResId = TaskR.string.pref_header_task_summary,
        icon = {
            Icon(
                imageVector = if (it) AppIcons.Filled.Task else AppIcons.Outlined.Task,
                contentDescription = null
            )
        }
    ),

    Updates(
        nameResId = UpdateR.string.pref_header_updates_title,
        descResId = UpdateR.string.pref_header_updates_summary,
        icon = {
            Icon(
                AppIcons.Outlined.SystemUpdateAlt,
                contentDescription = null
            )
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsCategory.SettingsListItem(
    modifier: Modifier = Modifier,
    shapes: ListItemShapes,
    selected: Boolean = false,
    onClick: () -> Unit,
) {
    val transition = updateTransition(selected, label = "Settings list item selection state")
    val fontWeight by transition.animateInt(label = "Settings list item font weight") {
        if (it) 600 else 400
    }

    SegmentedListItem(
        modifier = modifier,
        shapes = shapes,
        selected = selected,
        onClick = onClick,
        content = {
            Text(
                text = stringResource(nameResId),
                fontWeight = FontWeight(fontWeight)
            )
        },
        supportingContent = descResId?.letComposable { Text(text = stringResource(it)) },
        leadingContent = { icon(selected) }
    )
}
