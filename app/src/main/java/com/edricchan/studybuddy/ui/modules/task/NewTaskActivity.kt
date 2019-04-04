package com.edricchan.studybuddy.ui.modules.task

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
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
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by edricchan on 8/3/18.
 */
class NewTaskActivity : AppCompatActivity() {
	private var mTaskTitle: TextInputLayout? = null
	private var mTaskContent: TextInputLayout? = null
	private var mTaskTags: TextInputLayout? = null
	private var mTaskProjectSpinner: Spinner? = null
	private var mTaskIsDone: CheckBox? = null
	private var mTaskDate: Date? = null
	private var mAuth: FirebaseAuth? = null
	private var mFirestore: FirebaseFirestore? = null
	private var mCurrentUser: FirebaseUser? = null
	private var mAllowAccess: Boolean = false
	private val TAG = SharedUtils.getTag(this::class.java)
	private var tempTaskProject: String? = null
	private var mTaskProjectSpinnerAdapter: TaskProjectSpinnerAdapter? = null
	/**
	 * Whether the intent is to edit a task
	 */
	private var mIsEditing: Boolean = false

	private fun checkSignedIn(): Boolean {
		return mAuth!!.currentUser != null
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		mAuth = FirebaseAuth.getInstance()
		mFirestore = FirebaseFirestore.getInstance()
		mIsEditing = intent.getStringExtra("taskId") != null
		mCurrentUser = mAuth!!.currentUser
		if (!checkSignedIn()) {
			Toast.makeText(this, "Please sign in before continuing", Toast.LENGTH_SHORT).show()
			val signInIntent = Intent(this@NewTaskActivity, LoginActivity::class.java)
			startActivity(signInIntent)
			mAllowAccess = false
		} else {
			mAllowAccess = true
		}
		mTaskTitle = findViewById(R.id.taskTitle)
		mTaskProjectSpinner = findViewById(R.id.taskProject)
		mTaskTags = findViewById(R.id.taskTags)
		mTaskContent = findViewById(R.id.taskContent)
		mTaskIsDone = findViewById(R.id.taskIsDone)
		val mDialogDate = findViewById<ImageButton>(R.id.taskDatePickerBtn)
		val mDialogDateText = findViewById<TextView>(R.id.taskDatePickerResult)
		TooltipCompat.setTooltipText(mDialogDate, "Open datepicker dialog")
		mDialogDate.setOnClickListener {
			val c = Calendar.getInstance()
			val dpd = DatePickerDialog(this@NewTaskActivity,
					{ _, year, monthOfYear, dayOfMonth ->
						val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
						val datepickerDate = Date(year, monthOfYear, dayOfMonth)
						mDialogDateText.text = format.format(datepickerDate)
					}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
			dpd.datePicker.minDate = c.timeInMillis
			dpd.show()
			mTaskDate = SharedUtils.getDateFromDatePicker(dpd.datePicker)
		}

		val projectArrayList = ArrayList<TaskProject>()
		mTaskProjectSpinnerAdapter = TaskProjectSpinnerAdapter(this, android.R.layout.simple_spinner_item, projectArrayList)
		mTaskProjectSpinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
		mTaskProjectSpinner!!.adapter = mTaskProjectSpinnerAdapter
		mFirestore!!.collection("users/" + mCurrentUser!!.uid + "/todoProjects")
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
					mTaskProjectSpinnerAdapter!!.notifyDataSetChanged()
				}
		mTaskProjectSpinner!!.setSelection(mTaskProjectSpinnerAdapter!!.count)
		mTaskProjectSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
								mFirestore!!.collection("users/" + mCurrentUser!!.uid + "/todoProjects")
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
					val (_, id1) = mTaskProjectSpinner!!.getItemAtPosition(mTaskProjectSpinner!!.selectedItemPosition) as TaskProject
					tempTaskProject = id1
				}
			}

			override fun onNothingSelected(parent: AdapterView<*>) {
				tempTaskProject = null
			}
		}
		if (mIsEditing) {
			Log.w(TAG, "Editing mode is deprecated and will be moved to com.edricchan.studybuddy.ui.modules.task.EditTaskActivity.")
			title = "Edit task"
			findViewById<View>(R.id.progressBar).visibility = View.VISIBLE
			findViewById<View>(R.id.newTaskScrollView).visibility = View.GONE
			val mTaskId = intent.getStringExtra("taskId")
			mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
					.get()
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							val document = task.result
							if (document!!.exists()) {
								findViewById<View>(R.id.progressBar).visibility = View.GONE
								findViewById<View>(R.id.newTaskScrollView).visibility = View.VISIBLE
								val (content, dueDate, isDone, _, project, tags, title) = document.toObject(TaskItem::class.java)!!
								mTaskTitle!!.editText!!.setText(title)
								if (content != null) {
									mTaskContent!!.editText!!.setText(content)
								}
								mTaskIsDone!!.isChecked = isDone!!
								if (dueDate != null) {
									val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
									mTaskDate = Date(dueDate.toDate().time)
									mDialogDateText.text = format.format(mTaskDate)
								}
								if (project != null) {
									mTaskProjectSpinner!!.setSelection(mTaskProjectSpinnerAdapter!!.getPosition(mTaskProjectSpinnerAdapter!!.getTaskProjectById(project.id)))
								}
								if (tags != null) {
									mTaskTags!!.editText!!.setText(TextUtils.join(",", tags))
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

	override fun onPrepareOptionsMenu(menu: Menu): Boolean {
		if (mIsEditing) {
			menu.findItem(R.id.action_submit).title = "Save"
		}
		return true
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
					if (mTaskTitle!!.editText!!.length() != 0) {
						mTaskTitle!!.isErrorEnabled = false
						val taskItemBuilder = TaskItem.Builder()
						taskItemBuilder.setTitle(mTaskTitle!!.editTextStrValue!!)
						if (mTaskContent!!.editTextStrValue!!.isNotEmpty()) {
							taskItemBuilder.setContent(mTaskContent!!.editTextStrValue!!)
						}
						if (mTaskTags!!.editTextStrValue!!.isNotEmpty()) {
							taskItemBuilder.setTags(mTaskTags!!.editTextStrValue!!.split(",").map { it.trim() }.toMutableList())
						}
						if (mTaskDate != null) {
							taskItemBuilder.setDueDate(Timestamp(mTaskDate!!))
						}
						if (tempTaskProject != null) {
							taskItemBuilder.setProject(mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todoProjects/" + tempTaskProject))
						}
						taskItemBuilder.setIsDone(mTaskIsDone!!.isChecked)
						if (mIsEditing) {
							mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + intent.getStringExtra("taskId"))
									.set(taskItemBuilder.create())
									.addOnCompleteListener { task ->
										if (task.isSuccessful) {
											Toast.makeText(this, "Successfully edited the task!", Toast.LENGTH_SHORT).show()
											finish()
										} else {
											Log.e(TAG, "An error occurred while editing the task:", task.exception)
											Toast.makeText(this, "An error occurred while editing the task. Try again later.", Toast.LENGTH_LONG).show()
										}
									}
						} else {
							SharedUtils.addTask(taskItemBuilder.create(), mCurrentUser!!, mFirestore!!)
									.addOnCompleteListener { task ->
										if (task.isSuccessful) {
											Toast.makeText(this, "Successfully added task!", Toast.LENGTH_SHORT).show()
											finish()
										} else {
											Log.e(TAG, "An error occurred while adding the task:", task.exception)
											Toast.makeText(this, "An error occurred while adding the task. Try again later.", Toast.LENGTH_LONG).show()
										}
									}
						}
					} else {
						mTaskTitle!!.error = "Please enter something."
						Snackbar.make(findViewById(R.id.newTaskView), "Some errors occurred while attempting to submit the form.", Snackbar.LENGTH_LONG).show()
					}
				}
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}
}
