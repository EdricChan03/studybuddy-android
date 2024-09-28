package com.edricchan.studybuddy.features.tasks.ui.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskContentListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskCreatedAtOverline
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskDueDateListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskTitleListItem
import com.edricchan.studybuddy.ui.theming.compose.StudyBuddyTheme
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    titleTextModifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: TaskCardColors = TaskCardDefaults.colors(),
    borderColors: TaskCardBorderColors = TaskCardDefaults.borderColors(),
    elevation: CardElevation = TaskCardDefaults.cardElevation(),
    interactionSource: MutableInteractionSource? = null,
    inSelectionMode: Boolean = false,
    selected: Boolean = false,
    onSelectedChange: (Boolean) -> Unit = {},
    title: String? = null,
    content: String? = null,
    isDone: Boolean = false,
    dueDate: LocalDateTime? = null,
    createdAt: LocalDateTime? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    onMarkAsDoneClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val indication = LocalIndication.current
    val selectionModifier = if (inSelectionMode) Modifier
        .combinedClickable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            onClick = {},
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
        .toggleable(
            interactionSource = interactionSource,
            indication = indication,
            enabled = enabled,
            value = selected,
            onValueChange = onSelectedChange
        ) else Modifier.combinedClickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel
    )

    val transition = updateTransition(
        targetState = inSelectionMode && selected,
        label = "selected"
    )
    val containerColor by transition.animateColor(
        label = "card container colour"
    ) { isSelected ->
        colors.containerColor(enabled = enabled, selected = isSelected)
    }
    val contentColor by transition.animateColor(
        label = "card content colour"
    ) { isSelected ->
        colors.contentColor(enabled = enabled, selected = isSelected)
    }
    val borderColor by transition.animateColor(
        label = "card border colour"
    ) { isSelected ->
        borderColors.borderColor(enabled = enabled, selected = isSelected)
    }

    OutlinedCard(
        modifier = modifier then selectionModifier,
        enabled = enabled,
        onClick = onClick,
        colors = CardDefaults.outlinedCardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = colors.disabledContainerColor,
            disabledContentColor = colors.disabledContentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        elevation = elevation
    ) {
        // Use an inner Box such that the selected icon can stack on top of the
        // task information
        Box {
            // Task details
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                createdAt?.let { TaskCreatedAtOverline(createdAt = it.toInstant(ZoneOffset.UTC)) }
                TaskTitleListItem(
                    textModifier = titleTextModifier,
                    title = title ?: stringResource(R.string.task_adapter_empty_title),
                    isDone = isDone,
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
                TaskContentListItem(
                    content = content ?: stringResource(R.string.task_adapter_empty_content)
                )
                dueDate?.let {
                    TaskDueDateListItem(
                        dueDate = it,
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(onClick = onMarkAsDoneClick) {
                        Text(
                            text = stringResource(
                                if (isDone) R.string.task_adapter_mark_as_undone_btn_text
                                else R.string.task_adapter_mark_as_done_btn_text
                            )
                        )
                    }
                    TextButton(onClick = onDeleteClick) {
                        Text(text = stringResource(R.string.task_adapter_delete_task_btn_text))
                    }
                }
            }

            // Interesting "hack" (if you can call it that) for using the
            // ColumnScope.AnimatedVisibility variant - see
            // https://stackoverflow.com/q/67975569/ for more info
            // Selection icon
            this@OutlinedCard.AnimatedVisibility(
                visible = inSelectionMode,
                modifier = Modifier.align(Alignment.TopEnd),
                label = "selected icon animated visibility",
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Crossfade(targetState = selected, label = "selected icon crossfade") {
                    if (it) {
                        val bgColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                        Icon(
                            Icons.Outlined.CheckCircle,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(16.dp)
                                .border(2.dp, bgColor, CircleShape)
                                .clip(CircleShape)
                                .background(bgColor)
                        )
                    } else {
                        Icon(
                            painterResource(R.drawable.ic_radio_button_unchecked_outline_24dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TaskCardPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) = StudyBuddyTheme {
    TaskCard(
        title = "This is a task item",
        content = text,
        dueDate = LocalDateTime.now(),
        onClick = {},
        onMarkAsDoneClick = {},
        onDeleteClick = {}
    )
}

@Preview
@Composable
private fun DoneTaskCardPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) = StudyBuddyTheme {
    TaskCard(
        title = "This is a task item",
        content = text,
        dueDate = LocalDateTime.now(),
        isDone = true,
        onClick = {},
        onMarkAsDoneClick = {},
        onDeleteClick = {}
    )
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    colors: TaskCardColors = TaskCardDefaults.colors(),
    inSelectionMode: Boolean = false,
    selected: Boolean = false,
    onSelectedChange: (Boolean) -> Unit = {},
    task: TodoItem,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    onMarkAsDoneClick: () -> Unit,
    onDeleteClick: () -> Unit
) = TaskCard(
    modifier = modifier,
    colors = colors,
    inSelectionMode = inSelectionMode,
    selected = selected,
    onSelectedChange = onSelectedChange,
    title = task.title,
    content = task.content,
    dueDate = task.dueDate?.toLocalDateTime(),
    createdAt = task.createdAt?.toLocalDateTime(),
    isDone = task.done ?: false,
    onClick = onClick,
    onLongClick = onLongClick,
    onLongClickLabel = onLongClickLabel,
    onMarkAsDoneClick = onMarkAsDoneClick,
    onDeleteClick = onDeleteClick
)

@Immutable
data class TaskCardColors(
    val containerColor: Color,
    val contentColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
) {
    @Composable
    fun containerColor(enabled: Boolean, selected: Boolean): Color = if (enabled) {
        if (selected) selectedContainerColor else containerColor
    } else disabledContainerColor

    @Composable
    fun contentColor(enabled: Boolean, selected: Boolean): Color = if (enabled) {
        if (selected) selectedContentColor else contentColor
    } else disabledContentColor
}

@Immutable
data class TaskCardBorderColors(
    val borderColor: Color,
    val selectedBorderColor: Color,
    val disabledBorderColor: Color
) {
    @Composable
    fun borderColor(enabled: Boolean, selected: Boolean): Color = if (enabled) {
        if (selected) selectedBorderColor else borderColor
    } else disabledBorderColor
}

object TaskCardDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        contentColor: Color = contentColorFor(containerColor),
        disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = 0.38f)
            .compositeOver(
                MaterialTheme.colorScheme.surfaceColorAtElevation(
                    0.dp
                )
            ),
        disabledContentColor: Color = contentColorFor(containerColor).copy(alpha = 0.38f),
        selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        selectedContentColor: Color = contentColorFor(selectedContainerColor)
    ) = TaskCardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor
    )

    @Composable
    fun borderColors(
        borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
        selectedBorderColor: Color = MaterialTheme.colorScheme.secondary,
        disabledBorderColor: Color = MaterialTheme.colorScheme.outline
            .copy(alpha = 0.12f)
            .compositeOver(
                MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
            )
    ) = TaskCardBorderColors(
        borderColor = borderColor,
        selectedBorderColor = selectedBorderColor,
        disabledBorderColor = disabledBorderColor
    )

    /**
     * Creates a [CardElevation] that will animate between the provided values according to the
     * Material specification for an [OutlinedCard].
     *
     * @param defaultElevation the elevation used when the [OutlinedCard] is has no other
     * [Interaction]s.
     * @param pressedElevation the elevation used when the [OutlinedCard] is pressed.
     * @param focusedElevation the elevation used when the [OutlinedCard] is focused.
     * @param hoveredElevation the elevation used when the [OutlinedCard] is hovered.
     * @param draggedElevation the elevation used when the [OutlinedCard] is dragged.
     */
    @Composable
    fun cardElevation(
        defaultElevation: Dp = /* OutlinedCardTokens.ContainerElevation */ 0.dp,
        pressedElevation: Dp = defaultElevation,
        focusedElevation: Dp = defaultElevation,
        hoveredElevation: Dp = defaultElevation,
        draggedElevation: Dp = /* OutlinedCardTokens.DraggedContainerElevation */ 6.dp,
        disabledElevation: Dp = /* OutlinedCardTokens.DisabledContainerElevation */ 0.dp
    ): CardElevation = CardDefaults.outlinedCardElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        draggedElevation = draggedElevation,
        disabledElevation = disabledElevation
    )
}
