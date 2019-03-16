package com.edricchan.studybuddy.ui.modules.task.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.ContentView
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.extensions.toObjectWithId
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.ui.adapter.TasksAdapter
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.auth.RegisterActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.ViewTaskActivity
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java8.util.stream.IntStreams
import java.util.*
import java.util.stream.IntStream

@ContentView(R.layout.frag_todo)
class TaskFragment : Fragment() {
	private var mAuth: FirebaseAuth? = null
	private val mGoogleApiClient: GoogleApiClient? = null
	private val mUserName: String? = null
	private var mFirestore: FirebaseFirestore? = null
	private var mCurrentUser: FirebaseUser? = null
	private var mAdapter: TasksAdapter? = null
	private var mRecyclerView: RecyclerView? = null
	private var mFirestoreListener: ListenerRegistration? = null
	private var mFragmentView: View? = null
	private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
	private var mPrefs: SharedPreferences? = null
	private var mTodoFragPrefs: SharedPreferences? = null
//	private var mSelectionTracker: SelectionTracker<String>? = null
	private var mParentActivity: AppCompatActivity? = null
//	private var mActionModeCallback: ActionMode.Callback? = null

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.action_new_todo -> {
				newTaskActivity()
				return true
			}
			R.id.action_refresh_todos -> {
				loadTasksList(mCurrentUser!!.uid)
				return true
			}
			R.id.action_settings -> {
				val settingsIntent = Intent(context, SettingsActivity::class.java)
				Objects.requireNonNull<Context>(context).startActivity(settingsIntent)
				return true
			}
			R.id.action_sort_none -> {
				if (!item.isChecked) {
					SharedUtils.putPrefs(Objects.requireNonNull<Context>(context), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "none")
							.commit()
					item.isChecked = true
					loadTasksList(mCurrentUser!!.uid)
				}
				if (!item.isChecked) {
					SharedUtils.putPrefs(Objects.requireNonNull<Context>(context), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_desc")
							.commit()
					item.isChecked = true
					loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.DESCENDING)
				}
				return true
			}
			R.id.action_sort_title_descending -> {
				if (!item.isChecked) {
					SharedUtils.putPrefs(Objects.requireNonNull<Context>(context), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_desc").commit()
					item.isChecked = true
					loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.DESCENDING)
				}
				return true
			}
			R.id.action_sort_title_ascending -> {
				if (!item.isChecked) {
					SharedUtils.putPrefs(Objects.requireNonNull<Context>(context), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "title_asc")
							.commit()
					item.isChecked = true
					loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.ASCENDING)
				}
				return true
			}
			R.id.action_sort_due_date_new_to_old -> {
				if (!item.isChecked) {
					SharedUtils.putPrefs(Objects.requireNonNull<Context>(context), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "due_date_new_to_old")
							.commit()
					item.isChecked = true
					loadTasksList(mCurrentUser!!.uid, "dueDate", Query.Direction.DESCENDING)
				}
				return true
			}
			R.id.action_sort_due_date_old_to_new -> {
				if (!item.isChecked) {
					SharedUtils.putPrefs(Objects.requireNonNull<Context>(context), SHARED_PREFS_FILE, MODE_PRIVATE, "sortTasksBy", "due_date_old_to_new")
							.commit()
					item.isChecked = true
					loadTasksList(mCurrentUser!!.uid, "dueDate", Query.Direction.ASCENDING)
				}
				val debugIntent = Intent(context, DebugActivity::class.java)
				startActivity(debugIntent)
				return true
			}
			R.id.action_debug -> {
				val debugIntent = Intent(context, DebugActivity::class.java)
				startActivity(debugIntent)
				return true
			}
			else -> return super.onOptionsItemSelected(item)
		}
	}

	override fun onPrepareOptionsMenu(menu: Menu) {
		if (!BuildConfig.DEBUG) {
			menu.removeItem(R.id.action_debug)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		// Clear the main activity's menu before inflating the fragment's menu
		menu.clear()
		inflater.inflate(R.menu.menu_frag_todo, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		/*if (savedInstanceState != null && mSelectionTracker != null) {
			mSelectionTracker!!.onRestoreInstanceState(savedInstanceState)
		}*/

		mPrefs = PreferenceManager.getDefaultSharedPreferences(context!!)
		mTodoFragPrefs = Objects.requireNonNull<Context>(context).getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
		mFirestore = FirebaseFirestore.getInstance()
		mFragmentView = view

		SharedUtils.setBottomAppBarFabOnClickListener(mParentActivity!!, View.OnClickListener { newTaskActivity() })

		// Handles swiping down to refresh logic
		mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
		mSwipeRefreshLayout!!.setColorSchemeResources(R.color.colorPrimary)
		// Sets a refreshing listener
		mSwipeRefreshLayout!!.setOnRefreshListener {
			mAdapter!!.notifyDataSetChanged()
			loadTasksListHandler()
		}
		mRecyclerView = findViewById(R.id.recyclerView)

		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView!!.setHasFixedSize(false)

		// use a linear layout manager
		val mLayoutManager = LinearLayoutManager(context)
		mRecyclerView!!.layoutManager = mLayoutManager
		mRecyclerView!!.itemAnimator = DefaultItemAnimator()
		ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
			override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
				return false
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				mAdapter!!.deleteTask(viewHolder.adapterPosition)
			}
		}).attachToRecyclerView(mRecyclerView)
		findViewById<View>(R.id.actionNewTodo)
				.setOnClickListener { newTaskActivity() }
	}

	/*override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		if (mSelectionTracker != null) {
			mSelectionTracker!!.onSaveInstanceState(outState)
		}
	}*/

	override fun onDestroyView() {
		super.onDestroyView()

		if (mFirestoreListener != null) {
			mFirestoreListener!!.remove()
		}

		SharedUtils.clearBottomAppBarFabOnClickListener(mParentActivity!!)
	}

	override fun onStart() {
		super.onStart()
		mAuth = FirebaseAuth.getInstance()
		mCurrentUser = mAuth!!.currentUser
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in")
			val signInDialogBuilder = MaterialAlertDialogBuilder(context!!)
			signInDialogBuilder.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login) { dialogInterface, i ->
						val loginIntent = Intent(context, LoginActivity::class.java)
						startActivity(loginIntent)
						dialogInterface.dismiss()
					}
					.setNeutralButton(R.string.dialog_action_sign_up) { dialogInterface, i ->
						val registerIntent = Intent(context, RegisterActivity::class.java)
						startActivity(registerIntent)
						dialogInterface.dismiss()
					}
					.setNegativeButton(R.string.dialog_action_cancel) { dialogInterface, i -> dialogInterface.cancel() }
					.show()

		} else {
			loadTasksList(mCurrentUser!!.uid)
		}
	}

	override fun onAttach(context: Context) {
		mParentActivity = context as AppCompatActivity
		super.onAttach(context as Context)
	}

	private fun newTaskActivity() {
		val newTaskIntent = Intent(context, NewTaskActivity::class.java)
		startActivityForResult(newTaskIntent, ACTION_NEW_TASK)
	}

	private fun loadTasksListHandler() {
		if (mPrefs!!.getString("pref_todo_default_sort", "no_value") == "no_value") {
			if (mTodoFragPrefs!!.getString("sortTasksBy", "no_value") != "no_value") {
				when (Objects.requireNonNull(mTodoFragPrefs!!.getString("sortTasksBy", "due_date_new_to_old"))) {
					"title_desc" -> loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.DESCENDING)
					"title_asc" -> loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.ASCENDING)
					"due_date_new_to_old" -> loadTasksList(mCurrentUser!!.uid, "dueDate", Query.Direction.DESCENDING)
					"due_date_old_to_new" -> loadTasksList(mCurrentUser!!.uid, "dueDate", Query.Direction.ASCENDING)
					else -> loadTasksList(mCurrentUser!!.uid)
				}
			} else {
				loadTasksList(mCurrentUser!!.uid)
			}
		} else {
			when (Objects.requireNonNull(mPrefs!!.getString("pref_todo_default_sort", "due_date"))) {
				"title_desc" -> loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.DESCENDING)
				"title_asc" -> loadTasksList(mCurrentUser!!.uid, "title", Query.Direction.ASCENDING)
				"due_date_new_to_old" -> loadTasksList(mCurrentUser!!.uid, "dueDate", Query.Direction.DESCENDING)
				"due_date_old_to_new" -> loadTasksList(mCurrentUser!!.uid, "dueDate", Query.Direction.ASCENDING)
				else -> loadTasksList(mCurrentUser!!.uid)
			}
		}
	}

	/**
	 * Loads the task list
	 *
	 * @param uid       The user's ID
	 * @param fieldPath The field path to sort the list by
	 * @param direction The direction to sort the list in
	 */
	private fun loadTasksList(uid: String, fieldPath: String? = null, direction: Query.Direction? = null) {
		// Reduce the amount of duplicate code by placing the listener into a variable
		val listener = EventListener<QuerySnapshot> { documentSnapshots, e ->
			if (e != null) {
				Log.e(TAG, "Listen failed!", e)
				return@EventListener
			}
			val taskItemList = ArrayList<TaskItem>()

			for (doc in documentSnapshots!!) {
				Log.d(TAG, "Document: $doc")
				val note = doc.toObjectWithId<TaskItem>()
				taskItemList.add(note)
			}

			mAdapter = TasksAdapter(context!!, taskItemList)
			mRecyclerView!!.adapter = mAdapter
			mAdapter!!
					.setOnItemClickListener(object : TasksAdapter.OnItemClickListener {
						override fun onItemClick(item: TaskItem, position: Int) {
							val viewItemIntent = Intent(context, ViewTaskActivity::class.java)
							Log.d(TAG, "Task: $item")
							viewItemIntent.putExtra("taskId", item.id)
							startActivity(viewItemIntent)
						}

						override fun onDeleteButtonClick(item: TaskItem, position: Int) {
							val builder = MaterialAlertDialogBuilder(context!!)
							builder
									.setTitle(R.string.todo_frag_delete_task_dialog_title)
									.setMessage(R.string.todo_frag_delete_task_dialog_msg)
									.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
									.setPositiveButton(R.string.dialog_action_ok) { dialog, which ->
										mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + item.id)
												.delete()
												.addOnCompleteListener { task ->
													if (task.isSuccessful) {
														Snackbar.make(
																findParentActivityViewById(R.id.coordinatorLayout),
																"Successfully deleted todo!",
																Snackbar.LENGTH_SHORT)
																.show()
														mAdapter!!.notifyItemRemoved(position)
														dialog.dismiss()
													} else {
														Log.e(TAG, "An error occurred while attempting to delete the todo:", task.exception)
													}
												}
									}
									.show()
						}

						override fun onMarkAsDoneButtonClick(item: TaskItem, position: Int) {
							mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + item.id)
									.update("isDone", !item.isDone!!)
									.addOnCompleteListener { task ->
										val isDoneStr = if (item.isDone!!) "done" else "undone"
										if (task.isSuccessful) {
											Snackbar.make(
													findParentActivityViewById(R.id.coordinatorLayout),
													"Successfully marked task as $isDoneStr!",
													Snackbar.LENGTH_SHORT)
													.show()
											mAdapter!!.notifyItemChanged(position)
										} else {
											Snackbar.make(
													findParentActivityViewById(R.id.coordinatorLayout),
													"An error occurred while attempting to mark the todo as $isDoneStr",
													Snackbar.LENGTH_LONG)
													.show()
											Log.e(TAG, "An error occurred while attempting to mark the todo as $isDoneStr:", task.exception)
										}
									}
						}
					})
			/*if (mSelectionTracker == null) {
				mSelectionTracker = SelectionTracker.Builder(
						"selection-id",
						mRecyclerView!!,
						TaskItemKeyProvider(taskItemList),
						TaskItemLookup(mRecyclerView!!),
						StorageStrategy.createStringStorage())
						.withOnItemActivatedListener { item, event ->
							val viewItemIntent = Intent(context, ViewTaskActivity::class.java)
							viewItemIntent.putExtra("taskId", taskItemList[item.position].id)
							startActivity(viewItemIntent)
							true
						}
						.withSelectionPredicate(SelectionPredicates.createSelectAnything())
						.build()
			}*/
			mSwipeRefreshLayout!!.isRefreshing = false
			if (documentSnapshots.isEmpty) {
				Log.d(TAG, "Empty!")
				findViewById<View>(R.id.todoEmptyStateView).visibility = View.VISIBLE
				mSwipeRefreshLayout!!.visibility = View.GONE
			} else {
				Log.d(TAG, "Not Empty!")
				findViewById<View>(R.id.todoEmptyStateView).visibility = View.GONE
				mSwipeRefreshLayout!!.visibility = View.VISIBLE
			}
			/*mActionModeCallback = object : ActionMode.Callback {
				override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
					val inflater = mode.menuInflater
					inflater.inflate(R.menu.cab_tasks, menu)
					return true
				}

				override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
					if (mSelectionTracker!!.hasSelection()) {
						if (mSelectionTracker!!.selection.size() == taskItemList.size) {
							menu.removeItem(R.id.cab_action_select_all)
						}
					}
					return true
				}

				override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
					when (item.itemId) {
						R.id.cab_action_delete_selected -> {
							val confirmBuilder = MaterialAlertDialogBuilder(context!!)
							confirmBuilder
									.setTitle(R.string.todo_frag_delete_selected_tasks_dialog_title)
									.setMessage(R.string.todo_frag_delete_selected_tasks_dialog_msg)
									.setNegativeButton(R.string.dialog_action_cancel) { dialog, which ->
										mode.finish()
										dialog.dismiss()
									}
									.setPositiveButton(R.string.dialog_action_ok) { dialog, which ->
										for (id in mSelectionTracker!!.selection) {
											mFirestore!!.document("users/" + mCurrentUser!!.uid + "/todos/" + id)
													.delete()
													.addOnCompleteListener { task ->
														if (task.isSuccessful) {
															Log.d(TAG, "Successfully deleted todo!")
															mAdapter!!.notifyItemRemoved(getTaskItemPosition(taskItemList, id))
														} else {
															Log.e(TAG, "An error occurred while attempting to delete the todo at ID $id:", task.exception)
															Toast.makeText(mParentActivity, "An error occurred while attempting to delete the todo at ID $id", Toast.LENGTH_SHORT).show()
														}
													}
										}
										dialog.dismiss()
									}
									.show()
							return true
						}
						R.id.cab_action_select_all -> {
							Toast.makeText(mParentActivity, "Not implemented!", Toast.LENGTH_SHORT).show()
							return true
						}
						R.id.cab_action_edit_selected -> {
							Toast.makeText(mParentActivity, "Not implemented!", Toast.LENGTH_SHORT).show()
							return false
						}
						else -> return false
					}
				}

				override fun onDestroyActionMode(mode: ActionMode) {
					mSelectionTracker!!.clearSelection()
				}
			}
			mSelectionTracker!!
					.addObserver(object : SelectionTracker.SelectionObserver<String>() {
						override fun onSelectionChanged() {
							super.onSelectionChanged()
							Log.d(TAG, "Current size of selection: " + mSelectionTracker!!.selection.size())
							if (mSelectionTracker!!.hasSelection()) {
								val mode = mParentActivity!!.startSupportActionMode(mActionModeCallback!!)
								mode!!.setTitle(R.string.cab_title)
								mode.subtitle = resources.getQuantityString(R.plurals.cab_subtitle, mSelectionTracker!!.selection.size(), mSelectionTracker!!.selection.size())
							}
						}
					})
			*/
		}
		mSwipeRefreshLayout!!.isRefreshing = true
		val collectionRef = mFirestore!!.collection("users/" + mCurrentUser!!.uid + "/todos")
		if (fieldPath != null && direction != null) {
			mFirestoreListener = collectionRef.orderBy(fieldPath, direction)
					.addSnapshotListener(listener)
		} else {
			mFirestoreListener = collectionRef.addSnapshotListener(listener)
		}
	}


	/**
	 * Gets the position of the first occurrence of the document ID
	 *
	 * @param list The list to get the position for
	 * @param id   The ID of the document
	 * @return The position of the first occurrence of the document ID
	 */
	private fun getTaskItemPosition(list: List<TaskItem>, id: String): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			IntStream.range(0, list.size)
					.filter { index -> list[index].id == id }
					.findFirst()
					.orElse(-1)
		} else {
			IntStreams
					.range(0, list.size)
					.filter { index -> list[index].id == id }
					.findFirst()
					.orElse(-1)
		}
	}

	private fun <T : View> findParentActivityViewById(@IdRes id: Int): T {
		return mParentActivity!!.findViewById(id)
	}

	private fun <T : View> findViewById(@IdRes id: Int): T {
		return mFragmentView!!.findViewById(id)
	}

	companion object {
		/**
		 * Request code for new task activity
		 */
		private val ACTION_NEW_TASK = 1
		/**
		 * The Android tag for use with [android.util.Log]
		 */
		private val TAG = SharedUtils.getTag(TaskFragment::class.java)

		private val SHARED_PREFS_FILE = "TodoFragPrefs"
	}
}
