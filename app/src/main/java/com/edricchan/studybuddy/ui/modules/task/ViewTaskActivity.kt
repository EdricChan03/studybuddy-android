package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.firebase.toDateFormat
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.auth.RegisterActivity
import com.edricchan.studybuddy.ui.modules.task.utils.TaskUtils
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar.Duration
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_view_task.*
import io.noties.markwon.Markwon

class ViewTaskActivity : AppCompatActivity(R.layout.activity_view_task) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String
    private lateinit var taskUtils: TaskUtils
    private var currentUser: FirebaseUser? = null
    private var taskItem: TaskItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
        taskUtils = TaskUtils.getInstance(auth, firestore)

        taskId = intent.getStringExtra(EXTRA_TASK_ID) ?: ""
        if (taskId.isEmpty()) {
            Log.e(TAG, "No task ID was specified!")
            Toast.makeText(
                this, "An unknown error occurred while attempting to retrieve the" +
                        "task. Try again later.", Toast.LENGTH_LONG
            ).show()
            finish()
        } else {
            taskDocument = taskUtils.getTask(taskId)
        }
        Log.d(TAG, "Task ID: $taskId")

        bottomAppBar.replaceMenu(R.menu.menu_view_task)

        if (taskItem?.archived == true) {
            // Task has already been archived - show the unarchive menu item instead
            bottomAppBar.menu.findItem(R.id.action_unarchive).isVisible = true
            bottomAppBar.menu.findItem(R.id.action_archive).isVisible = false
        }

        bottomAppBar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                R.id.action_delete -> {
                    val builder = MaterialAlertDialogBuilder(this)
                    builder.setTitle("Delete todo?")
                        .setPositiveButton(R.string.dialog_action_ok) { _, _ ->
                            taskUtils.removeTask(taskId)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this,
                                            "Successfully deleted todo!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "An error occurred while deleting the todo. Try again later.",
                                            Toast.LENGTH_LONG
                                        ).show()
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
                        .update("done", !taskItem?.done!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showSnackbar(
                                    "Successfully marked task as " +
                                            if (!taskItem!!.done!!) "done" else "undone",
                                    Snackbar.LENGTH_SHORT
                                )
                                Log.d(
                                    TAG,
                                    "Task marked as " + if (!taskItem!!.done!!) "done" else "undone"
                                )
                            } else {
                                showSnackbar(
                                    "An error occurred while marking the todo as " +
                                            if (!taskItem!!.done!!) "done" else "undone",
                                    Snackbar.LENGTH_LONG
                                )
                                Log.e(
                                    TAG,
                                    "An error occurred while marking the todo as " +
                                            if (!taskItem!!.done!!) "done" else "undone",
                                    task.exception
                                )
                            }
                        }
                    true
                }
                R.id.action_archive -> {
                    // Variable to indicate whether task was already archived
                    val hasArchived = taskItem?.archived ?: false
                    Log.d(TAG, "Archived state: $hasArchived")
                    taskDocument
                        .update("archived", !hasArchived)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showSnackbar("Successfully archived task!", Snackbar.LENGTH_SHORT)
                                Log.d(TAG, "Successfully archived task!")
                            } else {
                                Toast.makeText(
                                    this,
                                    "An error occurred while attempting to archive the task",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(
                                    TAG,
                                    "An error occurred while attempting to archive the task:",
                                    task.exception
                                )
                            }
                        }
                    bottomAppBar.menu.findItem(R.id.action_unarchive).isVisible = true
                    bottomAppBar.menu.findItem(R.id.action_archive).isVisible = false
                    true
                }
                R.id.action_unarchive -> {
                    // Variable to indicate whether task was already archived
                    val hasArchived = taskItem?.archived ?: true
                    Log.d(TAG, "Archived state: $hasArchived")
                    taskDocument
                        .update("archived", !hasArchived)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showSnackbar("Successfully unarchived task!", Snackbar.LENGTH_SHORT)
                                Log.d(TAG, "Successfully unarchived task!")
                            } else {
                                Toast.makeText(
                                    this,
                                    "An error occurred while attempting to unarchive the task",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(
                                    TAG,
                                    "An error occurred while attempting to unarchive the task:",
                                    task.exception
                                )
                            }
                        }
                    bottomAppBar.menu.findItem(R.id.action_archive).isVisible = true
                    bottomAppBar.menu.findItem(R.id.action_unarchive).isVisible = false
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener super.onOptionsItemSelected(it)
            }
        }
        editTaskFab.setOnClickListener {
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
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (taskItem != null && taskItem?.title != null) {
            outState.putString(TASK_TITLE_TAG, taskItem?.title)
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
                        setViews(documentSnapshot.toObject<TaskItem>())
                        taskItem = documentSnapshot.toObject<TaskItem>()
                        Log.d(TAG, "Task item: $taskItem")
                        Log.d(TAG, "Document snapshot data: ${documentSnapshot.data}")
                        title = taskItem?.title
                    }
                }
            }
        /*.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            mItem = document.toObject(TaskItem.class);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            findViewById(R.id.taskView).setVisibility(View.VISIBLE);
                            mTaskTitle.setText(mItem.title);
                            if (mItem.content != null) {
                                mTaskContent.setText(mItem.content);
                            }
                        } else {
                            // TODO: Add handler for document not existing
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.mainView), R.string.view_task_unsuccessful_snackbar_text, Snackbar.LENGTH_INDEFINITE)
                                .setBehavior(new NoSwipeBehavior())
                                .setAction(R.string.view_task_unsuccessful_snackbar_btn_text, v -> loadTask());
                    }
                });*/
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
    private fun setViews(item: TaskItem?) {
        if (item?.content != null) {
            Markwon.create(this).setMarkdown(taskContent, item.content)
            toggleViewVisibility(taskContent, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(taskContent, View.VISIBLE, View.GONE)
        }
        if (item?.dueDate != null) {
            taskDate.text = item.dueDate.toDateFormat(getString(R.string.date_format_pattern))
            toggleViewVisibility(taskDate, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(taskDate, View.VISIBLE, View.GONE)
        }
        if (item?.project != null) {
            item.project
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null && task.result!!.exists()) {
                        taskProject.text = task.result!!.getString("name")
                    } else {
                        Toast.makeText(
                            this,
                            "An error occurred while attempting to retrieve the project. Please try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(
                            TAG,
                            "An error occurred while attempting to retrieve the project:",
                            task.exception
                        )
                    }
                }
            toggleViewVisibility(taskProject, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(taskProject, View.VISIBLE, View.GONE)
        }
        /*if (item?.title != null) {
            taskTitle.text = item.title
            toggleViewVisibility(taskTitle, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(taskTitle!!, View.VISIBLE, View.GONE)
        }*/
        if (item?.tags != null && item.tags.isNotEmpty()) {
            // Remove all chips or this will cause duplicate tags
            taskTags.removeAllViews()
            for (tag in item.tags) {
                val tempChip = Chip(this)
                tempChip.text = tag
                taskTags.addView(tempChip)
            }
            toggleViewVisibility(taskTagsParentView, View.GONE, View.VISIBLE)
        } else {
            toggleViewVisibility(taskTagsParentView, View.VISIBLE, View.GONE)
        }
        toggleViewVisibility(progressBar, View.VISIBLE, View.GONE)
        toggleViewVisibility(scrollTaskView, View.GONE, View.VISIBLE)
    }

    private fun showSnackbar(text: String, @Duration duration: Int) {
        Snackbar.make(mainView, text, duration)
            .setAnchorView(editTaskFab)
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
