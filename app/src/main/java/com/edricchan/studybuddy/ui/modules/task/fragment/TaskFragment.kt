package com.edricchan.studybuddy.ui.modules.task.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.Constants
import com.edricchan.studybuddy.constants.sharedprefs.TaskOptionsPrefConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.firebase.firestore.toObjectWithId
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.extensions.startActivityForResult
import com.edricchan.studybuddy.interfaces.TaskItem
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.auth.RegisterActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.ViewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.adapter.TasksAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetFragment
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.utils.SharedPrefUtils
import com.edricchan.studybuddy.utils.SharedUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java8.util.stream.IntStreams
import kotlinx.android.synthetic.main.frag_todo.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.stream.IntStream

// FIXME: Fix whole code - it's very messy especially after migrating to Kotlin
class TaskFragment : Fragment(R.layout.frag_todo) {
	private var mCurrentUser: FirebaseUser? = null
	private var mAdapter: TasksAdapter? = null
	private var mFirestoreListener: ListenerRegistration? = null
	private var mFragmentView: View? = null
	private var mTaskOptionsPrefs: SharedPreferences? = null
	//	private var mSelectionTracker: SelectionTracker<String>? = null
	private var mParentActivity: AppCompatActivity? = null
	//	private var mActionModeCallback: ActionMode.Callback? = null
	private lateinit var mAuth: FirebaseAuth
	private lateinit var mFirestore: FirebaseFirestore
	private lateinit var mPrefs: SharedPreferences
	private lateinit var mSharedPrefUtils: SharedPrefUtils

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			/*R.id.action_new_todo -> {
				newTaskActivity()
				return true
			}
			R.id.action_refresh_todos -> {
				loadTasksListHandler()
				return true
			}
			R.id.action_settings -> {
				startActivity<SettingsActivity>()
				return true
			}
			R.id.action_sort_by -> {
				val itemNoneId = 1
				val itemTitleAscId = 2
				val itemTitleDescId = 3
				val itemDueDateNewestId = 4
				val itemDueDateOldestId = 5
				val group = ModalBottomSheetGroup(id = 100, checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE,
						onItemCheckedChangeListener = object : ModalBottomSheetAdapter.OnItemCheckedChangeListener {
							override fun onItemCheckedChange(item: ModalBottomSheetItem) {
								when (item.id) {
									itemNoneId -> {
										mTaskOptionsPrefs?.edit {
											putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.NONE)
										}
									}
									itemTitleDescId -> {
										mTaskOptionsPrefs?.edit {
											putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.TITLE_DESC)
										}
									}
									itemTitleAscId -> {
										mTaskOptionsPrefs?.edit {
											putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.TITLE_ASC)
										}
									}
									itemDueDateNewestId -> {
										mTaskOptionsPrefs?.edit {
											putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_NEW_TO_OLD)
										}
									}
									itemDueDateOldestId -> {
										mTaskOptionsPrefs?.edit {
											putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_OLD_TO_NEW)
										}
									}
								}
								loadTasksListHandler()
							}
						})
				val items = arrayOf(
						ModalBottomSheetItem(id = itemNoneId, title = getString(R.string.sort_by_bottomsheet_none_title),
								icon = R.drawable.ic_close_24dp, group = group),
						ModalBottomSheetItem(id = itemTitleAscId, title = getString(R.string.sort_by_bottomsheet_title_asc_title),
								icon = R.drawable.ic_sort_ascending_24dp, group = group),
						ModalBottomSheetItem(id = itemTitleDescId, title = getString(R.string.sort_by_bottomsheet_title_desc_title),
								icon = R.drawable.ic_sort_descending_24dp, group = group),
						ModalBottomSheetItem(id = itemDueDateNewestId,
								title = getString(R.string.sort_by_bottomsheet_due_date_newest_title),
								icon = R.drawable.ic_sort_descending_24dp, group = group),
						ModalBottomSheetItem(id = itemDueDateOldestId,
								title = getString(R.string.sort_by_bottomsheet_due_date_oldest_title),
								icon = R.drawable.ic_sort_ascending_24dp, group = group)
				)
				val modalBottomSheet = ModalBottomSheetFragment()
				modalBottomSheet.setItems(items)
				modalBottomSheet.headerTitle = "Sort tasks by..."
				modalBottomSheet.show(requireFragmentManager(), modalBottomSheet.tag)
				return true
			}*/
			/*R.id.action_sort_none -> {
				if (!item.isChecked) {
					mTaskOptionsPrefs?.edit {
						putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.NONE)
					}
					item.isChecked = true
					loadTasksListHandler()
				}
				return true
			}
			R.id.action_sort_title_descending -> {
				if (!item.isChecked) {
					mTaskOptionsPrefs?.edit {
						putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.TITLE_DESC)
					}
					item.isChecked = true
					loadTasksListHandler()
				}
				return true
			}
			R.id.action_sort_title_ascending -> {
				if (!item.isChecked) {
					mTaskOptionsPrefs?.edit {
						putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.TITLE_ASC)
					}
					item.isChecked = true
					loadTasksListHandler()
				}
				return true
			}
			R.id.action_sort_due_date_new_to_old -> {
				if (!item.isChecked) {
					mTaskOptionsPrefs?.edit {
						putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_NEW_TO_OLD)
					}
					item.isChecked = true
					loadTasksListHandler()
				}
				return true
			}
			R.id.action_sort_due_date_old_to_new -> {
				if (!item.isChecked) {
					mTaskOptionsPrefs?.edit {
						putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_OLD_TO_NEW)
					}
					item.isChecked = true
					loadTasksListHandler()
				}
				return true
			}*/
			/*R.id.action_debug -> {
				startActivity<DebugActivity>()
				return true
			}*/
			R.id.action_show_more_options -> {
				val modalBottomSheet = ModalBottomSheetFragment()
				val items = arrayOf(
						ModalBottomSheetItem(title = getString(R.string.menu_frag_task_refresh_todos_title),
								icon = R.drawable.ic_refresh_24dp,
								onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
									override fun onItemClick(item: ModalBottomSheetItem) {
										loadTasksListHandler()
										modalBottomSheet.dismiss()
									}
								}),
						ModalBottomSheetItem(title = getString(R.string.menu_frag_task_sort_by_title),
								icon = R.drawable.ic_sort_24dp,
								onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
									override fun onItemClick(item: ModalBottomSheetItem) {
										showSortByOptions()
										modalBottomSheet.dismiss()
									}
								}),
						ModalBottomSheetItem(title = getString(R.string.menu_frag_task_import_from_title),
								visible = false, // Hide option for now
								icon = R.drawable.ic_import_24dp,
								onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
									override fun onItemClick(item: ModalBottomSheetItem) {
										TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
									}

								}
								),
						ModalBottomSheetItem(title = getString(R.string.menu_settings_title),
								icon = R.drawable.ic_settings_outline_24dp,
								onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
									override fun onItemClick(item: ModalBottomSheetItem) {
										startActivity<SettingsActivity>()
										modalBottomSheet.dismiss()
									}
								}),
						ModalBottomSheetItem(title = getString(R.string.menu_debug_title),
								icon = R.drawable.ic_bug_report_outline_24dp, visible = SharedUtils.isDevMode(requireContext()),
								onItemClickListener = object : ModalBottomSheetAdapter.OnItemClickListener {
									override fun onItemClick(item: ModalBottomSheetItem) {
										startActivity<DebugActivity>()
										modalBottomSheet.dismiss()
									}
								})
				)
				modalBottomSheet.setItems(items)
				modalBottomSheet.show(requireFragmentManager(), modalBottomSheet.tag)
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
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
		mSharedPrefUtils = SharedPrefUtils(requireContext())
		// Checks if old preference file exists
		if (mSharedPrefUtils.sharedPrefFileExists(SHARED_PREFS_FILE)) {
			Log.d(TAG, "Migrating shared preference file...")
			// Rename existing file to new file
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				// Use better implementation of renaming file
				try {
					Files.move(
							mSharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE).toPath(),
							mSharedPrefUtils.getSharedPrefsFile(TaskOptionsPrefConstants.FILE_TASK_OPTIONS).toPath(),
							StandardCopyOption.REPLACE_EXISTING)
					// Delete existing
					Log.d(TAG, "Deleting previous shared preference file...")
					val result = Files.deleteIfExists(mSharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE).toPath())
					if (result) {
						Log.d(TAG, "Successfully deleted previous shared preference file!")
					} else {
						Log.e(TAG, "Something went wrong while attempting to delete the previous shared preference file.")
					}
				} catch (e: IOException) {
					Log.e(TAG, "An error occurred while attempting to migrate the shared preference file:", e)
				}
			} else {
				val result = mSharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE).renameTo(mSharedPrefUtils.getSharedPrefsFile(TaskOptionsPrefConstants.FILE_TASK_OPTIONS))
				if (result) {
					Log.d(TAG, "Successfully migrated shared preference file!")
				} else {
					Log.e(TAG, "Something went wrong while attempting to migrate the shared preference file.")
				}
			}
		}
		// Migrate keys and their values
		migrateTaskOptsPrefs()
		mTaskOptionsPrefs = context?.getSharedPreferences(TaskOptionsPrefConstants.FILE_TASK_OPTIONS, Context.MODE_PRIVATE)
		mFirestore = FirebaseFirestore.getInstance()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		/*if (savedInstanceState != null && mSelectionTracker != null) {
			mSelectionTracker!!.onRestoreInstanceState(savedInstanceState)
		}*/


		mFragmentView = view

		mParentActivity?.let { SharedUtils.setBottomAppBarFabOnClickListener(it, View.OnClickListener { newTaskActivity() }) }

		swipeRefreshLayout.apply {
			setColorSchemeResources(R.color.colorPrimary)
			setOnRefreshListener {
				mAdapter?.notifyDataSetChanged()
				loadTasksListHandler()
			}
		}

		recyclerView.apply {
			setHasFixedSize(false)
			layoutManager = LinearLayoutManager(context)
			itemAnimator = DefaultItemAnimator()
		}
		ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
			override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
				return false
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				mAdapter?.deleteTask(viewHolder.adapterPosition)
			}
		}).attachToRecyclerView(recyclerView)
		actionNewTodo.setOnClickListener { newTaskActivity() }
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
			mFirestoreListener?.remove()
		}

		mParentActivity?.let { SharedUtils.clearBottomAppBarFabOnClickListener(it) }
	}

	override fun onStart() {
		super.onStart()
		mAuth = FirebaseAuth.getInstance()
		mCurrentUser = mAuth.currentUser
		if (mCurrentUser == null) {
			Log.d(TAG, "Not logged in")
			val signInDialogBuilder = MaterialAlertDialogBuilder(context)
			signInDialogBuilder.setTitle("Sign in")
					.setMessage("To access the content, please login or register for an account.")
					.setPositiveButton(R.string.dialog_action_login) { dialogInterface, i ->
						startActivity<LoginActivity>()
						dialogInterface.dismiss()
					}
					.setNeutralButton(R.string.dialog_action_sign_up) { dialogInterface, i ->
						startActivity<RegisterActivity>()
						dialogInterface.dismiss()
					}
					.setNegativeButton(R.string.dialog_action_cancel) { dialogInterface, i -> dialogInterface.cancel() }
					.show()

		} else {
			loadTasksList()
		}
	}

	override fun onAttach(context: Context) {
		mParentActivity = context as AppCompatActivity
		super.onAttach(context as Context)
	}

	private fun showSortByOptions() {
		val itemNoneId = 1
		val itemTitleAscId = 2
		val itemTitleDescId = 3
		val itemDueDateNewestId = 4
		val itemDueDateOldestId = 5
		val group = ModalBottomSheetGroup(id = 100, checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE,
				onItemCheckedChangeListener = object : ModalBottomSheetAdapter.OnItemCheckedChangeListener {
					override fun onItemCheckedChange(item: ModalBottomSheetItem) {
						when (item.id) {
							itemNoneId -> {
								mTaskOptionsPrefs?.edit {
									putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.NONE)
								}
							}
							itemTitleDescId -> {
								mTaskOptionsPrefs?.edit {
									putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.TITLE_DESC)
								}
							}
							itemTitleAscId -> {
								mTaskOptionsPrefs?.edit {
									putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.TITLE_ASC)
								}
							}
							itemDueDateNewestId -> {
								mTaskOptionsPrefs?.edit {
									putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_NEW_TO_OLD)
								}
							}
							itemDueDateOldestId -> {
								mTaskOptionsPrefs?.edit {
									putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_OLD_TO_NEW)
								}
							}
						}
						loadTasksListHandler()
					}
				})
		val items = arrayOf(
				ModalBottomSheetItem(id = itemNoneId, title = getString(R.string.sort_by_bottomsheet_none_title),
						icon = R.drawable.ic_close_24dp, group = group),
				ModalBottomSheetItem(id = itemTitleAscId, title = getString(R.string.sort_by_bottomsheet_title_asc_title),
						icon = R.drawable.ic_sort_ascending_24dp, group = group),
				ModalBottomSheetItem(id = itemTitleDescId, title = getString(R.string.sort_by_bottomsheet_title_desc_title),
						icon = R.drawable.ic_sort_descending_24dp, group = group),
				ModalBottomSheetItem(id = itemDueDateNewestId,
						title = getString(R.string.sort_by_bottomsheet_due_date_newest_title),
						icon = R.drawable.ic_sort_descending_24dp, group = group),
				ModalBottomSheetItem(id = itemDueDateOldestId,
						title = getString(R.string.sort_by_bottomsheet_due_date_oldest_title),
						icon = R.drawable.ic_sort_ascending_24dp, group = group)
		)
		val modalBottomSheet = ModalBottomSheetFragment()
		modalBottomSheet.setItems(items)
		modalBottomSheet.headerTitle = "Sort tasks by..."
		modalBottomSheet.show(requireFragmentManager(), modalBottomSheet.tag)
	}

	private fun showImportFromOptions() {

	}

	private fun newTaskActivity() {
		startActivityForResult<NewTaskActivity>(ACTION_NEW_TASK)
	}

	private fun migrateTaskOptsPrefs() {
		Log.d(TAG, "Migrating task options shared preference keys...")
		if (mTaskOptionsPrefs?.getString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT_OLD, null) != null) {
			Log.d(TAG, "Old task sorting tag still exists.")
			// Old SharedPreference key still exists
			val oldValue = mTaskOptionsPrefs?.getString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT_OLD, TaskOptionsPrefConstants.TaskSortValues.NONE)
			mTaskOptionsPrefs?.edit {
				putString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, oldValue)
				remove(TaskOptionsPrefConstants.PREF_DEFAULT_SORT_OLD)
			}
		}
	}

	private fun loadTasksListHandler() {
		@Suppress("MoveVariableDeclarationIntoWhen")
		val sortPref = if (mPrefs.getString(Constants.prefTodoDefaultSort, null) == null) {
			// TODO: Migrate old key's value to new key
			if (mTaskOptionsPrefs?.getString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT_OLD, null) == null) {
				mTaskOptionsPrefs?.getString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT, TaskOptionsPrefConstants.TaskSortValues.NONE)
			} else {
				mTaskOptionsPrefs?.getString(TaskOptionsPrefConstants.PREF_DEFAULT_SORT_OLD, TaskOptionsPrefConstants.TaskSortValues.NONE)
			}
		} else {
			mPrefs.getString(Constants.prefTodoDefaultSort, TaskOptionsPrefConstants.TaskSortValues.NONE)
		}
		when (sortPref) {
			TaskOptionsPrefConstants.TaskSortValues.NONE -> loadTasksList()
			TaskOptionsPrefConstants.TaskSortValues.TITLE_DESC -> loadTasksList("title", Query.Direction.DESCENDING)
			TaskOptionsPrefConstants.TaskSortValues.TITLE_ASC -> loadTasksList("title", Query.Direction.ASCENDING)
			TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_NEW_TO_OLD -> loadTasksList("dueDate", Query.Direction.DESCENDING)
			TaskOptionsPrefConstants.TaskSortValues.DUE_DATE_OLD_TO_NEW -> loadTasksList("dueDate", Query.Direction.ASCENDING)
			else -> loadTasksList()
		}
	}

	/**
	 * Loads the task list
	 *
	 * @param fieldPath The field path to sort the list by
	 * @param direction The direction to sort the list in
	 */
	private fun loadTasksList(fieldPath: String? = null, direction: Query.Direction? = null) {
		// Reduce the amount of duplicate code by placing the listener into a variable
		val listener = EventListener<QuerySnapshot> { documentSnapshots, e ->
			if (e != null) {
				Log.e(TAG, "Listen failed!", e)
				return@EventListener
			}
			val taskItemList = ArrayList<TaskItem>()

			documentSnapshots?.mapTo(taskItemList) { it.toObjectWithId() }

			mAdapter = TasksAdapter(requireContext(), taskItemList)
			recyclerView.adapter = mAdapter
			mAdapter!!
					.setOnItemClickListener(object : TasksAdapter.OnItemClickListener {
						override fun onItemClick(item: TaskItem, position: Int) {
							Log.d(TAG, "Task: $item")
							Log.d(TAG, "Task ID: ${item.id}")
							startActivity<ViewTaskActivity> {
								putExtra("taskId", item.id)
							}
						}

						override fun onDeleteButtonClick(item: TaskItem, position: Int) {
							val builder = MaterialAlertDialogBuilder(context!!)
							builder
									.setTitle(R.string.todo_frag_delete_task_dialog_title)
									.setMessage(R.string.todo_frag_delete_task_dialog_msg)
									.setNegativeButton(R.string.dialog_action_cancel) { dialog, which -> dialog.dismiss() }
									.setPositiveButton(R.string.dialog_action_ok) { dialog, which ->
										mFirestore.document("users/" + mCurrentUser!!.uid + "/todos/" + item.id)
												.delete()
												.addOnCompleteListener { task ->
													if (task.isSuccessful) {
														findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout)?.let {
															Snackbar.make(
																	it,
																	"Successfully deleted todo!",
																	Snackbar.LENGTH_SHORT)
																	.show()
														}
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
							mFirestore.document("users/${mCurrentUser?.uid}/todos/${item.id}")
									.update("done", !item.done!!)
									.addOnCompleteListener { task ->
										val isDoneStr = if (item.done!!) "done" else "undone"
										if (task.isSuccessful) {
											findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout)?.let {
												Snackbar.make(
														it,
														"Successfully marked task as $isDoneStr!",
														Snackbar.LENGTH_SHORT)
														.setAnchorView(findParentActivityViewById<FloatingActionButton>(R.id.fab))
														.show()
											}
											mAdapter?.notifyItemChanged(position)
										} else {
											findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout)?.let {
												Snackbar.make(
														it,
														"An error occurred while attempting to mark the todo as $isDoneStr",
														Snackbar.LENGTH_LONG)
														.setAnchorView(findParentActivityViewById<FloatingActionButton>(R.id.fab))
														.show()
											}
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
			swipeRefreshLayout.isRefreshing = false
			if (documentSnapshots != null) {
				if (documentSnapshots.isEmpty) {
					Log.d(TAG, "Empty!")
					findViewById<View>(R.id.todoEmptyStateView)?.visibility = View.VISIBLE
					swipeRefreshLayout.visibility = View.GONE
				} else {
					Log.d(TAG, "Not Empty!")
					findViewById<View>(R.id.todoEmptyStateView)?.visibility = View.GONE
					swipeRefreshLayout.visibility = View.VISIBLE
				}
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
		swipeRefreshLayout.isRefreshing = true
		val collectionRef = mFirestore.collection("users/${mCurrentUser?.uid}/todos")
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

	private fun <T : View> findParentActivityViewById(@IdRes idRes: Int): T? {
		return mParentActivity?.findViewById(idRes)
	}

	private fun <T : View> findViewById(@IdRes idRes: Int): T? {
		return mFragmentView?.findViewById(idRes)
	}

	companion object {
		/**
		 * Request code for new task activity
		 */
		private const val ACTION_NEW_TASK = 1

		private const val SHARED_PREFS_FILE = "TodoFragPrefs"
	}
}
