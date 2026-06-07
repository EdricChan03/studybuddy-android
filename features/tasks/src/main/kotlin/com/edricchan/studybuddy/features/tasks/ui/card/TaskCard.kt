package com.edricchan.studybuddy.features.tasks.ui.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.outlined.Check
import com.edricchan.studybuddy.core.resources.icons.outlined.CheckCircle
import com.edricchan.studybuddy.core.resources.icons.outlined.Circle
import com.edricchan.studybuddy.core.resources.icons.outlined.Delete
import com.edricchan.studybuddy.core.resources.icons.outlined.Undo
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.domain.model.TaskItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskContentListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskCreatedAtOverline
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskDueDateListItem
import com.edricchan.studybuddy.features.tasks.ui.attrs.TaskTitleListItem
import com.edricchan.studybuddy.ui.theming.compose.theme.preview.StudyBuddyThemeWrapperProvider
import com.edricchan.studybuddy.utils.androidx.compose.ui.tooling.preview.BooleanPreviewParameterProvider
import java.time.Duration
import java.time.Instant

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
    dueDate: Instant? = null,
    createdAt: Instant? = null,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    onLongClickLabel: String? = null,
    onMarkAsDoneClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val selectionModifier = if (inSelectionMode) Modifier
        .combinedClickable(
            interactionSource = interactionSource,
            enabled = enabled,
            onClick = {},
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
        .toggleable(
            interactionSource = interactionSource,
            enabled = enabled,
            value = selected,
            onValueChange = onSelectedChange
        ) else Modifier.combinedClickable(
        interactionSource = interactionSource,
        enabled = enabled,
        onClick = onClick,
        onLongClick = onLongClick,
        onLongClickLabel = onLongClickLabel
    )

    val isSelected by rememberUpdatedState(inSelectionMode && selected)
    val containerColor by colors.containerColor(enabled = enabled, selected = isSelected)
    val contentColor by colors.contentColor(enabled = enabled, selected = isSelected)
    val borderColor by borderColors.borderColor(enabled = enabled, selected = isSelected)

    OutlinedCard(
        modifier = modifier then selectionModifier,
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
                createdAt?.let { TaskCreatedAtOverline(createdAt = it) }
                TaskTitleListItem(
                    textModifier = titleTextModifier,
                    title = title ?: stringResource(R.string.task_adapter_empty_title),
                    isDone = isDone,
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
                TaskContentListItem(
                    content = content ?: stringResource(R.string.task_adapter_empty_content),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    )
                )
                dueDate?.let {
                    TaskDueDateListItem(
                        dueDate = it,
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }

                AnimatedVisibility(
                    modifier = Modifier.padding(top = 8.dp),
                    visible = !inSelectionMode,
                    label = "Card actions visibility",
                    enter = fadeIn(
                        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
                    ) + expandVertically(
                        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                    ),
                    exit = fadeOut(
                        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
                    ) + shrinkVertically(
                        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                    )
                ) {
                    TaskCardActions(
                        isCompleted = isDone,
                        onMarkAsCompletedClick = onMarkAsDoneClick,
                        onDeleteClick = onDeleteClick
                    )
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
                enter = fadeIn(
                    animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
                ) + scaleIn(
                    animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                ),
                exit = fadeOut(
                    animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
                ) + scaleOut(
                    animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
                )
            ) {
                TaskCardSelectionIndicator(selected = selected)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun TaskCardSelectionIndicator(modifier: Modifier = Modifier, selected: Boolean) {
    Crossfade(
        modifier = modifier,
        targetState = selected,
        label = "selected icon crossfade",
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
    ) {
        if (it) {
            Surface(
                modifier = Modifier.padding(8.dp),
                shape = MaterialShapes.Cookie9Sided.toShape(),
                color = MaterialTheme.colorScheme.secondary,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Icon(
                    AppIcons.Outlined.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                )
            }
        } else {
            Icon(
                AppIcons.Outlined.Circle,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun TaskCardActions(
    modifier: Modifier = Modifier,
    isCompleted: Boolean,
    onMarkAsCompletedClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val labelToggleComplete = stringResource(
        if (isCompleted) R.string.task_adapter_mark_as_undone_btn_text
        else R.string.task_adapter_mark_as_done_btn_text
    )
    val labelRequestDelete = stringResource(R.string.task_adapter_delete_task_btn_text)

    ButtonGroup(
        // Prevent the navigation to these individual buttons when using tab navigation -
        // there will be custom accessibility actions specified
        modifier = modifier,
        overflowIndicator = {
            ButtonGroupDefaults.OverflowIndicator(
                menuState = it
            )
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val completedIcon = if (!isCompleted) AppIcons.Outlined.Check
        else AppIcons.Outlined.Undo
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                ToggleButton(
                    modifier = Modifier
                        .weight(1f)
                        .animateWidth(
                            interactionSource = interactionSource,
                            compressionLimit = ButtonDefaults.ButtonWithIconContentPadding
                        )
                        .clearAndSetSemantics {},
                    checked = isCompleted,
                    onCheckedChange = {
                        onMarkAsCompletedClick()
                    },
                    interactionSource = interactionSource
                ) {
                    Icon(completedIcon, contentDescription = null)
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = labelToggleComplete)
                }
            },
            menuContent = { state ->
                DropdownMenuItem(
                    shapes = MenuDefaults.itemShapes(),
                    checked = isCompleted,
                    onCheckedChange = {
                        onMarkAsCompletedClick()
                        state.dismiss()
                    },
                    leadingIcon = {
                        Icon(completedIcon, contentDescription = null)
                    },
                    text = {
                        Text(text = labelToggleComplete)
                    }
                )
            }
        )
        customItem(
            buttonGroupContent = {
                val interactionSource = remember { MutableInteractionSource() }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Below
                    ),
                    state = rememberTooltipState(),
                    tooltip = {
                        PlainTooltip {
                            Text(text = labelRequestDelete)
                        }
                    }
                ) {
                    FilledTonalIconButton(
                        modifier = Modifier
                            .animateWidth(interactionSource)
                            .clearAndSetSemantics {},
                        onClick = onDeleteClick,
                        shapes = IconButtonDefaults.shapes(),
                        interactionSource = interactionSource
                    ) {
                        Icon(AppIcons.Outlined.Delete, contentDescription = null)
                    }
                }
            },
            menuContent = { state ->
                DropdownMenuItem(
                    leadingIcon = {
                        Icon(AppIcons.Outlined.Delete, contentDescription = null)
                    },
                    text = {
                        Text(text = labelRequestDelete)
                    },
                    onClick = {
                        onDeleteClick()
                        state.dismiss()
                    },
                )
            }
        )
    }
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun TaskCardPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isCompleted: Boolean
) {
    TaskCard(
        title = "Jetpack Compose rewrite",
        content = "Rewrite StudyBuddy to use **Jetpack Compose**, ideally at some point :)",
        createdAt = Instant.now(),
        dueDate = Instant.now() + Duration.ofDays(1),
        isDone = isCompleted,
        onClick = {},
        onMarkAsDoneClick = {},
        onDeleteClick = {}
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun DoneTaskCardPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    TaskCard(
        title = "This is a task item",
        content = text,
        dueDate = Instant.now(),
        isDone = true,
        onClick = {},
        onMarkAsDoneClick = {},
        onDeleteClick = {}
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun SelectedTaskCardPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    TaskCard(
        title = "This is a task item",
        content = text,
        dueDate = Instant.now(),
        isDone = true,
        inSelectionMode = true,
        selected = true,
        onClick = {},
        onMarkAsDoneClick = {},
        onDeleteClick = {}
    )
}

@Preview
@PreviewWrapper(StudyBuddyThemeWrapperProvider::class)
@Composable
private fun UnselectedTaskCardPreview(
    @PreviewParameter(LoremIpsum::class) text: String
) {
    TaskCard(
        title = "This is a task item",
        content = text,
        dueDate = Instant.now(),
        isDone = true,
        inSelectionMode = true,
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
    task: TaskItem,
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
    dueDate = task.dueDate,
    createdAt = task.createdAt,
    isDone = task.isCompleted,
    onClick = onClick,
    onLongClick = onLongClick,
    onLongClickLabel = onLongClickLabel,
    onMarkAsDoneClick = onMarkAsDoneClick,
    onDeleteClick = onDeleteClick
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Immutable
data class TaskCardColors(
    val containerColor: Color,
    val contentColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color,
    val disabledSelectedContainerColor: Color,
    val disabledSelectedContentColor: Color,
) {
    @Composable
    fun containerColor(enabled: Boolean, selected: Boolean): State<Color> = animateColorAsState(
        targetValue = when {
            enabled && selected -> selectedContainerColor
            enabled && !selected -> containerColor
            !enabled && selected -> disabledSelectedContainerColor
            else -> disabledContainerColor
        },
        label = "Task card container colour",
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
    )

    @Composable
    fun contentColor(enabled: Boolean, selected: Boolean): State<Color> = animateColorAsState(
        targetValue = when {
            enabled && selected -> selectedContentColor
            enabled && !selected -> contentColor
            !enabled && selected -> disabledSelectedContentColor
            else -> disabledContentColor
        },
        label = "Task card content colour",
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Immutable
data class TaskCardBorderColors(
    val borderColor: Color,
    val selectedBorderColor: Color,
    val disabledBorderColor: Color,
    val disabledSelectedBorderColor: Color,
) {
    @Composable
    fun borderColor(enabled: Boolean, selected: Boolean): State<Color> = animateColorAsState(
        targetValue = when {
            enabled && selected -> selectedBorderColor
            enabled && !selected -> borderColor
            !enabled && selected -> disabledSelectedBorderColor
            else -> disabledBorderColor
        },
        label = "Task card border colour",
        animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()
    )
}

object TaskCardDefaults {
    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        contentColor: Color = contentColorFor(containerColor),
        disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant
            .copy(alpha = 0.38f)
            .compositeOver(MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)),
        disabledContentColor: Color = contentColorFor(containerColor).copy(alpha = 0.38f),
        selectedContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        selectedContentColor: Color = contentColorFor(selectedContainerColor),
        disabledSelectedContainerColor: Color = selectedContainerColor.copy(
            alpha = 0.38f
        ),
        disabledSelectedContentColor: Color = selectedContentColor.copy(alpha = 0.38f)
    ) = TaskCardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor,
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor,
        disabledSelectedContainerColor = disabledSelectedContainerColor,
        disabledSelectedContentColor = disabledSelectedContentColor
    )

    @Composable
    fun borderColors(
        borderColor: Color = MaterialTheme.colorScheme.outlineVariant,
        selectedBorderColor: Color = MaterialTheme.colorScheme.secondary,
        disabledBorderColor: Color = MaterialTheme.colorScheme.outline
            .copy(alpha = 0.12f)
            .compositeOver(
                MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
            ),
        disabledSelectedBorderColor: Color = selectedBorderColor
            .copy(alpha = 0.12f)
            .compositeOver(
                MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp)
            )
    ) = TaskCardBorderColors(
        borderColor = borderColor,
        selectedBorderColor = selectedBorderColor,
        disabledBorderColor = disabledBorderColor,
        disabledSelectedBorderColor = disabledSelectedBorderColor
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
