package com.edricchan.studybuddy.ui.modules.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.editTextStrValue
import com.edricchan.studybuddy.extensions.toTimestamp
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.interfaces.TaskProject
import com.edricchan.studybuddy.ui.adapter.TaskProjectSpinnerAdapter
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_edit_task.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditTaskActivity : AppCompatActivity(R.layout.activity_edit_task) {
	private lateinit var mFirestore: FirebaseFirestore
	private lateinit var mAuth: FirebaseAuth
	private var mCurrentUser: FirebaseUser? = null
	private var mTaskId: String? = null
	private var mTaskDate: Date? = null
	private var mTaskItem: TaskItem? = null
	private lateinit var mTaskProjectSpinnerAdapter: TaskProjectSpinnerAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mFirestore = FirebaseFirestore.getInstance()
		mAuth = FirebaseAuth.getInstance()
		mCurrentUser = mAuth.currentUser
		mTaskId = intent.getStringExtra("taskId")
		TooltipCompat.setTooltipText(btnSelectDate, "Open datepicker")
		if (mTaskId == null) {
			Log.e(TAG, "Please specify a task item ID!")
			Toast.makeText(this, "An error occurred while attempting to retrieve the task item's details. Please try again later.", Toast.LENGTH_LONG)
					.show()
			finish()
		} else {
			btnSelectDate.setOnClickListener {
				val c = Calendar.getInstance()
				val dpd = DatePickerDialog(this@EditTaskActivity,
						{ _, year, monthOfYear, dayOfMonth ->
							val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
							val datepickerDate = Date(year, monthOfYear, dayOfMonth)
							textSelectedDate.text = format.format(datepickerDate)
						}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
				dpd.datePicker.minDate = c.timeInMillis
				dpd.show()
				mTaskDate = SharedUtils.getDateFromDatePicker(dpd.datePicker)
			}
			var tempTaskProject: String? = null
			val projectArrayList = ArrayList<TaskProject>()
			mTaskProjectSpinnerAdapter = TaskProjectSpinnerAdapter(this, android.R.layout.simple_spinner_item, projectArrayList)
			mTaskProjectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
			spinnerProject.adapter = mTaskProjectSpinnerAdapter
			mFirestore.collection("users/" + mCurrentUser?.uid + "/todoProjects")
					.addSnapshotListener { documentSnapshots, e ->
						if (e != null) {
							Log.e(TAG, "An error occurred while listening to changes:", e)
							return@addSnapshotListener
						}
						projectArrayList.clear()
						val createProjectBuilder = TaskProject.Builder()
						createProjectBuilder
								.setColor("#FFFFFF")
								.setId("PLUS")
								.setName(getString(R.string.task_project_create))
						projectArrayList.add(createProjectBuilder.create())
						if (documentSnapshots != null) {
							for (document in documentSnapshots) {
								projectArrayList.add(document.toObject(TaskProject::class.java))
							}
						}
						val chooseProjectBuilder = TaskProject.Builder()
						chooseProjectBuilder
								.setId("CHOOSE")
								.setName(getString(R.string.task_project_prompt))
						projectArrayList.add(chooseProjectBuilder.create())
						mTaskProjectSpinnerAdapter.notifyDataSetChanged()
					}
			spinnerProject.setSelection(mTaskProjectSpinnerAdapter.count)
			spinnerProject.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
					Log.d(TAG, "Task project ID: ${mTaskProjectSpinnerAdapter.getTaskProjectId(position)}")
					Log.d(TAG, "Task project name: ${mTaskProjectSpinnerAdapter.getItem(position)?.name}")
					if (mTaskProjectSpinnerAdapter.getTaskProjectId(position) == "PLUS") {
						val editTextDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
						val textInputLayout = editTextDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
						textInputLayout.editText?.hint = "Project name"
						val builder = MaterialAlertDialogBuilder(this@EditTaskActivity)
						builder.setTitle("New project")
								.setView(editTextDialogView)
								.setPositiveButton(R.string.dialog_action_create) { dialog, which ->
									val project = TaskProject.Builder()
									project.setName(textInputLayout.editTextStrValue!!)
									mFirestore.collection("users/" + mCurrentUser?.uid + "/todoProjects")
											.add(project.create())
											.addOnCompleteListener { task ->
												if (task.isSuccessful) {
													Toast.makeText(this@EditTaskActivity, "Successfully created project!", Toast.LENGTH_SHORT)
															.show()
													dialog.dismiss()
												} else {
													Toast.makeText(this@EditTaskActivity, "An error occurred while attempting to create the project. Try again later.", Toast.LENGTH_LONG)
															.show()
													Log.e(TAG, "An error occurred while attempting to create the project:", task.exception)
												}
											}
								}
								.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
								.show()
					} else {
						val (_, id1) = spinnerProject!!.getItemAtPosition(spinnerProject!!.selectedItemPosition) as TaskProject
						tempTaskProject = id1
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
								if (mTaskItem?.isDone != null) {
									checkboxMarkAsDone.isChecked = mTaskItem!!.isDone!!
								} else {
									checkboxMarkAsDone.isChecked = false
								}
								if (mTaskItem?.dueDate != null) {
									val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
									val date = Date(mTaskItem!!.dueDate!!.toDate().time)
									textSelectedDate.text = format.format(date)
								}
								if (mTaskItem?.project != null) {
									spinnerProject!!.setSelection(mTaskProjectSpinnerAdapter.getPosition(mTaskProjectSpinnerAdapter.getTaskProjectById(mTaskItem!!.project!!.id)))
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
		menuInflater.inflate(R.menu.menu_new_task, menu)
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
					if (mTaskDate != null && !mTaskItem!!.dueDate!!.equals(Timestamp(mTaskDate))) {
						taskItemUpdates["dueDate"] = mTaskDate.toTimestamp()
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
		private val TAG = SharedUtils.getTag(this::class.java)
	}
}