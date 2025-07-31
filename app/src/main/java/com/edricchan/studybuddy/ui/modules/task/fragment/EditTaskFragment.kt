package com.edricchan.studybuddy.ui.modules.task.fragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.toRoute
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.databinding.FragEditTaskBinding
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.exts.material.picker.setCalendarConstraints
import com.edricchan.studybuddy.exts.material.picker.setSelection
import com.edricchan.studybuddy.exts.material.picker.setStart
import com.edricchan.studybuddy.exts.material.picker.showMaterialDatePicker
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.tasks.compat.utils.TodoUtils
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant

class EditTaskFragment : ViewBindingFragment<FragEditTaskBinding>(FragEditTaskBinding::inflate) {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String

    private lateinit var todoUtils: TodoUtils

    private var taskInstant: Instant? = null
    private lateinit var todoItem: TodoItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            navController.navigateUp()
            return
        }

        todoUtils = TodoUtils(auth, firestore)

        taskId = runCatching {
            navController.getBackStackEntry<CompatDestination.Task.Edit>()
        }.getOrElse {
            Log.e(TAG, "No task item ID was specified. Exiting...", it)
            showToast(
                R.string.task_item_get_error_text,
                Toast.LENGTH_LONG
            )
            navController.navigateUp()
            return
        }.toRoute<CompatDestination.Task.Edit>().taskId

        taskDocument = todoUtils.getTask(taskId)
        lifecycleScope.launch {
            val taskData = taskDocument.get().await()
            if (!taskData.exists()) {
                Log.e(TAG, "No such task with the ID $taskId exists. Exiting...")
                showToast(
                    R.string.task_item_get_error_text,
                    Toast.LENGTH_LONG
                )
                navController.navigateUp()
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

    override val menuProvider = object : MenuProvider {
        override fun onCreateMenu(
            menu: Menu,
            menuInflater: MenuInflater
        ) {
            menuInflater.inflate(R.menu.menu_edit_task, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.action_save) {
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
                        navController.navigateUp()
                    } catch (e: Exception) {
                        Log.e(TAG, "Could not update task item", e)
                        showToast(
                            "An error occurred while attempting to update the task item. Please try again later.",
                            Toast.LENGTH_LONG
                        )
                    }
                }
                return true
            }

            return false
        }
    }
}
