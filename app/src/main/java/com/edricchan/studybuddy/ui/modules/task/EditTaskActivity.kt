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
import com.edricchan.studybuddy.extensions.firebase.firestore.toObjectWithId
import com.edricchan.studybuddy.extensions.toDateFormat
import com.edricchan.studybuddy.extensions.toTimestamp
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.interfaces.TaskProject
import com.edricchan.studybuddy.ui.modules.task.adapter.TaskProjectDropdownAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_edit_task.*
import java.util.*
import kotlin.collections.HashMap

class EditTaskActivity : AppCompatActivity(R.layout.activity_edit_task) {
	private lateinit var mFirestore: FirebaseFirestore
	private lateinit var mAuth: FirebaseAuth
	private var mCurrentUser: FirebaseUser? = null
	private var mTaskId: String? = null
	private var mTaskDate: Date? = null
	private var mTaskItem: TaskItem? = null
	private lateinit var mTaskProjectDropdownAdapter: TaskProjectDropdownAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mFirestore = FirebaseFirestore.getInstance()
		mAuth = FirebaseAuth.getInstance()
		mCurrentUser = mAuth.currentUser
		mTaskId = intent.getStringExtra("taskId")
		if (mTaskId == null) {
			Log.e(TAG, "Please specify a task item ID!")
			Toast.makeText(this, "An error occurred while attempting to retrieve the task item's details. Please try again later.", Toast.LENGTH_LONG)
					.show()
			finish()
		} else {
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
					mTaskDate = Date(selection)
					// Produces <day name>, <month> <year>
					taskDueDateChip.text = selection.toDateFormat(getString(R.string.date_format_pattern))
					// Allow due date to be reset
					taskDueDateChip.isCloseIconVisible = true
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
			var tempTaskProject: String? = null
			val projectArrayList = ArrayList<TaskProject>()
			mTaskProjectDropdownAdapter = TaskProjectDropdownAdapter(this, android.R.layout.simple_spinner_item, projectArrayList)
			mTaskProjectDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			spinnerProject.adapter = mTaskProjectDropdownAdapter
			mFirestore.collection("users/${mCurrentUser?.uid}/todoProjects")
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
						projectArrayList.add(TaskProject.build {
							id = PROJECT_CHOOSE_ID
							name = getString(R.string.task_project_prompt)
						})
						documentSnapshots?.mapTo(projectArrayList) { it.toObjectWithId() }
						mTaskProjectDropdownAdapter.notifyDataSetChanged()
					}
			spinnerProject.setSelection(mTaskProjectDropdownAdapter.count)
			spinnerProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
					if (mTaskProjectDropdownAdapter.getTaskProjectId(position) != null) {
						Log.d(TAG, "Task project ID: " + mTaskProjectDropdownAdapter.getTaskProjectId(position))
					}
					Log.d(TAG, "Task project name: " + mTaskProjectDropdownAdapter.getItem(position)?.name)
					when (mTaskProjectDropdownAdapter.getTaskProjectId(position)) {
						PROJECT_CREATE_ID -> {
							val editTextDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
							val textInputLayout = editTextDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
							textInputLayout.editText?.hint = "Project name"
							val builder = MaterialAlertDialogBuilder(this@EditTaskActivity)
							builder.setTitle("New project")
									.setView(editTextDialogView)
									.setPositiveButton(R.string.dialog_action_create) { dialog, which ->
										val project = TaskProject.build {
											name = textInputLayout.editTextStrValue!!
										}
										mFirestore.collection("users/${mCurrentUser?.uid}/todoProjects")
												.add(project)
												.addOnCompleteListener { task ->
													if (task.isSuccessful) {
														Toast.makeText(this@EditTaskActivity, "Successfully created project!", Toast.LENGTH_SHORT)
																.show()
														dialog.dismiss()
													} else {
														Toast.makeText(this@EditTaskActivity, "An error occurred while attempting to create the project." +
																"Try again later.", Toast.LENGTH_LONG)
																.show()
														Log.e(TAG, "An error occurred while attempting to create the project:", task.exception)
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
							val (_, id1) = spinnerProject.getItemAtPosition(spinnerProject.selectedItemPosition) as TaskProject
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
			mTaskId = intent.getStringExtra("taskId")
			mFirestore.document("users/${mCurrentUser?.uid}/todos/$mTaskId")
					.get()
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							val document = task.result
							if (document != null && document.exists()) {
								progressBar.visibility = View.GONE
								scrollView.visibility = View.VISIBLE
								mTaskItem = document.toObject<TaskItem>()
								if (mTaskItem?.title != null) {
									textInputTitle.editText?.setText(mTaskItem!!.title)
								}
								if (mTaskItem?.content != null) {
									textInputContent.editText?.setText(mTaskItem!!.content)
								}
								if (mTaskItem?.done != null) {
									checkboxMarkAsDone.isChecked = mTaskItem!!.done!!
								} else {
									checkboxMarkAsDone.isChecked = false
								}
								if (mTaskItem?.dueDate != null) {
									val date = Date(mTaskItem!!.dueDate!!.toDate().time)
//									textSelectedDate.text = format.format(date)
									taskDueDateChip.text = date.toDateFormat(getString(R.string.date_format_pattern))
									// Allow due date to be reset
									taskDueDateChip.isCloseIconVisible = true
								}
								if (mTaskItem?.project != null) {
									spinnerProject!!.setSelection(mTaskProjectDropdownAdapter.getPosition(mTaskProjectDropdownAdapter.getTaskProjectById(mTaskItem!!.project!!.id)))
								}
								if (mTaskItem?.tags != null) {
									textInputTags.editText?.setText(mTaskItem!!.tags!!.toMutableList().joinToString(","))
								}
							} else {
								Log.i(TAG, "Task with ID $mTaskId does not exist!")
								Toast.makeText(this, "Task does not exist!", Toast.LENGTH_LONG).show()
							}
						} else {
							Log.i(TAG, "Could not load task.", task.exception)
							Toast.makeText(this, "An error occurred while loading the task", Toast.LENGTH_LONG).show()
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
				if (mTaskItem != null) {
					if (textInputTitle?.editTextStrValue != null && textInputTitle.editTextStrValue != mTaskItem!!.title) {
						taskItemUpdates["title"] = textInputTitle.editTextStrValue!!
					}
					if (textInputContent?.editTextStrValue != null && textInputContent.editTextStrValue != mTaskItem!!.content) {
						taskItemUpdates["content"] = textInputContent.editTextStrValue!!
					}
					if (spinnerProject.selectedItem != null) {
//						taskItemUpdates["project"] = mFirestore.document("users/${mCurrentUser?.uid}/todoProjects/${spinnerProject.selectedItem}")
						Log.d(TAG, "Selected item: ${spinnerProject.selectedItem}")
					}
					if (mTaskDate != null && mTaskItem!!.dueDate!! != mTaskDate!!.toTimestamp()) {
						taskItemUpdates["dueDate"] = mTaskDate!!.toTimestamp()
					}
					mFirestore.document("users/${mCurrentUser?.uid}/todos/${mTaskId}")
							.update(taskItemUpdates)
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									Log.d(TAG, "Successfully updated task item with ID $mTaskId!")
									Toast.makeText(this, "Successfully updated task item!", Toast.LENGTH_SHORT)
											.show()
									finish()
								} else {
									Toast.makeText(this, "An error occurred while attempting to update the task item. Please try again later.", Toast.LENGTH_LONG)
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
	}
}