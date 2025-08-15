package com.edricchan.studybuddy.features.tasks.edit.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.exts.android.showToast
import com.edricchan.studybuddy.exts.datetime.format
import com.edricchan.studybuddy.exts.datetime.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toLocalDateTime
import com.edricchan.studybuddy.exts.firebase.toTimestamp
import com.edricchan.studybuddy.exts.material.picker.setCalendarConstraints
import com.edricchan.studybuddy.exts.material.picker.setSelection
import com.edricchan.studybuddy.exts.material.picker.setStart
import com.edricchan.studybuddy.exts.material.picker.showMaterialDatePicker
import com.edricchan.studybuddy.exts.material.textfield.editTextStrValue
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.databinding.FragEditTaskBinding
import com.edricchan.studybuddy.features.tasks.edit.vm.EditTaskViewModel
import com.edricchan.studybuddy.features.tasks.edit.vm.EditTaskViewModel.TaskState
import com.edricchan.studybuddy.ui.common.fragment.ViewBindingFragment
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import com.edricchan.studybuddy.core.resources.R as CoreResR

@AndroidEntryPoint
class EditTaskFragment : ViewBindingFragment<FragEditTaskBinding>(FragEditTaskBinding::inflate) {
    companion object {
        private const val TAG = "EditTaskFragment"
    }

    private val viewModel by viewModels<EditTaskViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

    private var taskInstant: Instant? = null
    private lateinit var todoItem: TodoItem

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (auth.currentUser == null) {
            Log.e(TAG, "User isn't logged in. Exiting...")
            showToast(
                "An error occurred while attempting to retrieve the task item's " +
                    "details. Please try again later.",
                Toast.LENGTH_LONG
            )
            navController.navigateUp()
            return
        }

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
                            taskInstant?.let(::setSelection)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.taskDetail.collect { state ->
                when (state) {
                    TaskState.DoesNotExist -> {
                        Log.w(
                            TAG,
                            "onViewCreated: Attempted to load non-existent task item " +
                                "with ID ${viewModel.taskId}, exiting"
                        )
                        navController.navigateUp()
                        return@collect
                    }

                    TaskState.Loading -> binding.apply {
                        progressBar.isVisible = true
                        scrollView.isVisible = false
                    }

                    is TaskState.Success -> binding.apply {
                        progressBar.isVisible = false
                        scrollView.isVisible = true
                        todoItem = state.item
                        with(todoItem) {
                            title?.let { textInputTitle.editText?.setText(it) }
                            content?.let { textInputContent.editText?.setText(it) }
                            done?.let { checkboxMarkAsDone.isChecked = it }
                            taskInstant = dueDate?.toInstant()
                            dueDate?.let {
                                // We need to convert it to a LocalDateTime as Instants don't support
                                // temporal units bigger than days - see the `Instant#isSupported` Javadocs
                                // for more info
                                taskDueDateChip.text = it.toLocalDateTime()
                                    .format(getString(CoreResR.string.java_time_format_pattern_default))
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
                val taskItemUpdates = buildMap<TodoItem.Field, Any> {
                    binding.also {
                        if (it.textInputTitle.editTextStrValue != todoItem.title) {
                            this[TodoItem.Field.Title] = it.textInputTitle.editTextStrValue
                        }
                        if (it.textInputContent.editTextStrValue != todoItem.content) {
                            this[TodoItem.Field.Content] = it.textInputContent.editTextStrValue
                        }
                        this[TodoItem.Field.IsDone] = it.checkboxMarkAsDone.isChecked
                        this[TodoItem.Field.Tags] = it.textInputTags.editTextStrValue.split(
                            Regex("""\s*,\s*""")
                        ).filter(String::isNotBlank)
                    }
                    taskInstant?.let {
                        if (todoItem.dueDate?.toInstant() != it) {
                            this[TodoItem.Field.DueDate] = it.toTimestamp()
                        }
                        // When taskInstant is set to null, this means that the user
                        // wants to clear the due-date of the item
                    } ?: run {
                        this[TodoItem.Field.DueDate] = FieldValue.delete()
                    }
                }

                viewModel.updateTask(
                    taskItemUpdates,
                    onSuccess = {
                        Log.d(
                            TAG,
                            "Successfully updated task item with ID ${viewModel.taskId}!"
                        )
                        showToast(
                            "Successfully updated task item!",
                            Toast.LENGTH_SHORT
                        )
                        navController.navigateUp()
                    },
                    onFailure = {
                        Log.e(TAG, "Could not update task item", it)
                        showToast(
                            "An error occurred while attempting to update the task item. Please try again later.",
                            Toast.LENGTH_LONG
                        )
                    }
                )
                return true
            }

            return false
        }
    }
}
