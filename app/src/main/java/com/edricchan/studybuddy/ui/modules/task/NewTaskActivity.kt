package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.ActivityNewTaskBinding
import com.edricchan.studybuddy.extensions.*
import com.edricchan.studybuddy.interfaces.TodoItem
import com.edricchan.studybuddy.interfaces.TodoProject
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.base.BaseActivity
import com.edricchan.studybuddy.ui.modules.task.adapter.TodoProjectDropdownAdapter
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * Created by edricchan on 8/3/18.
 */
class NewTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityNewTaskBinding
    private lateinit var firestore: FirebaseFirestore
    private var allowAccess: Boolean = false
    private var currentUser: FirebaseUser? = null
    private var taskDate: Date? = null
    private var tempTaskProject: String? = null
    private var todoProjectDropdownAdapter: TodoProjectDropdownAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        firestore = Firebase.firestore
        currentUser = auth.currentUser
        allowAccess = if (auth.currentUser == null) {
            showToast("Please sign in before continuing", Toast.LENGTH_SHORT)
            finish()
            startActivity<LoginActivity>()
            false
        } else {
            true
        }

        binding.apply {

            // TODO: Migrate logic to separate component
            taskDueDateChip.setOnClickListener {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                val constraints = CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .setStart(calendar.timeInMillis)
                    .build()
                val picker = MaterialDatePicker.Builder.datePicker().apply {
                    setCalendarConstraints(constraints)
                    if (taskDate != null) {
                        // Set the initial selection
                        setSelection(taskDate?.time)
                    }
                }.build()
                picker.addOnPositiveButtonClickListener { selection ->
                    if (selection != null) {
                        taskDate = Date(selection)
                        // Produces <day name>, <month> <day>
                        taskDueDateChip.text =
                            selection.toDateFormat(getString(R.string.date_format_pattern))
                        // Allow due date to be reset
                        taskDueDateChip.isCloseIconVisible = true
                    }
                }
                picker.show(supportFragmentManager, "taskDueDatePicker")
            }
            taskDueDateChip.setOnCloseIconClickListener {
                // Reset due date
                taskDate = null

                // Reset chip's state
                taskDueDateChip.isCloseIconVisible = false
                taskDueDateChip.setText(R.string.select_date_chip_default_text)
            }

            val projectArrayList = ArrayList<TodoProject>()
            todoProjectDropdownAdapter =
                TodoProjectDropdownAdapter(this@NewTaskActivity, R.layout.dropdown_menu_popup_item, projectArrayList)
            taskProjectAutocompleteTextView.setAdapter(todoProjectDropdownAdapter)
            if (currentUser != null) {
                firestore.collection("users/" + currentUser!!.uid + "/todoProjects")
                    .addSnapshotListener { documentSnapshots, e ->
                        if (e != null) {
                            Log.e(TAG, "An error occurred while listening to changes:", e)
                            return@addSnapshotListener
                        }

                        projectArrayList.clear()
                        projectArrayList.add(TodoProject.build {
                            id = PROJECT_NONE_ID
                            name = getString(R.string.task_project_none)
                        })

                        projectArrayList.add(TodoProject.build {
                            id = PROJECT_CREATE_ID
                            name = getString(R.string.task_project_create)
                        })

                        documentSnapshots?.mapTo(projectArrayList) { it.toObject() }

                        projectArrayList.add(TodoProject.build {
                            id = PROJECT_CHOOSE_ID
                            name = getString(R.string.task_project_prompt)
                        })

                        Log.d(TAG, "Array list: ${projectArrayList.joinToString()}")

                        todoProjectDropdownAdapter?.notifyDataSetChanged()
                    }
                taskProjectAutocompleteTextView.setSelection(todoProjectDropdownAdapter!!.count)
                taskProjectAutocompleteTextView.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            Log.d(
                                TAG,
                                "Task project ID: " + todoProjectDropdownAdapter!!.getTaskProjectId(
                                    position
                                )!!
                            )
                            Log.d(
                                TAG,
                                "Task project name: " + Objects.requireNonNull<TodoProject>(
                                    todoProjectDropdownAdapter!!.getItem(position)
                                ).name!!
                            )
                            when (todoProjectDropdownAdapter!!.getTaskProjectId(position)) {
                                PROJECT_CREATE_ID -> {
                                    val editTextDialogView =
                                        layoutInflater.inflate(R.layout.edit_text_dialog, null)
                                    val textInputLayout =
                                        editTextDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
                                    textInputLayout.editText?.hint = "Project name"
                                    val builder = MaterialAlertDialogBuilder(this@NewTaskActivity)
                                    builder.setTitle("New project")
                                        .setView(editTextDialogView)
                                        .setPositiveButton(R.string.dialog_action_create) { dialog, which ->
                                            val project = TodoProject.build {
                                                name = textInputLayout.editTextStrValue!!
                                            }
                                            firestore.collection("users/" + currentUser!!.uid + "/todoProjects")
                                                .add(project)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        showToast(
                                                            "Successfully created project!",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        dialog.dismiss()
                                                    } else {
                                                        showToast(
                                                            "An error occurred while attempting to create the project. Try again later.",
                                                            Toast.LENGTH_LONG
                                                        )
                                                        Log.e(
                                                            TAG,
                                                            "An error occurred while attempting to create the project:",
                                                            task.exception
                                                        )
                                                    }
                                                }
                                        }
                                        .setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
                                        .show()
                                }
                                PROJECT_NONE_ID -> {
                                    Log.d(TAG, "Selected no project!")
                                }
                                PROJECT_CHOOSE_ID -> {
                                    Log.d(TAG, "Selected choose project!")
                                }
                                else -> {
                                    tempTaskProject = projectArrayList[position].id
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            tempTaskProject = null
                        }
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
                onBackPressed()
                return true
            }
            R.id.action_submit -> {
                if (allowAccess) {
                    binding.apply {
                        if (taskTitleTextInputLayout.editText!!.length() != 0) {
                            taskTitleTextInputLayout.isErrorEnabled = false
                            val taskItem = TodoItem.build {
                                title = taskTitleTextInputLayout.editTextStrValue
                                if (taskContentTextInputLayout.editTextStrValue.isNotEmpty()) {
                                    content = taskContentTextInputLayout.editTextStrValue
                                }
                                if (taskTagsTextInputLayout.editTextStrValue.isNotEmpty()
                                ) {
                                    tags = taskTagsTextInputLayout.editTextStrValue.split(",")
                                        .map { it.trim() }.toMutableList()
                                }
                                if (taskDate != null) {
                                    dueDate = Timestamp(taskDate!!)
                                }
                                if (tempTaskProject != null) {
                                    project =
                                        firestore.document("users/${currentUser?.uid}/todoProjects/$tempTaskProject")
                                }
                                done = taskIsDoneCheckbox.isChecked
                            }
                            TodoUtils.getInstance(auth, firestore).addTask(taskItem)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showToast(
                                            "Successfully added task!",
                                            Toast.LENGTH_SHORT
                                        )
                                        finish()
                                    } else {
                                        Log.e(
                                            TAG,
                                            "An error occurred while adding the task:",
                                            task.exception
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
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val PROJECT_CHOOSE_ID = "CHOOSE"
        private const val PROJECT_CREATE_ID = "PLUS"
        private const val PROJECT_NONE_ID = "NONE"
    }
}
