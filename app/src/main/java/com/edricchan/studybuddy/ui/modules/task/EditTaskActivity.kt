package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityEditTaskBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toInstant
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.exts.material.picker.setCalendarConstraints
import com.edricchan.studybuddy.exts.material.picker.setSelection
import com.edricchan.studybuddy.exts.material.picker.setStart
import com.edricchan.studybuddy.exts.material.picker.showMaterialDatePicker
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant

class EditTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String

    private lateinit var todoUtils: TodoUtils

    private var taskInstant: Instant? = null
    private lateinit var todoItem: TodoItem

    private lateinit var binding: ActivityEditTaskBinding

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditTaskBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = Firebase.firestore
        auth = Firebase.auth

        auth.currentUser?.let {
            currentUser = it
        } ?: run {
            Log.e(TAG, "User isn't logged in. Exiting...")
            showToast(
                "An error occurred while attempting to retrieve the task item's " +
                    "details. Please try again later.",
                Toast.LENGTH_LONG
            )
            finish()
            return
        }

        todoUtils = TodoUtils(auth, firestore)

        val taskIdExtra = intent.getStringExtra(EXTRA_TASK_ID)
        if (!intent.hasExtra(EXTRA_TASK_ID) || taskIdExtra.isNullOrBlank()) {
            Log.e(TAG, "No task item ID was specified. Exiting...")
            showToast(
                R.string.task_item_get_error_text,
                Toast.LENGTH_LONG
            )
            finish()
            return
        }
        taskId = taskIdExtra
        taskDocument = todoUtils.getTask(taskId)
        lifecycleScope.launch {
            val taskData = taskDocument.get().await()
            if (!taskData.exists()) {
                Log.e(TAG, "No such task with the ID $taskId exists. Exiting...")
                showToast(
                    R.string.task_item_get_error_text,
                    Toast.LENGTH_LONG
                )
                finish()
                return@launch
            }

            todoItem = taskData.toObject()
                ?: return@launch // null is returned if the doc doesn't exist

            binding.apply {
                // TODO: Migrate logic to separate component
                taskDueDateChip.apply {
                    setOnClickListener {
                        showMaterialDatePicker(
                            "taskDueDatePicker",
                            builderInit = {
                                setCalendarConstraints {
                                    setValidator(DateValidatorPointForward.now())
                                    setStart(Instant.now())
                                }
                                // Set the initial selection
                                taskInstant?.let { setSelection(it) }
                            },
                            pickerInit = {
                                addOnPositiveButtonClickListener { selection ->
                                    Instant.ofEpochMilli(selection).let {
                                        taskInstant = it
                                        // Produces <day name>, <month> <year>
                                        // We need to convert it to a LocalDateTime as Instants
                                        // don't support temporal units bigger than days - see
                                        // the `Instant#isSupported` Javadocs for more info
                                        text = it.toLocalDateTime()
                                            .format(getString(R.string.date_format_pattern))
                                    }
                                    // Allow due date to be reset
                                    isCloseIconVisible = true
                                }
                            })
                    }
                    setOnCloseIconClickListener {
                        // Reset due date
                        taskInstant = null

                        // Reset chip's state
                        isCloseIconVisible = false
                        setText(R.string.select_date_chip_default_text)
                    }
                }

                progressBar.isVisible = false
                scrollView.isVisible = true
                with(todoItem) {
                    title?.let { textInputTitle.editText?.setText(it) }
                    content?.let { textInputContent.editText?.setText(it) }
                    done?.let { checkboxMarkAsDone.isChecked = it }
                    dueDate?.let {
                        // We need to convert it to a LocalDateTime as Instants don't support
                        // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                        // for more info
                        taskDueDateChip.text = it.toLocalDateTime()
                            .format(getString(R.string.date_format_pattern))
                        // Allow due date to be reset
                        taskDueDateChip.isCloseIconVisible = true
                    }
                    tags?.let {
                        textInputTags.editText?.setText(it.joinToString(","))
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
                val taskItemUpdates = buildMap<String, Any> {
                    binding.also {
                        if (it.textInputTitle.editTextStrValue != todoItem.title) {
                            this["title"] = it.textInputTitle.editTextStrValue
                        }
                        if (it.textInputContent.editTextStrValue != todoItem.content) {
                            this["content"] = it.textInputContent.editTextStrValue
                        }
                    }
                    taskInstant?.let {
                        if (todoItem.dueDate?.toInstant() != it) {
                            this["dueDate"] = it.toTimestamp()
                        }
                    }
                }

                lifecycleScope.launch {
                    try {
                        taskDocument.update(taskItemUpdates).await()

                        Log.d(TAG, "Successfully updated task item with ID $taskId!")
                        showToast(
                            "Successfully updated task item!",
                            Toast.LENGTH_SHORT
                        )
                        finish()
                    } catch (e: Exception) {
                        Log.e(TAG, "Could not update task item", e)
                        showToast(
                            "An error occurred while attempting to update the task item. Please try again later.",
                            Toast.LENGTH_LONG
                        )
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
