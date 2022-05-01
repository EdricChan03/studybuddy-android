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
import com.edricchan.studybuddy.databinding.ActivityEditTaskBinding
import com.edricchan.studybuddy.extensions.*
import com.edricchan.studybuddy.extensions.firebase.firestore.toObjectWithId
import com.edricchan.studybuddy.interfaces.TodoItem
import com.edricchan.studybuddy.interfaces.TodoProject
import com.edricchan.studybuddy.ui.modules.base.BaseActivity
import com.edricchan.studybuddy.ui.modules.task.adapter.TodoProjectDropdownAdapter
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

class EditTaskActivity : BaseActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskDocument: DocumentReference
    private lateinit var taskId: String
    private lateinit var todoProjectDropdownAdapter: TodoProjectDropdownAdapter
    private lateinit var todoUtils: TodoUtils
    private var currentUser: FirebaseUser? = null
    private var taskDate: Date? = null
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
                        taskDate = Date(selection)
                        // Produces <day name>, <month> <year>
                        taskDueDateChip.text =
                            selection.toDateFormat(getString(R.string.date_format_pattern))
                        // Allow due date to be reset
                        taskDueDateChip.isCloseIconVisible = true
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
                var tempTaskProject: String? = null
                val projectArrayList = ArrayList<TodoProject>()
                todoProjectDropdownAdapter = TodoProjectDropdownAdapter(
                    this@EditTaskActivity,
                    android.R.layout.simple_spinner_item,
                    projectArrayList
                )
                todoProjectDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerProject.adapter = todoProjectDropdownAdapter
                firestore.collection("users/${currentUser?.uid}/todoProjects")
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
                        projectArrayList.add(TodoProject.build {
                            id = PROJECT_CHOOSE_ID
                            name = getString(R.string.task_project_prompt)
                        })
                        documentSnapshots?.mapTo(projectArrayList) { it.toObjectWithId() }
                        todoProjectDropdownAdapter.notifyDataSetChanged()
                    }
                spinnerProject.setSelection(todoProjectDropdownAdapter.count)
                spinnerProject.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View,
                            position: Int,
                            id: Long
                        ) {
                            if (todoProjectDropdownAdapter.getTaskProjectId(position) != null) {
                                Log.d(
                                    TAG,
                                    "Task project ID: " + todoProjectDropdownAdapter.getTaskProjectId(
                                        position
                                    )
                                )
                            }
                            Log.d(
                                TAG,
                                "Task project name: " + todoProjectDropdownAdapter.getItem(position)?.name
                            )
                            when (todoProjectDropdownAdapter.getTaskProjectId(position)) {
                                PROJECT_CREATE_ID -> {
                                    val editTextDialogView =
                                        layoutInflater.inflate(R.layout.edit_text_dialog, null)
                                    val textInputLayout =
                                        editTextDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
                                    textInputLayout.editText?.hint = "Project name"
                                    val builder = MaterialAlertDialogBuilder(this@EditTaskActivity)
                                    builder.setTitle("New project")
                                        .setView(editTextDialogView)
                                        .setPositiveButton(R.string.dialog_action_create) { dialog, which ->
                                            val project = TodoProject.build {
                                                name = textInputLayout.editTextStrValue
                                            }
                                            firestore.collection("users/${currentUser?.uid}/todoProjects")
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
                                                            "An error occurred while attempting to create the project." +
                                                                    "Try again later.",
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
                                    val (_, id1) = spinnerProject.getItemAtPosition(spinnerProject.selectedItemPosition) as TodoProject
                                    tempTaskProject = id1
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            tempTaskProject = null
                        }
                    }
                progressBar.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
                taskDocument
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                progressBar.visibility = View.GONE
                                scrollView.visibility = View.VISIBLE
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
                                if (todoItem?.dueDate != null) {
                                    val date = Date(todoItem!!.dueDate!!.toDate().time)
                                    taskDueDateChip.text =
                                        date.toDateFormat(getString(R.string.date_format_pattern))
                                    // Allow due date to be reset
                                    taskDueDateChip.isCloseIconVisible = true
                                }
                                if (todoItem?.project != null) {
                                    spinnerProject!!.setSelection(
                                        todoProjectDropdownAdapter.getPosition(
                                            todoProjectDropdownAdapter.getTaskProjectById(todoItem!!.project!!.id)
                                        )
                                    )
                                }
                                if (todoItem?.tags != null) {
                                    textInputTags.editText?.setText(
                                        todoItem!!.tags!!.toMutableList().joinToString(
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
                onBackPressed()
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
                        if (spinnerProject.selectedItem != null) {
                            // taskItemUpdates["project"] = mFirestore.document("users/${mCurrentUser?.uid}/todoProjects/${spinnerProject.selectedItem}")
                            Log.d(TAG, "Selected item: ${spinnerProject.selectedItem}")
                        }
                    }
                    if (taskDate != null && todoItem!!.dueDate!! != taskDate!!.toTimestamp()) {
                        taskItemUpdates["dueDate"] = taskDate!!.toTimestamp()
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
        private const val PROJECT_CHOOSE_ID = "CHOOSE"
        private const val PROJECT_CREATE_ID = "PLUS"
        private const val PROJECT_NONE_ID = "NONE"
        /** The extra tag for the task's ID. */
        const val EXTRA_TASK_ID = "taskId"
    }
}
