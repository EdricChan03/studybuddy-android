package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityNewTaskBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.android.startActivity
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.exts.material.picker.setCalendarConstraints
import com.edricchan.studybuddy.exts.material.picker.setSelection
import com.edricchan.studybuddy.exts.material.picker.showMaterialDatePicker
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.auth.ui.LoginActivity
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.common.BaseActivity
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant

/**
 * Created by edricchan on 8/3/18.
 */
class NewTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityNewTaskBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser
    private var taskInstant: Instant? = null

    override val isEdgeToEdgeEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewTaskBinding.inflate(layoutInflater).apply {
            setSupportActionBar(toolbar)
        }.also { setContentView(it.root) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = Firebase.auth
        firestore = Firebase.firestore
        auth.currentUser.let {
            if (it == null) {
                showToast("Please sign in before continuing", Toast.LENGTH_SHORT)
                finish()
                startActivity<LoginActivity>()
                return
            }
            currentUser = it
        }

        binding.apply {
            // TODO: Migrate logic to separate component
            taskDueDateChip.apply {
                setOnClickListener {
                    showMaterialDatePicker(
                        "taskDueDatePicker",
                        builderInit = {
                            setCalendarConstraints(
                                start = Instant.now(),
                                validator = DateValidatorPointForward.now()
                            )
                            taskInstant?.let {
                                // Set the initial selection
                                setSelection(it)
                            }
                        },
                        pickerInit = {
                            addOnPositiveButtonClickListener { selection ->
                                Instant.ofEpochMilli(selection).let {
                                    taskInstant = it
                                    // Produces <day name>, <month> <year>
                                    // We need to convert it to a LocalDateTime as Instants don't support
                                    // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                                    // for more info
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_new_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }

            R.id.action_submit -> {
                binding.apply {
                    if (taskTitleTextInputLayout.editTextStrValue.isNotBlank()) {
                        taskTitleTextInputLayout.isErrorEnabled = false
                        val taskItem = TodoItem.build {
                            title = taskTitleTextInputLayout.editTextStrValue
                            if (taskContentTextInputLayout.editTextStrValue.isNotBlank()) {
                                content = taskContentTextInputLayout.editTextStrValue
                            }
                            if (taskTagsTextInputLayout.editTextStrValue.isNotBlank()) {
                                tags = taskTagsTextInputLayout.editTextStrValue.split(",")
                                    .map { it.trim() }
                            }
                            taskInstant?.toTimestamp()?.let { dueDate = it }
                            done = taskIsDoneCheckbox.isChecked
                        }
                        lifecycleScope.launch {
                            try {
                                TodoUtils(auth, firestore).addTask(taskItem)
                                    .await()
                                showToast(
                                    "Successfully added task!",
                                    Toast.LENGTH_SHORT
                                )
                                finish()
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "An error occurred while adding the task:",
                                    e
                                )
                                showToast(
                                    "An error occurred while adding the task. Try again later.",
                                    Toast.LENGTH_LONG
                                )
                            }
                        }
                    } else {
                        taskTitleTextInputLayout.error = "Please enter something."
                        showSnackbar(
                            coordinatorLayoutNewTask,
                            "Some errors occurred while attempting to submit the form.",
                            Snackbar.LENGTH_LONG
                        )
                    }
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
