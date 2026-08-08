package com.edricchan.studybuddy.features.tasks.detail.ui.compat

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.common.ui.ConfirmDeleteTaskDialog
import com.edricchan.studybuddy.features.tasks.detail.data.mapCurrentTask
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.detail.ui.TaskDetailScreen
import com.edricchan.studybuddy.features.tasks.detail.vm.TaskDetailViewModel
import com.edricchan.studybuddy.features.tasks.navigation.navigateToEditTask
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fab.FabConfig
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.utils.androidx.core.menuProvider
import com.google.android.material.bottomappbar.BottomAppBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskDetailFragment : ComposableFragment() {
    private val viewModel by viewModels<TaskDetailViewModel>()

    private fun onNavigateToEditTask(taskId: String = viewModel.currentTaskId) {
        navController.navigateToEditTask(taskId)
    }

    override val menuProvider = menuProvider(
        menuResId = R.menu.menu_view_task,
        onPrepareMenu = { menu ->
            viewLifecycleOwner.lifecycleScope.launch {
                val hasArchived = viewModel.mapCurrentTask { it.isArchived }
                menu.findItem(R.id.action_unarchive)?.isVisible = hasArchived
                menu.findItem(R.id.action_archive)?.isVisible = !hasArchived
            }
        },
        onMenuItemSelected = {
            when (it.itemId) {
                R.id.action_edit -> {
                    onNavigateToEditTask()
                    true
                }

                R.id.action_mark_as_done -> {
                    viewModel.onToggleComplete()
                    true
                }

                R.id.action_delete -> {
                    viewModel.showConfirmDeleteDialog()
                    true
                }

                R.id.action_archive, R.id.action_unarchive -> {
                    viewModel.onToggleArchived()
                    true
                }

                else -> false
            }
        }
    )

    override val fabConfig = FabConfig(
        iconRes = R.drawable.ic_edit_outline_24dp,
        contentDescriptionRes = R.string.action_edit_task,
        onClick = ::onNavigateToEditTask,
        alignment = BottomAppBar.FAB_ALIGNMENT_MODE_END
    )

    @Composable
    override fun Content(modifier: Modifier) {
        TaskDetailScreen(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            viewModel = viewModel
        )

        if (viewModel.isConfirmDeleteDialogShown) {
            ConfirmDeleteTaskDialog(
                taskTitle = viewModel.currentTaskOrNull?.title,
                onDismissRequest = viewModel::dismissConfirmDeleteDialog,
                onDeleteTask = viewModel::onDeleteTask
            )
        }

        LaunchedEffect(Unit) {
            viewModel.currentTaskStateFlow.filterIsInstance<TaskDetailState.NoData>()
                .collect {
                    // This NoData state can be emitted right after the task has been
                    // deleted, so we need to catch it here rather than in the ViewModel,
                    // where it wouldn't get triggered otherwise as we would've already
                    // navigated up
                    if (viewModel.hasPendingDelete) {
                        showSnackBar(
                            R.string.task_delete_success_msg,
                            SnackBarData.Duration.Long
                        )
                        navController.navigateUp()
                    }
                }
        }
    }
}
