package com.edricchan.studybuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import ru.noties.markwon.Markwon
import java.text.SimpleDateFormat
import java.util.*

class ViewTaskActivity : AppCompatActivity() {
	private var mAuth: FirebaseAuth? = null
	private var mFirestore: FirebaseFirestore? = null
	private var mCurrentUser: FirebaseUser? = null
	private val TAG = SharedUtils.getTag(this::class.java)
	private var mTaskId: String? = null
	private var mTaskTitle: TextView? = null
	private var mTaskContent: TextView? = null
	private var mTaskDate: TextView? = null
	private var mTaskProject: TextView? = null
	private var mTaskTags: ChipGroup? = null
	private var mTaskTagsParentView: LinearLayout? = null
	private var taskItem: TaskItem? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_view_task)
		supportActionBar!!.setDisplayHomeAsUpEnabled(true)
		val intent = intent
		mTaskId = intent.getStringExtra("taskId")
		mTaskTitle = findViewById(R.id.taskTitle)
		mTaskContent = findViewById(R.id.taskContent)
		mTaskDate = findViewById(R.id.taskDate)
		mTaskTags = findViewById(R.id.taskTags)
		mTaskProject = findViewById(R.id.taskProject)
		mTaskTagsParentView = findViewById(R.id.taskTagsParentView)
	}

	override fun onStart() {
		super.onStart()
		mFirestore = FirebaseFirestore.getInstance()
		mAuth = FirebaseAuth.getInstance()
		mCurrentUser = mAuth!!.currentUser
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in")
			val signInDialogBuilder = MaterialAlertDialogBuilder(this)
			signInDialogBuilder
					.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login) { dialogInterface, _ ->
						val loginIntent = Intent(this, LoginActivity::class.java)
						startActivity(loginIntent)
						dialogInterface.dismiss()
					}
					.setNeutralButton(R.string.dialog_action_sign_up) { dialogInterface, _ ->
						val registerIntent = Intent(this, RegisterActivity::class.java)
						startActivity(registerIntent)
						dialogInterface.dismiss()
					}
					.setNegativeButton(R.string.dialog_action_cancel) { dialogInterface, _ -> dialogInterface.cancel() }
					.show()
		} else {
			loadTask()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		menuInflater.inflate(R.menu.menu_view_task, menu)
		return true
	}

	override fun onPrepareOptionsMenu(menu: Menu): Boolean {
		if (taskItem != null) {
			if (taskItem!!.isDone!!) {
				menu.findItem(R.id.action_mark_as_done)
						.setTitle(R.string.action_mark_as_undone)
			} else {
				menu.findItem(R.id.action_mark_as_done)
						.setTitle(R.string.action_mark_as_done)
			}
		}
		return super.onPrepareOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			R.id.action_delete -> {
				val builder = MaterialAlertDialogBuilder(this)
				builder.setTitle("Delete todo?")
						.setPositiveButton(R.string.dialog_action_ok) { dialog, which ->
							mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
									.delete()
									.addOnCompleteListener { task ->
										if (task.isSuccessful) {
											Toast.makeText(this, "Successfully deleted todo!", Toast.LENGTH_SHORT).show()
											finish()
										} else {
											Toast.makeText(this, "An error occurred while deleting the todo. Try again later.", Toast.LENGTH_LONG).show()
											Log.e(TAG, "An error occurred while deleting the todo.", task.exception)
										}
									}
						}
						.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
						.show()
				return true
			}
			R.id.action_edit -> {
				val editTaskIntent = Intent(this@ViewTaskActivity, NewTaskActivity::class.java)
				editTaskIntent.putExtra("taskId", mTaskId)
				startActivity(editTaskIntent)
				return true
			}
			R.id.action_mark_as_done -> {
				mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
						.update("hasDone", !taskItem!!.isDone!!)
						.addOnCompleteListener { task ->
							if (task.isSuccessful) {
								Snackbar.make(findViewById(R.id.mainView), "Task marked as " + if ((!taskItem!!.isDone!!)) "done" else "undone", Snackbar.LENGTH_SHORT)
										.show()
							} else {
								Toast.makeText(this, "An error occurred while marking the todo as " + if ((!taskItem!!.isDone!!)) "done" else "undone", Toast.LENGTH_LONG)
										.show()
								Log.e(TAG, "An error occurred while marking the todo as " + if ((!taskItem!!.isDone!!)) "done" else "undone", task.exception)
							}
							invalidateOptionsMenu()
						}
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	/**
	 * Loads the task that was supplied via `taskId`
	 */
	private fun loadTask() {
		mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
				.addSnapshotListener { documentSnapshot, e ->
					if (e != null) {
						Log.e(TAG, "An error occurred while retrieving the task:", e)
						Snackbar.make(findViewById(R.id.mainView), R.string.view_task_unsuccessful_snackbar_text, Snackbar.LENGTH_LONG)
								.show()
					} else {
						if (documentSnapshot != null && documentSnapshot.exists()) {
							setViews(Objects.requireNonNull<TaskItem>(documentSnapshot.toObject(TaskItem::class.java)))
							taskItem = documentSnapshot.toObject(TaskItem::class.java)
						}
					}
				}
		/*.addOnCompleteListener(task -> {
					if (task.isSuccessful()) {
						DocumentSnapshot document = task.getResult();
						if (document.exists()) {
							mItem = document.toObject(TaskItem.class);
							findViewById(R.id.progressBar).setVisibility(View.GONE);
							findViewById(R.id.taskView).setVisibility(View.VISIBLE);
							mTaskTitle.setText(mItem.title);
							if (mItem.content != null) {
								mTaskContent.setText(mItem.content);
							}
						} else {
							// TODO: Add handler for document not existing
						}
					} else {
						Snackbar.make(findViewById(R.id.mainView), R.string.view_task_unsuccessful_snackbar_text, Snackbar.LENGTH_INDEFINITE)
								.setBehavior(new NoSwipeBehavior())
								.setAction(R.string.view_task_unsuccessful_snackbar_btn_text, v -> loadTask());
					}
				});*/
	}

	/**
	 * Toggles the visiblity of a view to `visibility`
	 *
	 * @param view              The view to toggle the visibility of
	 * @param initialVisibility The visibility to check against the view. Can be [View.GONE], [View.INVISIBLE] or [View.VISIBLE]
	 * @param visibility        The visibility to toggle the view to. Can be [View.GONE], [View.INVISIBLE] or [View.VISIBLE]
	 */
	private fun toggleViewVisibility(view: View, initialVisibility: Int, visibility: Int) {
		if (view.visibility == initialVisibility) {
			view.visibility = visibility
		}
	}

	/**
	 * Sets all of the [TextView] and [com.google.android.material.chip.ChipGroup] values
	 *
	 * @param item The task item
	 */
	private fun setViews(item: TaskItem) {
		if (item.content != null) {
			Markwon.setMarkdown(mTaskContent!!, item.content!!)
			toggleViewVisibility(mTaskContent!!, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(mTaskContent!!, View.VISIBLE, View.GONE)
		}
		if (item.dueDate != null) {
			val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
			mTaskDate!!.text = format.format(item.dueDate!!.toDate())
			toggleViewVisibility(mTaskDate!!, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(mTaskDate!!, View.VISIBLE, View.GONE)
		}
		if (item.project != null) {
			item.project!!
					.get()
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							mTaskProject!!.text = task.result!!.getString("name")
						} else {
							Toast.makeText(this, "An error occurred while attempting to retrieve the project. Please try again later.", Toast.LENGTH_SHORT).show()
							Log.e(TAG, "An error occurred while attempting to retrieve the project:", task.exception)
						}
					}
			toggleViewVisibility(mTaskProject!!, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(mTaskProject!!, View.VISIBLE, View.GONE)
		}
		if (item.title != null) {
			mTaskTitle!!.text = item.title
			toggleViewVisibility(mTaskTitle!!, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(mTaskTitle!!, View.VISIBLE, View.GONE)
		}
		if (item.tags != null && !item.tags!!.isEmpty()) {
			// Remove all chips or this will cause duplicate tags
			mTaskTags!!.removeAllViews()
			for (tag in item.tags!!) {
				val tempChip = Chip(this)
				tempChip.text = tag
				mTaskTags!!.addView(tempChip)
			}
			toggleViewVisibility(mTaskTagsParentView!!, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(mTaskTagsParentView!!, View.VISIBLE, View.GONE)
		}
		toggleViewVisibility(findViewById(R.id.progressBar), View.VISIBLE, View.GONE)
		toggleViewVisibility(findViewById(R.id.scrollTaskView), View.GONE, View.VISIBLE)
	}
}
