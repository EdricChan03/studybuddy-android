package com.edricchan.studybuddy.ui.modules.task.fragment

import android.content.Context
import android.content.SharedPreferences
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
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.FragTodoBinding
import com.edricchan.studybuddy.extensions.startActivity
import com.edricchan.studybuddy.exts.androidx.preference.defaultSharedPreferences
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.exts.material.snackbar.showSnackbar
import com.edricchan.studybuddy.features.help.HelpActivity
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.migrations.TasksMigrator
import com.edricchan.studybuddy.ui.dialogs.showAuthRequiredDialog
import com.edricchan.studybuddy.ui.modules.debug.DebugActivity
import com.edricchan.studybuddy.ui.modules.settings.SettingsActivity
import com.edricchan.studybuddy.ui.modules.task.NewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.ViewTaskActivity
import com.edricchan.studybuddy.ui.modules.task.adapter.TodosAdapter
import com.edricchan.studybuddy.ui.modules.task.utils.TodoUtils
import com.edricchan.studybuddy.ui.theming.dynamicColorPrimary
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.showModalBottomSheet
import com.edricchan.studybuddy.utils.UiUtils
import com.edricchan.studybuddy.utils.isDevMode
import com.edricchan.studybuddy.utils.recyclerview.ItemTouchDirection
import com.edricchan.studybuddy.utils.recyclerview.setItemTouchHelper
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
import kotlinx.coroutines.launch

// FIXME: Fix whole code - it's very messy especially after migrating to Kotlin
class TodoFragment : Fragment() {
    private lateinit var adapter: TodosAdapter
    private var firestoreListener: ListenerRegistration? = null
    private lateinit var taskOptionsPrefs: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var parentActivity: AppCompatActivity

    private lateinit var uiUtils: UiUtils
    private lateinit var todoUtils: TodoUtils

    private var _binding: FragTodoBinding? = null
    private val binding get() = _binding!!

    private val menuProvider = object : MenuProvider {
        override fun onPrepareMenu(menu: Menu) {
            if (!BuildConfig.DEBUG) menu.removeItem(R.id.action_debug)
        }

        override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
            // Clear the main activity's menu before inflating the fragment's menu
            menu.clear()
            inflater.inflate(R.menu.menu_frag_todo, menu)
        }

        override fun onMenuItemSelected(item: MenuItem) = when (item.itemId) {
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
                        item(context.getString(R.string.menu_help_title)) {
                            setIcon(R.drawable.ic_help_outline_24dp)
                            setItemClickListener {
                                startActivity<HelpActivity>()
                                dismiss()
                            }
                        }
                        item(context.getString(R.string.menu_debug_title)) {
                            setIcon(R.drawable.ic_bug_report_outline_24dp)
                            visible = context.isDevMode()
                            setItemClickListener {
                                startActivity<DebugActivity>()
                                dismiss()
                            }
                        }
                    }
                }
                true
            }

            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Perform migrations if necessary
        lifecycleScope.launch {
            TasksMigrator(requireContext()).migrate()
        }

        taskOptionsPrefs = requireContext().getSharedPreferences(
            TodoOptionsPrefConstants.FILE_TODO_OPTIONS,
            Context.MODE_PRIVATE
        )
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
        activity?.addMenuProvider(menuProvider, viewLifecycleOwner)

        uiUtils = UiUtils.getInstance(parentActivity)

        uiUtils.bottomAppBarFab?.setOnClickListener { newTaskActivity() }

        binding.apply {
            swipeRefreshLayout.apply {
                setColorSchemeColors(requireContext().dynamicColorPrimary)
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
                            this@TodoFragment.adapter.currentList[viewHolder.bindingAdapterPosition].id
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
            requireContext().showAuthRequiredDialog()
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
        startActivity<NewTaskActivity>()
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
                    val updatedDone = !(item.done ?: false)
                    todoUtils.getTask(item.id)
                        .update("done", updatedDone)
                        .addOnCompleteListener { task ->
                            val isDoneStr = if (updatedDone) "done" else "undone"
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

}
