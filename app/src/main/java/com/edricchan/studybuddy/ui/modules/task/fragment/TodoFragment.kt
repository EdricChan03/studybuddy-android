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
import com.edricchan.studybuddy.constants.sharedprefs.TodoOptionsPrefConstants
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.extensions.startActivityForResult
import com.edricchan.studybuddy.interfaces.TodoItem
import com.edricchan.studybuddy.ui.modules.auth.LoginActivity
import com.edricchan.studybuddy.ui.modules.auth.RegisterActivity
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.ViewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.adapter.TodosAdapter
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetAdapter
import com.edricchan.studybuddy.ui.widget.bottomsheet.ModalBottomSheetFragment
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetItem
import com.edricchan.studybuddy.utils.SharedPrefUtils
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.UiUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.frag_todo.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

// FIXME: Fix whole code - it's very messy especially after migrating to Kotlin
class TodoFragment : Fragment(R.layout.frag_todo) {
    private lateinit var adapter: TodosAdapter
    private var firestoreListener: ListenerRegistration? = null
    private lateinit var fragmentView: View
    private lateinit var taskOptionsPrefs: SharedPreferences
    //	private var mSelectionTracker: SelectionTracker<String>? = null
    //	private var mActionModeCallback: ActionMode.Callback? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var parentActivity: AppCompatActivity
    private lateinit var prefs: SharedPreferences
    private lateinit var sharedPrefUtils: SharedPrefUtils
    private lateinit var uiUtils: UiUtils
    private lateinit var todoUtils: TodoUtils

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
                        icon = R.drawable.ic_bug_report_outline_24dp,
                        visible = SharedUtils.isDevMode(requireContext()),
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
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPrefUtils = SharedPrefUtils(requireContext())
        // Checks if old preference file exists
        // TODO: Move to a separate class
        if (sharedPrefUtils.sharedPrefFileExists(SHARED_PREFS_FILE)) {
            Log.d(TAG, "Migrating shared preference file...")
            // Rename existing file to new file
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Use better implementation of renaming file
                try {
                    Files.move(
                        sharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE).toPath(),
                        sharedPrefUtils.getSharedPrefsFile(TodoOptionsPrefConstants.FILE_TODO_OPTIONS).toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                    // Delete existing
                    Log.d(TAG, "Deleting previous shared preference file...")
                    val result =
                        Files.deleteIfExists(sharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE).toPath())
                    if (result) {
                        Log.d(TAG, "Successfully deleted previous shared preference file!")
                    } else {
                        Log.e(
                            TAG,
                            "Something went wrong while attempting to delete the previous shared preference file."
                        )
                    }
                } catch (e: IOException) {
                    Log.e(
                        TAG,
                        "An error occurred while attempting to migrate the shared preference file:",
                        e
                    )
                }
            } else {
                val result = sharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE)
                    .renameTo(sharedPrefUtils.getSharedPrefsFile(TodoOptionsPrefConstants.FILE_TODO_OPTIONS))
                if (result) {
                    Log.d(TAG, "Successfully migrated shared preference file!")
                } else {
                    Log.e(
                        TAG,
                        "Something went wrong while attempting to migrate the shared preference file."
                    )
                }
            }
        }
        // Migrate keys and their values
        taskOptionsPrefs = requireContext().getSharedPreferences(
            TodoOptionsPrefConstants.FILE_TODO_OPTIONS,
            Context.MODE_PRIVATE
        )
        migrateTaskOptsPrefs()
        firestore = Firebase.firestore
        auth = Firebase.auth
        todoUtils = TodoUtils.getInstance(auth, firestore)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*if (savedInstanceState != null && mSelectionTracker != null) {
            mSelectionTracker!!.onRestoreInstanceState(savedInstanceState)
        }*/

        fragmentView = view

        uiUtils = UiUtils.getInstance(parentActivity)

        uiUtils.bottomAppBarFab?.setOnClickListener { newTaskActivity() }

        swipeRefreshLayout.apply {
            setColorSchemeResources(R.color.colorPrimary)
            setOnRefreshListener {
                adapter?.notifyDataSetChanged()
                loadTasksListHandler()
            }
        }

        recyclerView.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
        }
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (adapter != null) {
                    todoUtils.removeTask(adapter!!.todoItemList[viewHolder.adapterPosition].id)
                }
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

        if (firestoreListener != null) {
            firestoreListener?.remove()
        }

        uiUtils.bottomAppBarFab?.setOnClickListener(null)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            Log.d(TAG, "Not logged in")
            val signInDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            signInDialogBuilder.setTitle("Sign in")
                .setMessage("To access the content, please login or register for an account.")
                .setPositiveButton(R.string.dialog_action_login) { dialog, _ ->
                    startActivity<LoginActivity>()
                    dialog.dismiss()
                }
                .setNeutralButton(R.string.dialog_action_sign_up) { dialog, _ ->
                    startActivity<RegisterActivity>()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .show()

        } else {
            loadTasksList()
        }
    }

    override fun onAttach(context: Context) {
        parentActivity = context as AppCompatActivity
        super.onAttach(context as Context)
    }

    private fun showSortByOptions() {
        val itemNoneId = 1
        val itemTitleAscId = 2
        val itemTitleDescId = 3
        val itemDueDateNewestId = 4
        val itemDueDateOldestId = 5
        val group = ModalBottomSheetGroup(id = 100,
            checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE,
            onItemCheckedChangeListener = object :
                ModalBottomSheetAdapter.OnItemCheckedChangeListener {
                override fun onItemCheckedChange(item: ModalBottomSheetItem) {
                    when (item.id) {
                        itemNoneId -> {
                            taskOptionsPrefs.edit {
                                putString(
                                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                                    TodoOptionsPrefConstants.TodoSortValues.NONE
                                )
                            }
                        }
                        itemTitleDescId -> {
                            taskOptionsPrefs.edit {
                                putString(
                                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                                    TodoOptionsPrefConstants.TodoSortValues.TITLE_DESC
                                )
                            }
                        }
                        itemTitleAscId -> {
                            taskOptionsPrefs.edit {
                                putString(
                                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                                    TodoOptionsPrefConstants.TodoSortValues.TITLE_ASC
                                )
                            }
                        }
                        itemDueDateNewestId -> {
                            taskOptionsPrefs.edit {
                                putString(
                                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                                    TodoOptionsPrefConstants.TodoSortValues.DUE_DATE_NEW_TO_OLD
                                )
                            }
                        }
                        itemDueDateOldestId -> {
                            taskOptionsPrefs.edit {
                                putString(
                                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                                    TodoOptionsPrefConstants.TodoSortValues.DUE_DATE_OLD_TO_NEW
                                )
                            }
                        }
                    }
                    loadTasksListHandler()
                }
            })
        val items = arrayOf(
            ModalBottomSheetItem(
                id = itemNoneId, title = getString(R.string.sort_by_bottomsheet_none_title),
                icon = R.drawable.ic_close_24dp, group = group
            ),
            ModalBottomSheetItem(
                id = itemTitleAscId,
                title = getString(R.string.sort_by_bottomsheet_title_asc_title),
                icon = R.drawable.ic_sort_ascending_24dp,
                group = group
            ),
            ModalBottomSheetItem(
                id = itemTitleDescId,
                title = getString(R.string.sort_by_bottomsheet_title_desc_title),
                icon = R.drawable.ic_sort_descending_24dp,
                group = group
            ),
            ModalBottomSheetItem(
                id = itemDueDateNewestId,
                title = getString(R.string.sort_by_bottomsheet_due_date_newest_title),
                icon = R.drawable.ic_sort_descending_24dp, group = group
            ),
            ModalBottomSheetItem(
                id = itemDueDateOldestId,
                title = getString(R.string.sort_by_bottomsheet_due_date_oldest_title),
                icon = R.drawable.ic_sort_ascending_24dp, group = group
            )
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
        if (taskOptionsPrefs.getString(
                TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD,
                null
            ) != null
        ) {
            Log.d(TAG, "Old task sorting tag still exists.")
            // Old SharedPreference key still exists
            val oldValue = taskOptionsPrefs.getString(
                TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD,
                TodoOptionsPrefConstants.TodoSortValues.NONE
            )
            taskOptionsPrefs.edit {
                putString(TodoOptionsPrefConstants.PREF_DEFAULT_SORT, oldValue)
                remove(TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD)
            }
        }
    }

    private fun loadTasksListHandler() {
        @Suppress("MoveVariableDeclarationIntoWhen")
        val sortPref = if (prefs.getString(Constants.prefTodoDefaultSort, null) == null) {
            // TODO: Migrate old key's value to new key
            if (taskOptionsPrefs.getString(
                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD,
                    null
                ) == null
            ) {
                taskOptionsPrefs.getString(
                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                    TodoOptionsPrefConstants.TodoSortValues.NONE
                )
            } else {
                taskOptionsPrefs.getString(
                    TodoOptionsPrefConstants.PREF_DEFAULT_SORT_OLD,
                    TodoOptionsPrefConstants.TodoSortValues.NONE
                )
            }
        } else {
            prefs.getString(
                Constants.prefTodoDefaultSort,
                TodoOptionsPrefConstants.TodoSortValues.NONE
            )
        }
        when (sortPref) {
            TodoOptionsPrefConstants.TodoSortValues.NONE -> loadTasksList()
            TodoOptionsPrefConstants.TodoSortValues.TITLE_DESC -> loadTasksList(
                "title",
                Query.Direction.DESCENDING
            )
            TodoOptionsPrefConstants.TodoSortValues.TITLE_ASC -> loadTasksList(
                "title",
                Query.Direction.ASCENDING
            )
            TodoOptionsPrefConstants.TodoSortValues.DUE_DATE_NEW_TO_OLD -> loadTasksList(
                "dueDate",
                Query.Direction.DESCENDING
            )
            TodoOptionsPrefConstants.TodoSortValues.DUE_DATE_OLD_TO_NEW -> loadTasksList(
                "dueDate",
                Query.Direction.ASCENDING
            )
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
        val listener = EventListener<QuerySnapshot> { snapshot, e ->
            if (e != null) {
                Log.e(TAG, "Listen failed!", e)
                return@EventListener
            }
            val taskItemList = snapshot?.toObjects<TodoItem>() ?: listOf()

            val itemListener = object : TodosAdapter.OnItemClickListener {
                override fun onItemClick(item: TodoItem, position: Int) {
                    Log.d(TAG, "Task: $item")
                    Log.d(TAG, "Task ID: ${item.id}")
                    startActivity<ViewTaskActivity> {
                        putExtra(ViewTaskActivity.EXTRA_TASK_ID, item.id)
                    }
                }

                override fun onDeleteButtonClick(item: TodoItem, position: Int) {
                    val builder = MaterialAlertDialogBuilder(context!!)
                    builder
                        .setTitle(R.string.todo_frag_delete_task_dialog_title)
                        .setMessage(R.string.todo_frag_delete_task_dialog_msg)
                        .setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
                            todoUtils.removeTask(item.id)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout)?.let {
                                            Snackbar.make(
                                                it,
                                                "Successfully deleted todo!",
                                                Snackbar.LENGTH_SHORT
                                            )
                                                .show()
                                        }
                                        adapter.notifyItemRemoved(position)
                                        dialog.dismiss()
                                    } else {
                                        Log.e(
                                            TAG,
                                            "An error occurred while attempting to delete the todo:",
                                            task.exception
                                        )
                                    }
                                }
                        }
                        .show()
                }

                override fun onMarkAsDoneButtonClick(item: TodoItem, position: Int) {
                    todoUtils.getTask(item.id)
                        .update("done", !item.done!!)
                        .addOnCompleteListener { task ->
                            val isDoneStr = if (item.done) "done" else "undone"
                            if (task.isSuccessful) {
                                findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout)?.let {
                                    Snackbar.make(
                                        it,
                                        "Successfully marked task as $isDoneStr!",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .setAnchorView(
                                            findParentActivityViewById<FloatingActionButton>(
                                                R.id.fab
                                            )
                                        )
                                        .show()
                                }
                                adapter.notifyItemChanged(position)
                            } else {
                                findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayout).let {
                                    if (it != null) {
                                        Snackbar.make(
                                            it,
                                            "An error occurred while attempting to mark the todo as $isDoneStr",
                                            Snackbar.LENGTH_LONG
                                        )
                                            .setAnchorView(
                                                findParentActivityViewById<FloatingActionButton>(
                                                    R.id.fab
                                                )
                                            )
                                            .show()
                                    }
                                }
                                Log.e(
                                    TAG,
                                    "An error occurred while attempting to mark the todo as $isDoneStr:",
                                    task.exception
                                )
                            }
                        }
                }
            }
            adapter = TodosAdapter(requireContext(), taskItemList, itemListener)
            recyclerView.adapter = adapter
            /*if (mSelectionTracker == null) {
                mSelectionTracker = SelectionTracker.Builder(
                        "selection-id",
                        mRecyclerView!!,
                        TodoItemKeyProvider(taskItemList),
                        TodoItemLookup(mRecyclerView!!),
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
            if (snapshot != null) {
                if (snapshot.isEmpty) {
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
        val collectionRef = todoUtils.taskCollectionRef
        firestoreListener = if (fieldPath != null && direction != null) {
            collectionRef.orderBy(fieldPath, direction)
                .addSnapshotListener(listener)
        } else {
            collectionRef.addSnapshotListener(listener)
        }
    }

    private fun <T : View> findParentActivityViewById(@IdRes idRes: Int): T? {
        return parentActivity.findViewById(idRes)
    }

    private fun <T : View> findViewById(@IdRes idRes: Int): T? {
        return fragmentView.findViewById(idRes)
    }

    companion object {
        /**
         * Request code for new task activity
         */
        private const val ACTION_NEW_TASK = 1

        private const val SHARED_PREFS_FILE = "TodoFragPrefs"
    }
}
