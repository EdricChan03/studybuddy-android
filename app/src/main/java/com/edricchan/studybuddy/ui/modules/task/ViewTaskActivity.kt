package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.core.view.plusAssign
import androidx.core.view.updatePadding
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityViewTaskBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.android.widget.paintStrikeThrough
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.markwon.coilImagesPlugin
import com.edricchan.studybuddy.exts.markwon.linkifyPlugin
import com.edricchan.studybuddy.exts.markwon.setMarkdown
import com.edricchan.studybuddy.exts.markwon.strikethroughPlugin
import com.edricchan.studybuddy.exts.markwon.taskListPlugin
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.data.model.TodoProject
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.dialogs.showAuthRequiredDialog
import com.edricchan.studybuddy.ui.modules.task.vm.TaskDetailViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityViewTaskBinding
    private var currentUser: FirebaseUser? = null
    private lateinit var todoItem: TodoItem

    private val viewModel by viewModels<TaskDetailViewModel>()

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTaskBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.updatePadding(
                left = insets.left,
                right = insets.right
            )

            windowInsets
        }

        auth = Firebase.auth
        currentUser = auth.currentUser

        if (viewModel.taskId.isBlank()) {
            Log.e(TAG, "No task ID was specified!")
            showToast(R.string.task_fetch_fail_msg, Toast.LENGTH_LONG)
            finish()
        }
        Log.d(TAG, "Task ID: ${viewModel.taskId}")

        binding.bottomAppBar.apply {
            replaceMenu(R.menu.menu_view_task)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    android.R.id.home -> {
                        onBackPressedDispatcher.onBackPressed()
                        true
                    }

                    R.id.action_delete -> {
                        showMaterialAlertDialog {
                            setTitle(R.string.todo_frag_delete_task_dialog_title)
                            setPositiveButton(R.string.dialog_action_ok) { _, _ ->
                                onRemoveTask()
                            }
                            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
                        }
                        true
                    }

                    R.id.action_edit -> {
                        navigateToEditTask(viewModel.taskId)
                        true
                    }

                    R.id.action_mark_as_done -> {
                        onToggleComplete()
                        true
                    }

                    R.id.action_archive -> {
                        onToggleArchive()
                        menu.findItem(R.id.action_unarchive).isVisible = true
                        menu.findItem(R.id.action_archive).isVisible = false
                        true
                    }

                    R.id.action_unarchive -> {
                        onToggleArchive()
                        menu.findItem(R.id.action_archive).isVisible = true
                        menu.findItem(R.id.action_unarchive).isVisible = false
                        true
                    }

                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

        binding.editTaskFab.setOnClickListener {
            navigateToEditTask(viewModel.taskId)
        }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            Log.d(TAG, "Not logged in")
            showAuthRequiredDialog()
        } else {
            loadTask()
            loadTaskProject()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        todoItem.apply {
            outState.putBoolean(TASK_COMPLETED_TAG, done ?: false)
            title?.let {
                outState.putString(TASK_TITLE_TAG, it)
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(TASK_TITLE_TAG)?.let { title = it }
        setTitleStrikeThrough(savedInstanceState.getBoolean(TASK_TITLE_TAG, false))
    }

    private fun setTitleStrikeThrough(
        isCompleted: Boolean
    ) {
        // Hacky workaround to get the Toolbar's TextView to apply the strike-through
        // on, but it appears to work ¯\_(ツ)_/¯
        // TODO: Revisit to verify performance
        binding.toolbar.children.filterIsInstance<TextView>().forEach {
            it.paintStrikeThrough(isCompleted)
        }
    }

    private fun navigateToEditTask(taskId: String) {
        startActivity<EditTaskActivity> {
            putExtra(EditTaskActivity.EXTRA_TASK_ID, taskId)
        }
    }

    private fun onRemoveTask() {
        lifecycleScope.launch {
            try {
                viewModel.delete(todoItem)
                showToast(R.string.task_delete_success_msg, Toast.LENGTH_SHORT)
                finish()
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
        lifecycleScope.launch {
            try {
                // Variable to indicate whether task was already archived
                val hasArchived = todoItem.archived ?: false
                Log.d(TAG, "Archived state: $hasArchived")

                viewModel.archive(viewModel.taskId, !hasArchived)
                Log.d(TAG, "Successfully toggled task archival state to ${!hasArchived}")
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
        lifecycleScope.launch {
            try {
                viewModel.toggleCompleted(todoItem)
            } catch (e: Exception) {
                showSnackbar(
                    if (todoItem.done != true) R.string.task_done_fail_msg else R.string.task_undone_fail_msg,
                    Snackbar.LENGTH_LONG
                )
                Log.e(
                    TAG,
                    "An error occurred while marking the todo as " +
                        if (todoItem.done != true) "done" else "undone",
                    e
                )
            }
        }
    }

    /**
     * Loads the task that was supplied via `taskId`
     */
    private fun loadTask() {
        lifecycleScope.launch {
            try {
                viewModel.taskFlow.flowWithLifecycle(lifecycle)
                    .collect { item ->
                        if (item != null) {
                            setViews(item)
                            todoItem = item
                            Log.d(TAG, "Task item: $todoItem")
                            title = todoItem.title
                        } else {
                            Log.d(TAG, "Null task item returned")
                            finish()
                        }
                    }
            } catch (e: Exception) {
                Log.e(TAG, "An error occurred while retrieving the task:", e)
                showSnackbar(
                    R.string.view_task_unsuccessful_snackbar_text,
                    Snackbar.LENGTH_LONG
                )
            }
        }
    }

    private fun loadTaskProject() {
        lifecycleScope.launch {
            try {
                viewModel.taskProjectFlow
                    .flowWithLifecycle(lifecycle)
                    .collect { project ->
                        setViews(project)
                    }
            } catch (e: Exception) {
                showToast(R.string.task_project_fetch_fail_msg, Toast.LENGTH_SHORT)
                Log.e(
                    TAG,
                    "An error occurred while attempting to retrieve the project:",
                    e
                )
            }
        }
    }

    private fun setViews(item: TodoItem) {
        binding.apply {
            bottomAppBar.apply {
                if (item.archived == true) {
                    // Task has already been archived - show the unarchive menu item instead
                    menu.findItem(R.id.action_unarchive).isVisible = true
                    menu.findItem(R.id.action_archive).isVisible = false
                }
            }
            toolbar.apply {
                title = item.title
                setTitleStrikeThrough(item.done ?: false)
            }
            taskContent.apply {
                item.content.also {
                    isVisible = it != null
                }?.let {
                    setMarkdown(it) {
                        usePlugins(
                            this@ViewTaskActivity.coilImagesPlugin,
                            linkifyPlugin,
                            strikethroughPlugin,
                            this@ViewTaskActivity.taskListPlugin
                        )
                    }
                }
            }
            taskDate.apply {
                item.dueDate.also {
                    isVisible = it != null
                }?.let {
                    // This Timestamp needs to be converted to a LocalDateTime as
                    // Instants don't support temporal units bigger than days - see
                    // the `Instant#isSupported` Javadoc for more info
                    text = it.toLocalDateTime().format(getString(R.string.date_format_pattern))
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
                        this += Chip(this@ViewTaskActivity).apply { text = tag }
                    }
                }
            }
            progressBar.isVisible = false
            scrollTaskView.isVisible = true
        }
    }

    private fun setViews(project: TodoProject?) {
        binding.taskProject.apply {
            project.also {
                isVisible = it != null
            }?.let {
                text = it.name
            }
        }
    }

    private fun showSnackbar(text: String, @Duration duration: Int) {
        Snackbar.make(binding.mainView, text, duration)
            .setAnchorView(binding.editTaskFab)
            .show()
    }

    private fun showSnackbar(@StringRes textRes: Int, @Duration duration: Int) {
        showSnackbar(getString(textRes), duration)
    }

    companion object {
        // Tag to be used for saving the task item's title
        private const val TASK_TITLE_TAG = "taskTitle"

        // Tag to be used for saving the task item's completion state
        private const val TASK_COMPLETED_TAG = "taskCompleted"

        private const val TAG = "ViewTaskActivity"
    }
}
