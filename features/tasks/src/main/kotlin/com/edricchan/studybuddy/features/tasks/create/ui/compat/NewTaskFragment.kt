package com.edricchan.studybuddy.features.tasks.create.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.auth.navigateToLogin
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.exts.material.picker.setCalendarConstraints
import com.edricchan.studybuddy.exts.material.picker.setSelection
import com.edricchan.studybuddy.exts.material.picker.showMaterialDatePicker
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.compat.utils.TodoUtils
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.databinding.FragNewTaskBinding
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject
import com.edricchan.studybuddy.core.resources.R as CoreResR

@AndroidEntryPoint
class NewTaskFragment : ViewBindingFragment<FragNewTaskBinding>(FragNewTaskBinding::inflate) {
    companion object {
        const val ACTION_NEW_TASK_SHORTCUT = "com.edricchan.studybuddy.shortcuts.ACTION_NEW_TASK"
    }

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    private lateinit var currentUser: FirebaseUser
    private var taskInstant: Instant? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth.currentUser.let {
            if (it == null) {
                showToast("Please sign in before continuing", Toast.LENGTH_SHORT)
                navController.navigateToLogin {
                    popUpTo<CompatDestination.Auth.Login>()
                }
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
                                        .format(getString(CoreResR.string.java_time_format_pattern_default))
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

    override val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_new_item, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
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
                            viewLifecycleOwner.lifecycleScope.launch {
                                try {
                                    TodoUtils(auth, firestore).addTask(taskItem)
                                        .await()
                                    showToast(
                                        "Successfully added task!",
                                        Toast.LENGTH_SHORT
                                    )
                                    navController.popBackStack()
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
                            showSnackBar(
                                "Some errors occurred while attempting to submit the form.",
                                SnackBarData.Duration.Long
                            )
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }
}
