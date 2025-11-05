package com.edricchan.studybuddy.features.tasks.compat.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.plusAssign
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.edricchan.studybuddy.core.compat.navigation.task.navigateToTaskEdit
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.markwon.coilImagesPlugin
import com.edricchan.studybuddy.exts.markwon.linkifyPlugin
import com.edricchan.studybuddy.exts.markwon.setMarkdown
import com.edricchan.studybuddy.exts.markwon.strikethroughPlugin
import com.edricchan.studybuddy.exts.markwon.taskListPlugin
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.features.tasks.databinding.FragTaskDetailBinding
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

            val hasArchived = viewModel.currentTask?.archived ?: false
            menu.findItem(R.id.action_unarchive)?.isVisible = hasArchived
            menu.findItem(R.id.action_archive)?.isVisible = !hasArchived
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_edit -> {
                    navController.navigateToTaskEdit(viewModel.currentTaskId)
                    true
                }

                R.id.action_mark_as_done -> {
                    onToggleComplete()
                    true
                }

                R.id.action_delete -> {
                    requireContext().showMaterialAlertDialog {
                        setTitle(R.string.todo_frag_delete_task_dialog_title)
                        setMessage(R.string.todo_frag_delete_task_dialog_msg)
                        setNegativeButton(android.R.string.cancel, null) // No-op
                        setPositiveButton(R.string.action_delete_task) { _, _ ->
                            onRemoveTask()
                        }
                    }
                    true
                }

                R.id.action_archive, R.id.action_unarchive -> {
                    onToggleArchive()
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currTaskFlow.collect(::setTaskData)
                }

                launch {
                    viewModel.currTaskProjectFlow.collect(::setTaskProjectData)
                }
            }
        }
    }

    private fun setTaskProjectData(project: TodoProject?) {
        binding.taskProject.apply {
            project.also {
                isVisible = it != null
            }?.let { project ->
                text = project.name
            }
        }
    }

    private fun setTaskData(item: TodoItem?) {
        binding.apply {
            taskTitle.apply {
                item?.title.also {
                    isVisible = it != null
                }?.let { text = it }
            }
            taskContent.apply {
                item?.content.also {
                    isVisible = it != null
                }?.let {
                    setMarkdown(it) {
                        usePlugins(
                            requireContext().coilImagesPlugin,
                            linkifyPlugin,
                            strikethroughPlugin,
                            requireContext().taskListPlugin
                        )
                    }
                }
            }
            taskId.apply {
                isVisible = requireContext().isDevMode()
                text = viewModel.currentTaskId
            }
            taskDate.apply {
                item?.dueDate.also {
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
                item?.tags.also {
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
            progressBar.isVisible = false
            scrollTaskView.isVisible = true
        }
    }

    private fun onRemoveTask() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.deleteTask()
                showToast(R.string.task_delete_success_msg, Toast.LENGTH_SHORT)
                navController.navigateUp()
            } catch (e: Exception) {
                showToast(R.string.task_delete_fail_msg, Toast.LENGTH_LONG)
                Log.e(
                    TAG,
                    "An error occurred while deleting the task.",
                    e
                )
            }
        }
    }

    private fun onToggleArchive() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Variable to indicate whether task was already archived
                val hasArchived = viewModel.currentTask?.archived ?: false
                Log.d(TAG, "Archived state: $hasArchived")

                viewModel.archive(!hasArchived)
                Log.d(
                    TAG,
                    "Successfully toggled task archival state to ${!hasArchived}"
                )
                requireActivity().invalidateMenu()
            } catch (e: Exception) {
                showToast(R.string.task_archive_fail_msg, Toast.LENGTH_SHORT)
                Log.e(
                    TAG,
                    "An error occurred while attempting to archive the task:",
                    e
                )
            }
        }
    }

    private fun onToggleComplete() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                viewModel.currentTask?.let { viewModel.toggleCompleted(it) }
            } catch (e: Exception) {
                showSnackBar(
                    if (viewModel.currentTask?.done != true) R.string.task_done_fail_msg else R.string.task_undone_fail_msg,
                    SnackBarData.Duration.Long
                )
                Log.e(
                    TAG,
                    "An error occurred while marking the task as " +
                        if (viewModel.currentTask?.done != true) "done" else "undone",
                    e
                )
            }
        }
    }

    private companion object {
        private const val TAG = "TaskDetailFragment"
    }
}
