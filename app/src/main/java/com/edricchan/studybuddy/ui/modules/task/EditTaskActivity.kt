package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityEditTaskBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toInstant
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.time.Instant

class EditTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String
    private lateinit var todoUtils: TodoUtils
    private var currentUser: FirebaseUser? = null
    private var taskInstant: Instant? = null
    private var todoItem: TodoItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = Firebase.firestore
        auth = Firebase.auth
        currentUser = auth.currentUser

        todoUtils = TodoUtils.getInstance(auth, firestore)
        taskId = intent.getStringExtra(EXTRA_TASK_ID) ?: ""
        if (taskId.isEmpty()) {
            Log.e(TAG, "Please specify a task item ID!")
            Toast.makeText(
                this,
                "An error occurred while attempting to retrieve the task item's details. Please try again later.",
                Toast.LENGTH_LONG
            )
                .show()
            finish()
        } else {
            taskDocument = todoUtils.getTask(taskId)
            binding.apply {
                // TODO: Migrate logic to separate component
                taskDueDateChip.setOnClickListener {
                    val constraints = CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now())
                        .setStart(Instant.now().toEpochMilli())
                        .build()
                    val picker = MaterialDatePicker.Builder.datePicker().apply {
                        setCalendarConstraints(constraints)
                        // Set the initial selection
                        taskInstant?.let { setSelection(it.toEpochMilli()) }
                    }.build()
                    picker.addOnPositiveButtonClickListener { selection ->
                        Instant.ofEpochMilli(selection).let {
                            taskInstant = it
                            // Produces <day name>, <month> <year>
                            // We need to convert it to a LocalDateTime as Instants don't support
                            // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                            // for more info
                            taskDueDateChip.text =
                                it.toLocalDateTime().format(getString(R.string.date_format_pattern))
                        }
                        // Allow due date to be reset
                        taskDueDateChip.isCloseIconVisible = true
                    }
                    picker.show(supportFragmentManager, "taskDueDatePicker")
                }
                taskDueDateChip.setOnCloseIconClickListener {
                    // Reset due date
                    taskInstant = null

                    // Reset chip's state
                    taskDueDateChip.isCloseIconVisible = false
                    taskDueDateChip.setText(R.string.select_date_chip_default_text)
                }
                progressBar.isVisible = true
                scrollView.isVisible = false
                taskDocument
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                progressBar.isVisible = false
                                scrollView.isVisible = true
                                todoItem = document.toObject<TodoItem>()
                                if (todoItem?.title != null) {
                                    textInputTitle.editText?.setText(todoItem!!.title)
                                }
                                if (todoItem?.content != null) {
                                    textInputContent.editText?.setText(todoItem!!.content)
                                }
                                if (todoItem?.done != null) {
                                    checkboxMarkAsDone.isChecked = todoItem!!.done!!
                                } else {
                                    checkboxMarkAsDone.isChecked = false
                                }
                                todoItem?.dueDate?.let {
                                    // We need to convert it to a LocalDateTime as Instants don't support
                                    // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                                    // for more info
                                    taskDueDateChip.text = it.toLocalDateTime()
                                        .format(getString(R.string.date_format_pattern))
                                    // Allow due date to be reset
                                    taskDueDateChip.isCloseIconVisible = true
                                }
                                if (todoItem?.tags != null) {
                                    textInputTags.editText?.setText(
                                        todoItem!!.tags!!.joinToString(
                                            ","
                                        )
                                    )
                                }
                            }
                        } else {
                            Log.i(TAG, "Could not load task.", task.exception)
                            showToast(
                                "An error occurred while loading the task",
                                Toast.LENGTH_LONG
                            )
                        }
                    }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_edit_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            R.id.action_save -> {
                val taskItemUpdates = HashMap<String, Any>()
                if (todoItem != null) {
                    binding.apply {
                        if (textInputTitle.editTextStrValue != todoItem?.title) {
                            taskItemUpdates["title"] = textInputTitle.editTextStrValue
                        }
                        if (textInputContent.editTextStrValue != todoItem?.content) {
                            taskItemUpdates["content"] = textInputContent.editTextStrValue
                        }
                    }
                    taskInstant?.let {
                        if (todoItem!!.dueDate!!.toInstant() != it) {
                            taskItemUpdates["dueDate"] = it.toTimestamp()
                        }
                    }
                    taskDocument
                        .update(taskItemUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "Successfully updated task item with ID $taskId!")
                                Toast.makeText(
                                    this,
                                    "Successfully updated task item!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    "An error occurred while attempting to update the task item. Please try again later.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        /** The extra tag for the task's ID. */
        const val EXTRA_TASK_ID = "taskId"
    }
}
