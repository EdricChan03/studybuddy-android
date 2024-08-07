package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.plusAssign
import androidx.core.view.updatePadding
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityViewTaskBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.markwon.coilImagesPlugin
import com.edricchan.studybuddy.exts.markwon.linkifyPlugin
import com.edricchan.studybuddy.exts.markwon.setMarkdown
import com.edricchan.studybuddy.exts.markwon.strikethroughPlugin
import com.edricchan.studybuddy.exts.markwon.taskListPlugin
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.tasks.compat.utils.TodoUtils
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.dialogs.showAuthRequiredDialog
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ViewTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String
    private lateinit var todoUtils: TodoUtils
    private lateinit var binding: ActivityViewTaskBinding
    private var currentUser: FirebaseUser? = null
    private var todoItem: TodoItem? = null

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

        firestore = Firebase.firestore
        auth = Firebase.auth
        currentUser = auth.currentUser
        todoUtils = TodoUtils(auth, firestore)

        taskId = intent.getStringExtra(EXTRA_TASK_ID) ?: ""
        if (taskId.isEmpty()) {
            Log.e(TAG, "No task ID was specified!")
            // TODO: i18n code
            showToast(
                "An unknown error occurred while attempting to retrieve the" +
                    "task. Try again later.", Toast.LENGTH_LONG
            )
            finish()
        } else {
            taskDocument = todoUtils.getTask(taskId)
        }
        Log.d(TAG, "Task ID: $taskId")

        binding.bottomAppBar.apply {
            replaceMenu(R.menu.menu_view_task)
            if (todoItem?.archived == true) {
                // Task has already been archived - show the unarchive menu item instead
                menu.findItem(R.id.action_unarchive).isVisible = true
                menu.findItem(R.id.action_archive).isVisible = false
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    android.R.id.home -> {
                        onBackPressedDispatcher.onBackPressed()
                        true
                    }

                    R.id.action_delete -> {
                        showMaterialAlertDialog {
                            // TODO: i18n code
                            setTitle("Delete todo?")
                            setPositiveButton(R.string.dialog_action_ok) { _, _ ->
                                todoUtils.removeTask(taskId)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            showToast(
                                                "Successfully deleted todo!",
                                                Toast.LENGTH_SHORT
                                            )
                                            finish()
                                        } else {
                                            showToast(
                                                "An error occurred while deleting the todo. Try again later.",
                                                Toast.LENGTH_LONG
                                            )
                                            Log.e(
                                                TAG,
                                                "An error occurred while deleting the todo.",
                                                task.exception
                                            )
                                        }
                                    }
                            }
                            setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
                        }
                        true
                    }

                    R.id.action_edit -> {
                        startActivity<EditTaskActivity> {
                            putExtra(EditTaskActivity.EXTRA_TASK_ID, taskId)
                        }
                        true
                    }

                    R.id.action_mark_as_done -> {
                        taskDocument
                            .update("done", !todoItem?.done!!)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showSnackbar(
                                        "Successfully marked task as " +
                                            if (!todoItem!!.done!!) "done" else "undone",
                                        Snackbar.LENGTH_SHORT
                                    )
                                    Log.d(
                                        TAG,
                                        "Task marked as " + if (!todoItem!!.done!!) "done" else "undone"
                                    )
                                } else {
                                    showSnackbar(
                                        "An error occurred while marking the todo as " +
                                            if (!todoItem!!.done!!) "done" else "undone",
                                        Snackbar.LENGTH_LONG
                                    )
                                    Log.e(
                                        TAG,
                                        "An error occurred while marking the todo as " +
                                            if (!todoItem!!.done!!) "done" else "undone",
                                        task.exception
                                    )
                                }
                            }
                        true
                    }

                    R.id.action_archive -> {
                        // Variable to indicate whether task was already archived
                        val hasArchived = todoItem?.archived ?: false
                        Log.d(TAG, "Archived state: $hasArchived")
                        taskDocument
                            .update("archived", !hasArchived)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showSnackbar(
                                        "Successfully archived task!",
                                        Snackbar.LENGTH_SHORT
                                    )
                                    Log.d(TAG, "Successfully archived task!")
                                } else {
                                    showToast(
                                        "An error occurred while attempting to archive the task",
                                        Toast.LENGTH_SHORT
                                    )
                                    Log.e(
                                        TAG,
                                        "An error occurred while attempting to archive the task:",
                                        task.exception
                                    )
                                }
                            }
                        menu.findItem(R.id.action_unarchive).isVisible = true
                        menu.findItem(R.id.action_archive).isVisible = false
                        true
                    }

                    R.id.action_unarchive -> {
                        // Variable to indicate whether task was already archived
                        val hasArchived = todoItem?.archived ?: true
                        Log.d(TAG, "Archived state: $hasArchived")
                        taskDocument
                            .update("archived", !hasArchived)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showSnackbar(
                                        "Successfully unarchived task!",
                                        Snackbar.LENGTH_SHORT
                                    )
                                    Log.d(TAG, "Successfully unarchived task!")
                                } else {
                                    showToast(
                                        "An error occurred while attempting to unarchive the task",
                                        Toast.LENGTH_SHORT
                                    )
                                    Log.e(
                                        TAG,
                                        "An error occurred while attempting to unarchive the task:",
                                        task.exception
                                    )
                                }
                            }
                        menu.findItem(R.id.action_archive).isVisible = true
                        menu.findItem(R.id.action_unarchive).isVisible = false
                        true
                    }

                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

        binding.editTaskFab.setOnClickListener {
            startActivity<EditTaskActivity> {
                putExtra(EditTaskActivity.EXTRA_TASK_ID, taskId)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (currentUser == null) {
            Log.d(TAG, "Not logged in")
            showAuthRequiredDialog()
        } else {
            loadTask()
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
        if (todoItem != null && todoItem?.title != null) {
            outState.putString(TASK_TITLE_TAG, todoItem?.title)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.getString(TASK_TITLE_TAG) != null) {
            title = savedInstanceState.getString(TASK_TITLE_TAG)
        }
    }

    /**
     * Loads the task that was supplied via `taskId`
     */
    private fun loadTask() {
        taskDocument
            .addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    Log.e(TAG, "An error occurred while retrieving the task:", e)
                    showSnackbar(
                        R.string.view_task_unsuccessful_snackbar_text,
                        Snackbar.LENGTH_LONG
                    )
                } else {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        setViews(documentSnapshot.toObject<TodoItem>())
                        todoItem = documentSnapshot.toObject<TodoItem>()
                        Log.d(TAG, "Task item: $todoItem")
                        Log.d(TAG, "Document snapshot data: ${documentSnapshot.data}")
                        title = todoItem?.title
                    }
                }
            }
    }

    /**
     * Sets all of the [TextView] and [com.google.android.material.chip.ChipGroup] values
     *
     * @param item The task item
     */
    private fun setViews(item: TodoItem?) {
        binding.apply {
            taskContent.apply {
                item?.content.also {
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
                item?.dueDate.also {
                    isVisible = it != null
                }?.let {
                    // We need to convert it to a LocalDateTime as Instants don't support
                    // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                    // for more info
                    text = it.toLocalDateTime().format(getString(R.string.date_format_pattern))
                }
            }
            taskProject.apply {
                item?.project.also {
                    isVisible = it != null
                }?.let {
                    it.get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful && task.result != null && task.result!!.exists()) {
                                text = task.result!!.getString("name")
                            } else {
                                showToast(
                                    "An error occurred while attempting to retrieve the project. Please try again later.",
                                    Toast.LENGTH_SHORT
                                )
                                Log.e(
                                    TAG,
                                    "An error occurred while attempting to retrieve the project:",
                                    task.exception
                                )
                            }
                        }
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
                        this += Chip(this@ViewTaskActivity).apply { text = tag }
                    }
                }
            }
            progressBar.isVisible = false
            scrollTaskView.isVisible = true
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

        /** The extra tag for the task's ID. */
        const val EXTRA_TASK_ID = "taskId"
    }
}
