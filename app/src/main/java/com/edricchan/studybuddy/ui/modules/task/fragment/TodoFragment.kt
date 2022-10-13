package com.edricchan.studybuddy.ui.modules.task.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.edit
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.constants.sharedprefs.TodoOptionsPrefConstants
import com.edricchan.studybuddy.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues
import com.edricchan.studybuddy.databinding.FragTodoBinding
import com.edricchan.studybuddy.extensions.TAG
import com.edricchan.studybuddy.extensions.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.extensions.showSnackbar
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
import com.edricchan.studybuddy.ui.widget.bottomsheet.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widget.bottomsheet.showModalBottomSheet
import com.edricchan.studybuddy.utils.SharedPrefUtils
import com.edricchan.studybuddy.utils.SharedUtils
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.recyclerview.ItemTouchDirection
import com.edricchan.studybuddy.utils.recyclerview.setItemTouchHelper
import com.google.android.material.color.MaterialColors
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

// FIXME: Fix whole code - it's very messy especially after migrating to Kotlin
class TodoFragment : Fragment() {
    private lateinit var adapter: TodosAdapter
    private var firestoreListener: ListenerRegistration? = null
    private lateinit var taskOptionsPrefs: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var parentActivity: AppCompatActivity
    private lateinit var prefs: SharedPreferences
    private lateinit var sharedPrefUtils: SharedPrefUtils
    private lateinit var uiUtils: UiUtils
    private lateinit var todoUtils: TodoUtils

    private var _binding: FragTodoBinding? = null
    private val binding get() = _binding!!

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_more_options -> {
                val context = requireContext()
                showModalBottomSheet {
                    setItems {
                        item(context.getString(R.string.menu_frag_task_refresh_todos_title)) {
                            setIcon(R.drawable.ic_refresh_24dp)
                            setItemClickListener {
                                loadTasksListHandler()
                                dismiss()
                            }
                        }
                        item(context.getString(R.string.menu_frag_task_sort_by_title)) {
                            setIcon(R.drawable.ic_sort_24dp)
                            setItemClickListener {
                                showSortByOptions()
                                dismiss()
                            }
                        }
                        item(context.getString(R.string.menu_settings_title)) {
                            setIcon(R.drawable.ic_settings_outline_24dp)
                            setItemClickListener {
                                startActivity<SettingsActivity>()
                                dismiss()
                            }
                        }
                        item(context.getString(R.string.menu_debug_title)) {
                            setIcon(R.drawable.ic_bug_report_outline_24dp)
                            visible = SharedUtils.isDevMode(context)
                            setItemClickListener {
                                startActivity<DebugActivity>()
                                dismiss()
                            }
                        }
                    }
                }
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (!BuildConfig.DEBUG) menu.removeItem(R.id.action_debug)
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
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
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
                        sharedPrefUtils.getSharedPrefsFile(TodoOptionsPrefConstants.FILE_TODO_OPTIONS)
                            .toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                    // Delete existing
                    Log.d(TAG, "Deleting previous shared preference file...")
                    val result =
                        Files.deleteIfExists(
                            sharedPrefUtils.getSharedPrefsFile(SHARED_PREFS_FILE).toPath()
                        )
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragTodoBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiUtils = UiUtils.getInstance(parentActivity)

        uiUtils.bottomAppBarFab?.setOnClickListener { newTaskActivity() }

        binding.apply {
            swipeRefreshLayout.apply {
                setColorSchemeColors(MaterialColors.getColor(this, R.attr.colorPrimary))
                setOnRefreshListener {
                    adapter.notifyDataSetChanged()
                    loadTasksListHandler()
                }
            }

            recyclerView.apply {
                setHasFixedSize(false)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                setItemTouchHelper(
                    listOf(ItemTouchDirection.None),
                    listOf(
                        ItemTouchDirection.Left,
                        ItemTouchDirection.Right
                    ),
                    onSwiped = { viewHolder, _ ->
                        todoUtils.removeTask(
                            this@TodoFragment.adapter.getItem(viewHolder.bindingAdapterPosition).id
                        )
                    }
                )
            }

            actionNewTodo.setOnClickListener { newTaskActivity() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

        firestoreListener?.remove()

        uiUtils.bottomAppBarFab?.setOnClickListener(null)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            Log.d(TAG, "Not logged in")
            requireContext().showMaterialAlertDialog {
                setTitle("Sign in")
                setMessage("To access the content, please login or register for an account.")
                setPositiveButton(R.string.dialog_action_login) { dialog, _ ->
                    startActivity<LoginActivity>()
                    dialog.dismiss()
                }
                setNeutralButton(R.string.dialog_action_sign_up) { dialog, _ ->
                    startActivity<RegisterActivity>()
                    dialog.dismiss()
                }
                setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                    dialog.cancel()
                }
            }
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
        val itemOptions = mapOf(
            itemNoneId to TodoSortValues.NONE,
            itemTitleAscId to TodoSortValues.TITLE_ASC,
            itemTitleDescId to TodoSortValues.TITLE_DESC,
            itemDueDateNewestId to TodoSortValues.DUE_DATE_NEW_TO_OLD,
            itemDueDateOldestId to TodoSortValues.DUE_DATE_OLD_TO_NEW
        )

        showModalBottomSheet("Sort tasks by...") {
            val context = this@TodoFragment.requireContext()
            group(100, {
                checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE
                setItemCheckedChangeListener {
                    taskOptionsPrefs.edit {
                        putString(
                            TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
                            itemOptions[it.id] ?: TodoSortValues.NONE
                        )
                    }
                    loadTasksListHandler()
                }
            }) {
                item(context.getString(R.string.sort_by_bottomsheet_none_title)) {
                    id = itemNoneId
                    setIcon(R.drawable.ic_close_24dp)
                }
                item(context.getString(R.string.sort_by_bottomsheet_title_asc_title)) {
                    id = itemTitleAscId
                    setIcon(R.drawable.ic_sort_ascending_24dp)
                }
                item(context.getString(R.string.sort_by_bottomsheet_title_desc_title)) {
                    id = itemTitleDescId
                    setIcon(R.drawable.ic_sort_descending_24dp)
                }
                item(context.getString(R.string.sort_by_bottomsheet_due_date_newest_title)) {
                    id = itemDueDateNewestId
                    setIcon(R.drawable.ic_sort_descending_24dp)
                }
                item(context.getString(R.string.sort_by_bottomsheet_due_date_oldest_title)) {
                    id = itemDueDateOldestId
                    setIcon(R.drawable.ic_sort_ascending_24dp)
                }
            }
        }
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
        val sortPref = taskOptionsPrefs.getString(
            TodoOptionsPrefConstants.PREF_DEFAULT_SORT,
            TodoSortValues.NONE
        )

        val sortMap = mapOf(
            TodoSortValues.NONE to null,
            TodoSortValues.TITLE_ASC to ("title" to Query.Direction.ASCENDING),
            TodoSortValues.TITLE_DESC to ("title" to Query.Direction.DESCENDING),
            TodoSortValues.DUE_DATE_NEW_TO_OLD to ("dueDate" to Query.Direction.DESCENDING),
            TodoSortValues.DUE_DATE_OLD_TO_NEW to ("dueDate" to Query.Direction.ASCENDING)
        )

        loadTasksList(sortMap[sortPref]?.first, sortMap[sortPref]?.second)
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
                    requireContext().showMaterialAlertDialog {
                        setTitle(R.string.todo_frag_delete_task_dialog_title)
                        setMessage(R.string.todo_frag_delete_task_dialog_msg)
                        setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
                        setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
                            todoUtils.removeTask(item.id)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayoutMain)?.let {
                                            showSnackbar(
                                                it,
                                                "Successfully deleted todo!",
                                                Snackbar.LENGTH_SHORT
                                            )
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
                    }
                }

                override fun onMarkAsDoneButtonClick(item: TodoItem, position: Int) {
                    todoUtils.getTask(item.id)
                        .update("done", !item.done!!)
                        .addOnCompleteListener { task ->
                            val isDoneStr = if (item.done) "done" else "undone"
                            if (task.isSuccessful) {
                                findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayoutMain)?.let {
                                    showSnackbar(
                                        it,
                                        "Successfully marked task as $isDoneStr!",
                                        Snackbar.LENGTH_SHORT
                                    ) {
                                        anchorView =
                                            findParentActivityViewById<FloatingActionButton>(
                                                R.id.fab
                                            )
                                    }
                                }
                                adapter.notifyItemChanged(position)
                            } else {
                                findParentActivityViewById<CoordinatorLayout>(R.id.coordinatorLayoutMain)?.let {
                                    showSnackbar(
                                        it,
                                        "An error occurred while attempting to mark the todo as $isDoneStr",
                                        Snackbar.LENGTH_LONG
                                    ) {
                                        anchorView =
                                            findParentActivityViewById<FloatingActionButton>(
                                                R.id.fab
                                            )
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

            binding.apply {
                recyclerView.adapter = adapter

                swipeRefreshLayout.apply {
                    isRefreshing = false
                    if (snapshot != null) {
                        isGone = snapshot.isEmpty
                        todoEmptyStateView.isVisible = snapshot.isEmpty
                    }
                }
            }
        }
        binding.swipeRefreshLayout.isRefreshing = true
        firestoreListener = todoUtils.taskCollectionRef.run {
            if (fieldPath != null && direction != null) orderBy(fieldPath, direction)
            addSnapshotListener(listener)
        }
    }

    private fun <T : View> findParentActivityViewById(@IdRes idRes: Int): T? {
        return parentActivity.findViewById(idRes)
    }

    companion object {
        /**
         * Request code for new task activity
         */
        private const val ACTION_NEW_TASK = 1

        private const val SHARED_PREFS_FILE = "TodoFragPrefs"
    }
}
