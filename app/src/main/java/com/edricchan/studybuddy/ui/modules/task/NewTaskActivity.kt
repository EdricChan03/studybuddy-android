package com.edricchan.studybuddy.ui.modules.task

import android.app.DatePickerDialog
import android.content.Intent
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
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.interfaces.TaskProject
import com.edricchan.studybuddy.ui.adapter.TaskProjectSpinnerAdapter
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_new_task.*
import java.text.SimpleDateFormat
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
	private val TAG = SharedUtils.getTag(this::class.java)
	private var tempTaskProject: String? = null
	private var mTaskProjectSpinnerAdapter: TaskProjectSpinnerAdapter? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mAuth = FirebaseAuth.getInstance()
		mFirestore = FirebaseFirestore.getInstance()
		mCurrentUser = mAuth.currentUser
		if (mAuth.currentUser == null) {
			Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show()
			val signInIntent = Intent(this@NewTaskActivity, LoginActivity::class.java)
			finish()
			startActivity(signInIntent)
			mAllowAccess = false
		} else {
			mAllowAccess = true
		}
		TooltipCompat.setTooltipText(taskDatePickerBtn, "Open datepicker")
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
		}

		val projectArrayList = ArrayList<TaskProject>()
		mTaskProjectSpinnerAdapter = TaskProjectSpinnerAdapter(this, android.R.layout.simple_spinner_item, projectArrayList)
		mTaskProjectSpinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		taskProjectSpinner.adapter = mTaskProjectSpinnerAdapter
		if (mCurrentUser != null) {
			mFirestore.collection("users/" + mCurrentUser!!.uid + "/todoProjects")
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
								projectArrayList.add(document.toObject<TaskProject>())
							}
						}
						val chooseProjectBuilder = TaskProject.Builder()
						chooseProjectBuilder
								.setId("CHOOSE")
								.setName(getString(R.string.task_project_prompt))
						projectArrayList.add(chooseProjectBuilder.create())
						mTaskProjectSpinnerAdapter!!.notifyDataSetChanged()
					}
			taskProjectSpinner.setSelection(mTaskProjectSpinnerAdapter!!.count)
			taskProjectSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
					Log.d(TAG, "Task project ID: " + mTaskProjectSpinnerAdapter!!.getTaskProjectId(position)!!)
					Log.d(TAG, "Task project name: " + Objects.requireNonNull<TaskProject>(mTaskProjectSpinnerAdapter!!.getItem(position)).name!!)
					if (mTaskProjectSpinnerAdapter!!.getTaskProjectId(position) == "PLUS") {
						val editTextDialogView = layoutInflater.inflate(R.layout.edit_text_dialog, null)
						val textInputLayout = editTextDialogView.findViewById<TextInputLayout>(R.id.textInputLayout)
						textInputLayout.editText!!.hint = "Project name"
						val builder = MaterialAlertDialogBuilder(this@NewTaskActivity)
						builder.setTitle("New project")
								.setView(editTextDialogView)
								.setPositiveButton(R.string.dialog_action_create) { dialog, which ->
									val project = TaskProject.Builder()
									project.setName(textInputLayout.editTextStrValue!!)
									mFirestore.collection("users/" + mCurrentUser!!.uid + "/todoProjects")
											.add(project.create())
											.addOnCompleteListener { task ->
												if (task.isSuccessful) {
													Toast.makeText(this@NewTaskActivity, "Successfully created project!", Toast.LENGTH_SHORT)
															.show()
													dialog.dismiss()
												} else {
													Toast.makeText(this@NewTaskActivity, "An error occurred while attempting to create the project. Try again later.", Toast.LENGTH_LONG)
															.show()
													Log.e(TAG, "An error occurred while attempting to create the project:", task.exception)
												}
											}
								}
								.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
								.show()
					} else {
						val (_, id1) = taskProjectSpinner.getItemAtPosition(taskProjectSpinner.selectedItemPosition) as TaskProject
						tempTaskProject = id1
					}
				}

				override fun onNothingSelected(parent: AdapterView<*>) {
					tempTaskProject = null
				}
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_new_task, menu)
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
						val taskItemBuilder = TaskItem.Builder()
						taskItemBuilder.setTitle(taskTitleTextInputLayout.editTextStrValue!!)
						if (taskContentTextInputLayout.editTextStrValue!!.isNotEmpty()) {
							taskItemBuilder.setContent(taskContentTextInputLayout.editTextStrValue!!)
						}
						if (taskTagsTextInputLayout.editTextStrValue!!.isNotEmpty()) {
							taskItemBuilder.setTags(taskTagsTextInputLayout.editTextStrValue!!.split(",").map { it.trim() }.toMutableList())
						}
						if (mTaskDate != null) {
							taskItemBuilder.setDueDate(Timestamp(mTaskDate!!))
						}
						if (tempTaskProject != null) {
							taskItemBuilder.setProject(mFirestore.document("users/" + mCurrentUser!!.uid + "/todoProjects/" + tempTaskProject))
						}
						taskItemBuilder.setIsDone(taskIsDoneCheckbox.isChecked)
						SharedUtils.addTask(taskItemBuilder.create(), mCurrentUser!!, mFirestore)
								.addOnCompleteListener { task ->
									if (task.isSuccessful) {
										Toast.makeText(this, "Successfully added task!", Toast.LENGTH_SHORT).show()
										finish()
									} else {
										Log.e(TAG, "An error occurred while adding the task:", task.exception)
										Toast.makeText(this, "An error occurred while adding the task. Try again later.", Toast.LENGTH_LONG).show()
									}
								}
					} else {
						taskTitleTextInputLayout.error = "Please enter something."
						Snackbar.make(findViewById(R.id.newTaskCooardinatorLayout), "Some errors occurred while attempting to submit the form.", Snackbar.LENGTH_LONG).show()
					}
				}
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}
