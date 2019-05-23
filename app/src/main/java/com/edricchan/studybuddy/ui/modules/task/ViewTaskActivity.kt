package com.edricchan.studybuddy.ui.modules.task

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.auth.RegisterActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.Duration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_view_task.*
import ru.noties.markwon.Markwon
import java.text.SimpleDateFormat
import java.util.*

class ViewTaskActivity : AppCompatActivity(R.layout.activity_view_task) {
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mFirestore: FirebaseFirestore
	private var mCurrentUser: FirebaseUser? = null
	private val TAG = SharedUtils.getTag(this::class.java)
	private var mTaskId: String? = null
	private var taskItem: TaskItem? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		mTaskId = intent.getStringExtra("taskId")
		Log.d(TAG, "Task ID: $mTaskId")
		bottomAppBar.replaceMenu(R.menu.menu_view_task)
		bottomAppBar.setOnMenuItemClickListener {
			when (it.itemId) {
				android.R.id.home -> {
					onBackPressed()
					return@setOnMenuItemClickListener true
				}
				R.id.action_delete -> {
					val builder = MaterialAlertDialogBuilder(this)
					builder.setTitle("Delete todo?")
							.setPositiveButton(R.string.dialog_action_ok) { _, _ ->
								mFirestore.document("users/${mCurrentUser?.uid}/todos/$mTaskId")
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
							.setNegativeButton(R.string.dialog_action_cancel) { dialog, _ -> dialog.dismiss() }
							.show()
					return@setOnMenuItemClickListener true
				}
				R.id.action_edit -> {
					startActivity<EditTaskActivity> {
						putExtra("taskId", mTaskId)
					}
					return@setOnMenuItemClickListener true
				}
				R.id.action_mark_as_done -> {
					mFirestore.document("users/${mCurrentUser?.uid}/todos/$mTaskId")
							.update("isDone", !taskItem?.isDone!!)
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									showSnackbar("Task marked as " + if ((!taskItem!!.isDone!!)) "done" else "undone", Snackbar.LENGTH_SHORT)
									Log.d(TAG, "Task marked as " + if ((!taskItem!!.isDone!!)) "done" else "undone")
								} else {
									Toast.makeText(this, "An error occurred while marking the todo as " + if ((!taskItem!!.isDone!!)) "done" else "undone", Toast.LENGTH_LONG)
											.show()
									Log.e(TAG, "An error occurred while marking the todo as " + if ((!taskItem!!.isDone!!)) "done" else "undone", task.exception)
								}
							}
					return@setOnMenuItemClickListener true
				}
				R.id.action_archive -> {
					mFirestore.document("users/${mCurrentUser?.uid}/todos/$mTaskId")
							.update("isArchived", taskItem?.isArchived ?: false)
							.addOnCompleteListener { task ->
								if (task.isSuccessful) {
									showSnackbar("Successfully ${if (taskItem?.isArchived == true) "un" else ""}archived task!", Snackbar.LENGTH_SHORT)
									Log.d(TAG, "Successfully ${if (taskItem?.isArchived == true) "un" else ""}archived task!")
								} else {
									Toast.makeText(this, "An error occurred while attempting to ${if (taskItem?.isArchived == true) "un" else ""}archive the task", Toast.LENGTH_SHORT).show()
									Log.e(TAG, "An error occurred while attempting to ${if (taskItem?.isArchived == true) "un" else ""}archive the task:", task.exception)
								}
							}
					return@setOnMenuItemClickListener true
				}
				else -> return@setOnMenuItemClickListener super.onOptionsItemSelected(it)
			}
		}
		editTaskFab.setOnClickListener {
			startActivity<EditTaskActivity> {
				putExtra("taskId", mTaskId)
			}
		}
	}

	override fun onStart() {
		super.onStart()
		mFirestore = FirebaseFirestore.getInstance()
		mAuth = FirebaseAuth.getInstance()
		mCurrentUser = mAuth.currentUser
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in")
			val signInDialogBuilder = MaterialAlertDialogBuilder(this)
			signInDialogBuilder
					.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login) { dialogInterface, _ ->
						startActivity<LoginActivity>()
						dialogInterface.dismiss()
					}
					.setNeutralButton(R.string.dialog_action_sign_up) { dialogInterface, _ ->
						startActivity<RegisterActivity>()
						dialogInterface.dismiss()
					}
					.setNegativeButton(R.string.dialog_action_cancel) { dialogInterface, _ -> dialogInterface.cancel() }
					.show()
		} else {
			loadTask()
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> {
				onBackPressed()
				return true
			}
			/*R.id.action_delete -> {
				val builder = MaterialAlertDialogBuilder(this)
				builder.setTitle("Delete todo?")
						.setPositiveButton(R.string.dialog_action_ok) { dialog, which ->
							mFirestore.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
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
				mFirestore.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
						.update("isDone", !taskItem!!.isDone!!)
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
			}*/
			else -> return super.onOptionsItemSelected(item)
		}
	}

	/**
	 * Loads the task that was supplied via `taskId`
	 */
	private fun loadTask() {
		mFirestore.document("users/" + mCurrentUser!!.uid + "/todos/" + mTaskId)
				.addSnapshotListener { documentSnapshot, e ->
					if (e != null) {
						Log.e(TAG, "An error occurred while retrieving the task:", e)
						showSnackbar(R.string.view_task_unsuccessful_snackbar_text, Snackbar.LENGTH_LONG)
					} else {
						if (documentSnapshot != null && documentSnapshot.exists()) {
							setViews(documentSnapshot.toObject<TaskItem>())
							taskItem = documentSnapshot.toObject<TaskItem>()
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
	private fun setViews(item: TaskItem?) {
		if (item?.content != null) {
			Markwon.setMarkdown(taskContent, item.content!!)
			toggleViewVisibility(taskContent, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(taskContent, View.VISIBLE, View.GONE)
		}
		if (item?.dueDate != null) {
			val format = SimpleDateFormat(getString(R.string.date_format_pattern), Locale.ENGLISH)
			taskDate.text = format.format(item.dueDate!!.toDate())
			toggleViewVisibility(taskDate, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(taskDate, View.VISIBLE, View.GONE)
		}
		if (item?.project != null) {
			item.project!!
					.get()
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							taskProject.text = task.result!!.getString("name")
						} else {
							Toast.makeText(this, "An error occurred while attempting to retrieve the project. Please try again later.", Toast.LENGTH_SHORT).show()
							Log.e(TAG, "An error occurred while attempting to retrieve the project:", task.exception)
						}
					}
			toggleViewVisibility(taskProject, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(taskProject, View.VISIBLE, View.GONE)
		}
		if (item?.title != null) {
			taskTitle.text = item.title
			toggleViewVisibility(taskTitle, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(taskTitle!!, View.VISIBLE, View.GONE)
		}
		if (item?.tags != null && !item.tags!!.isEmpty()) {
			// Remove all chips or this will cause duplicate tags
			taskTags.removeAllViews()
			for (tag in item.tags!!) {
				val tempChip = Chip(this)
				tempChip.text = tag
				taskTags.addView(tempChip)
			}
			toggleViewVisibility(taskTagsParentView, View.GONE, View.VISIBLE)
		} else {
			toggleViewVisibility(taskTagsParentView, View.VISIBLE, View.GONE)
		}
		toggleViewVisibility(progressBar, View.VISIBLE, View.GONE)
		toggleViewVisibility(scrollTaskView, View.GONE, View.VISIBLE)
	}

	private fun showSnackbar(text: String, @Duration duration: Int) {
		Snackbar.make(mainView, text, duration)
				.setAnchorView(editTaskFab)
				.show()
	}

	private fun showSnackbar(@StringRes textRes: Int, @Duration duration: Int) {
		showSnackbar(getString(textRes), duration)
	}
}
