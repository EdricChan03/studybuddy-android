package com.edricchan.studybuddy.features.tasks.components.form.tags

import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Label
import com.edricchan.studybuddy.features.tasks.components.form.R
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.OutlinedChipTextField
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.data.ChipItem
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.data.ChipTextFieldState
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.data.chipItemFactory
import com.edricchan.studybuddy.ui.widgets.compose.form.chips.data.rememberChipTextFieldState
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskTagItem(
    override val text: String
) : ChipItem {
    companion object {
        val Factory: ChipItem.Factory<TaskTagItem> = chipItemFactory()
    }
}

@Composable
fun TaskTagsTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tagsState: ChipTextFieldState<TaskTagItem>
) {
    OutlinedChipTextField(
        modifier = modifier,
        enabled = enabled,
        state = tagsState,
        leadingIcon = {
            Icon(
                AppIcons.Outlined.Label,
                contentDescription = null
            )
        },
        label = { Text(text = stringResource(R.string.task_tags_text_field_label)) },
        labelPosition = TextFieldLabelPosition.Inside(isAlwaysMinimized = true),
        shape = OutlinedTextFieldDefaults.roundedShape,
        colors = OutlinedTextFieldDefaults.tonalColors()
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun TaskTagsTextFieldPreview() {
    val state = rememberChipTextFieldState(chipFactory = TaskTagItem.Factory)

    TaskTagsTextField(
        tagsState = state
    )
}
