package com.edricchan.studybuddy.features.tasks.compat.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.plusAssign
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.compat.navigation.task.navigateToTaskEdit
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.databinding.FragTaskDetailBinding
import com.edricchan.studybuddy.features.tasks.detail.data.mapCurrentTask
import com.edricchan.studybuddy.features.tasks.detail.data.state.TaskDetailState
import com.edricchan.studybuddy.features.tasks.domain.model.TaskProject
import com.edricchan.studybuddy.features.tasks.vm.TaskDetailViewModel
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fab.FabConfig
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.edricchan.studybuddy.utils.dev.isDevMode
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.edricchan.studybuddy.core.resources.R as CoreResR

@AndroidEntryPoint
class TaskDetailFragment :
    ViewBindingFragment<FragTaskDetailBinding>(FragTaskDetailBinding::inflate) {

    private val viewModel by viewModels<TaskDetailViewModel>()

    override val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.menu_view_task, menu)
        }

        override fun onPrepareMenu(menu: Menu) {
            super.onPrepareMenu(menu)

            viewLifecycleOwner.lifecycleScope.launch {
                val hasArchived = viewModel.mapCurrentTask { it.isArchived }
                menu.findItem(R.id.action_unarchive)?.isVisible = hasArchived
                menu.findItem(R.id.action_archive)?.isVisible = !hasArchived
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_edit -> {
                    navController.navigateToTaskEdit(viewModel.currentTaskId)
                    true
                }

                R.id.action_mark_as_done -> {
                    viewModel.onToggleComplete()
                    true
                }

                R.id.action_delete -> {
                    requireContext().showMaterialAlertDialog {
                        setTitle(R.string.todo_frag_delete_task_dialog_title)
                        setMessage(R.string.todo_frag_delete_task_dialog_msg)
                        setNegativeButton(android.R.string.cancel, null) // No-op
                        setPositiveButton(R.string.action_delete_task) { _, _ ->
                            viewModel.onDeleteTask()
                        }
                    }
                    true
                }

                R.id.action_archive, R.id.action_unarchive -> {
                    viewModel.onToggleArchived()
                    true
                }

                else -> false
            }
        }
    }

    override val fabConfig = FabConfig(
        iconRes = R.drawable.ic_edit_outline_24dp,
        contentDescriptionRes = R.string.action_edit_task,
        onClick = {
            navController.navigateToTaskEdit(viewModel.currentTaskId)
        },
        alignment = BottomAppBar.FAB_ALIGNMENT_MODE_END
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.scrollTaskView) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())

            v.updatePadding(bottom = insets.bottom)

            windowInsets
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentTaskStateFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect(::setTaskData)
        }
    }

    private fun setTaskProjectData(project: TaskProject?) {
        binding.taskProject.apply {
            project.also {
                isVisible = it != null
            }?.let { project ->
                text = project.name
            }
        }
    }

    private fun setTaskData(state: TaskDetailState) {
        binding.progressBar.isVisible = state is TaskDetailState.Loading
        binding.scrollTaskView.isVisible = state !is TaskDetailState.Loading
        requireActivity().invalidateMenu()

        when (state) {
            TaskDetailState.Loading -> {}
            TaskDetailState.NoData -> {
                viewLifecycleOwner.lifecycleScope.launch {
                    // This NoData state can be emitted right after the task has been
                    // deleted, so we need to catch it here rather than in the ViewModel,
                    // where it wouldn't get triggered otherwise as we would've already
                    // navigated up
                    if (viewModel.pendingDelete.value) {
                        viewModel.showSnackBar(
                            R.string.task_delete_success_msg,
                            SnackBarData.Duration.Long
                        )
                    }
                    Log.d(
                        TAG,
                        "setTaskData: Attempted to load task ID ${viewModel.currentTaskId} but no such task exists"
                    )
                    navController.navigateUp()
                }
            }

            is TaskDetailState.Error -> {
                Log.e(TAG, "setTaskData: Could not load task item:", state.error)
            }

            is TaskDetailState.Success -> {
                val item = state.item
                setTaskProjectData(item.project)
                binding.apply {
                    taskTitle.apply {
                        item.title.takeIf { it.isNotBlank() }.also {
                            isVisible = it != null
                        }?.let { text = it }
                    }
                    taskContent.apply {
                        item.content.also {
                            isVisible = it != null
                        }?.let {
                            markdownText = it
                        }
                    }
                    taskId.apply {
                        isVisible = requireContext().isDevMode()
                        text = viewModel.currentTaskId
                    }
                    taskDate.apply {
                        item.dueDate.also {
                            isVisible = it != null
                        }?.let {
                            // We need to convert it to a LocalDateTime as Instants don't support
                            // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                            // for more info
                            text = it.toLocalDateTime().format(
                                getString(CoreResR.string.java_time_format_pattern_default)
                            )
                        }
                    }
                    taskTags.apply {
                        item.tags.also {
                            taskTagsParentView.isVisible = !it.isNullOrEmpty()
                        }?.let {
                            Log.d(TAG, "Tags: $it")
                            // Remove all chips or this will cause duplicate tags
                            removeAllViews()
                            it.forEach { tag ->
                                this += Chip(requireContext()).apply { text = tag }
                            }
                        }
                    }
                }
            }
        }
    }

    private companion object {
        private const val TAG = "TaskDetailFragment"
    }
}
