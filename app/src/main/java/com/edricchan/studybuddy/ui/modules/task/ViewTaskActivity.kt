package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityViewTaskBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.firebase.toLocalDateTime
import com.edricchan.studybuddy.extensions.format
import com.edricchan.studybuddy.extensions.showToast
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.interfaces.TodoItem
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.auth.RegisterActivity
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin

class ViewTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String
    private lateinit var todoUtils: TodoUtils
    private lateinit var binding: ActivityViewTaskBinding
    private var currentUser: FirebaseUser? = null
    private var todoItem: TodoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = Firebase.firestore
        auth = Firebase.auth
        currentUser = auth.currentUser
        todoUtils = TodoUtils.getInstance(auth, firestore)

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
                        val builder = MaterialAlertDialogBuilder(this@ViewTaskActivity)
                        // TODO: i18n code
                        builder.setTitle("Delete todo?")
                            .setPositiveButton(R.string.dialog_action_ok) { _, _ ->
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
                            .setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
                            .show()
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
            val signInDialogBuilder = MaterialAlertDialogBuilder(this)
            signInDialogBuilder
                .setTitle("Sign in")
                .setMessage("To access the content, please login or register for an account.")
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_action_login) { dialogInterface, _ ->
                    startActivity<LoginActivity>()
                    dialogInterface.dismiss()
                }
                .setNeutralButton(R.string.dialog_action_sign_up) { dialogInterface, _ ->
                    startActivity<RegisterActivity>()
                    dialogInterface.dismiss()
                }
                .setNegativeButton(R.string.dialog_action_cancel) { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .show()
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
     * Toggles the visiblity of a view to `visibility`
     *
     * @param view              The view to toggle the visibility of
     * @param initialVisibility The visibility to check against the view. Can be [View.GONE], [View.INVISIBLE] or [View.VISIBLE]
     * @param visibility        The visibility to toggle the view to. Can be [View.GONE], [View.INVISIBLE] or [View.VISIBLE]
     */
    private fun toggleViewVisibility(view: View, initialVisibility: Int, visibility: Int) {
        if (view.visibility == initialVisibility) {
            view.visibility = visibility
        }
    }

    /**
     * Sets all of the [TextView] and [com.google.android.material.chip.ChipGroup] values
     *
     * @param item The task item
     */
    private fun setViews(item: TodoItem?) {
        if (item?.content != null) {
            Markwon.builder(this)
                .usePlugin(CoilImagesPlugin.create(this))
                .usePlugin(LinkifyPlugin.create())
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TaskListPlugin.create(this))
                .build()
                .setMarkdown(binding.taskContent, item.content)
            toggleViewVisibility(binding.taskContent, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(binding.taskContent, View.VISIBLE, View.GONE)
        }
        if (item?.dueDate != null) {
            binding.taskDate.text = item.dueDate.toLocalDateTime().format(getString(R.string.date_format_pattern))
            toggleViewVisibility(binding.taskDate, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(binding.taskDate, View.VISIBLE, View.GONE)
        }
        if (item?.project != null) {
            item.project
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null && task.result!!.exists()) {
                        binding.taskProject.text = task.result!!.getString("name")
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
            toggleViewVisibility(binding.taskProject, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(binding.taskProject, View.VISIBLE, View.GONE)
        }
        /*if (item?.title != null) {
            taskTitle.text = item.title
            toggleViewVisibility(taskTitle, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(taskTitle!!, View.VISIBLE, View.GONE)
        }*/
        if (item?.tags != null && item.tags.isNotEmpty()) {
            // Remove all chips or this will cause duplicate tags
            binding.taskTags.removeAllViews()
            for (tag in item.tags) {
                val tempChip = Chip(this)
                tempChip.text = tag
                binding.taskTags.addView(tempChip)
            }
            toggleViewVisibility(binding.taskTagsParentView, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(binding.taskTagsParentView, View.VISIBLE, View.GONE)
        }
        toggleViewVisibility(binding.progressBar, View.VISIBLE, View.GONE)
        toggleViewVisibility(binding.scrollTaskView, View.GONE, View.VISIBLE)
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
