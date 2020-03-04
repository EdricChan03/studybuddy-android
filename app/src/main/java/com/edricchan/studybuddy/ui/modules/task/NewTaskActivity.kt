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
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.extensions.toDateFormat
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.interfaces.TaskProject
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.task.adapter.TaskProjectDropdownAdapter
import com.edricchan.studybuddy.ui.modules.task.utils.TaskUtils
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_new_task.*
import java.util.*

/**
 * Created by edricchan on 8/3/18.
 */
class NewTaskActivity : AppCompatActivity(R.layout.activity_new_task) {
    private var mTaskDate: Date? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore
    private var mCurrentUser: FirebaseUser? = null
    private var mAllowAccess: Boolean = false
    private var tempTaskProject: String? = null
    private var mTaskProjectDropdownAdapter: TaskProjectDropdownAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()
        mCurrentUser = mAuth.currentUser
        mAllowAccess = if (mAuth.currentUser == null) {
            Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show()
            finish()
            startActivity<LoginActivity>()
            false
        } else {
            true
        }
        /*TooltipCompat.setTooltipText(taskDatePickerBtn, "Open datepicker")
        taskDatePickerBtn.setOnClickListener {
            val c = Calendar.getInstance()
            val dpd = DatePickerDialog(this@NewTaskActivity,
                    { _, year, monthOfYear, dayOfMonth ->
                        val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
                        val datepickerDate = Date(year, monthOfYear, dayOfMonth)
                        taskDatePickerTextView.text = format.format(datepickerDate)
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
            dpd.datePicker.minDate = c.timeInMillis
            dpd.show()
            mTaskDate = SharedUtils.getDateFromDatePicker(dpd.datePicker)
        }*/
        // TODO: Migrate logic to separate component
        taskDueDateChip.setOnClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            val constraints = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .setStart(calendar.timeInMillis)
                .build()
            val picker = MaterialDatePicker.Builder.datePicker().apply {
                setCalendarConstraints(constraints)
                if (mTaskDate != null) {
                    // Set the initial selection
                    setSelection(mTaskDate?.time)
                }
            }.build()
            picker.addOnPositiveButtonClickListener { selection ->
                if (selection != null) {
                    mTaskDate = Date(selection)
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
            mTaskDate = null

            // Reset chip's state
            taskDueDateChip.isCloseIconVisible = false
            taskDueDateChip.setText(R.string.select_date_chip_default_text)
        }

        val projectArrayList = ArrayList<TaskProject>()
        mTaskProjectDropdownAdapter =
            TaskProjectDropdownAdapter(this, R.layout.dropdown_menu_popup_item, projectArrayList)
        taskProjectAutocompleteTextView.setAdapter(mTaskProjectDropdownAdapter)
        if (mCurrentUser != null) {
            mFirestore.collection("users/" + mCurrentUser!!.uid + "/todoProjects")
                .addSnapshotListener { documentSnapshots, e ->
                    if (e != null) {
                        Log.e(TAG, "An error occurred while listening to changes:", e)
                        return@addSnapshotListener
                    }

                    projectArrayList.clear()
                    projectArrayList.add(TaskProject.build {
                        id = PROJECT_NONE_ID
                        name = getString(R.string.task_project_none)
                    })

                    projectArrayList.add(TaskProject.build {
                        id = PROJECT_CREATE_ID
                        name = getString(R.string.task_project_create)
                    })

                    documentSnapshots?.mapTo(projectArrayList) { it.toObject() }

                    projectArrayList.add(TaskProject.build {
                        id = PROJECT_CHOOSE_ID
                        name = getString(R.string.task_project_prompt)
                    })

                    Log.d(TAG, "Array list: ${projectArrayList.joinToString()}")

                    mTaskProjectDropdownAdapter?.notifyDataSetChanged()
                }
            taskProjectAutocompleteTextView.setSelection(mTaskProjectDropdownAdapter!!.count)
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
                            "Task project ID: " + mTaskProjectDropdownAdapter!!.getTaskProjectId(
                                position
                            )!!
                        )
                        Log.d(
                            TAG,
                            "Task project name: " + Objects.requireNonNull<TaskProject>(
                                mTaskProjectDropdownAdapter!!.getItem(position)
                            ).name!!
                        )
                        when (mTaskProjectDropdownAdapter!!.getTaskProjectId(position)) {
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
                                        val project = TaskProject.build {
                                            name = textInputLayout.editTextStrValue!!
                                        }
                                        mFirestore.collection("users/" + mCurrentUser!!.uid + "/todoProjects")
                                            .add(project)
                                            .addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        this@NewTaskActivity,
                                                        "Successfully created project!",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    dialog.dismiss()
                                                } else {
                                                    Toast.makeText(
                                                        this@NewTaskActivity,
                                                        "An error occurred while attempting to create the project. Try again later.",
                                                        Toast.LENGTH_LONG
                                                    )
                                                        .show()
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
                if (mAllowAccess) {
                    if (taskTitleTextInputLayout.editText!!.length() != 0) {
                        taskTitleTextInputLayout.isErrorEnabled = false
                        val taskItem = TaskItem.build {
                            title = taskTitleTextInputLayout.editTextStrValue
                            if (taskContentTextInputLayout.editTextStrValue.isNotEmpty()) {
                                content = taskContentTextInputLayout.editTextStrValue
                            }
                            if (taskTagsTextInputLayout?.editTextStrValue != null &&
                                taskTagsTextInputLayout?.editTextStrValue!!.isNotEmpty()
                            ) {
                                tags = taskTagsTextInputLayout?.editTextStrValue?.split(",")
                                    ?.map { it.trim() }?.toMutableList()
                            }
                            if (mTaskDate != null) {
                                dueDate = Timestamp(mTaskDate!!)
                            }
                            if (tempTaskProject != null) {
                                project =
                                    mFirestore.document("users/${mCurrentUser?.uid}/todoProjects/$tempTaskProject")
                            }
                            done = taskIsDoneCheckbox.isChecked
                        }
                        TaskUtils.getInstance(mAuth, mFirestore).addTask(taskItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Successfully added task!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Log.e(
                                        TAG,
                                        "An error occurred while adding the task:",
                                        task.exception
                                    )
                                    Toast.makeText(
                                        this,
                                        "An error occurred while adding the task. Try again later.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        taskTitleTextInputLayout.error = "Please enter something."
                        Snackbar.make(
                            findViewById(R.id.coordinatorLayout),
                            "Some errors occurred while attempting to submit the form.",
                            Snackbar.LENGTH_LONG
                        ).show()
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
