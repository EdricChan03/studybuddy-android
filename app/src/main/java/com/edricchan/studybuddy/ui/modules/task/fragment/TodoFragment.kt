package com.edricchan.studybuddy.ui.modules.task.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.edricchan.studybuddy.BuildConfig
import com.edricchan.studybuddy.R
import com.edricchan.studybuddy.databinding.FragTodoBinding
import com.edricchan.studybuddy.exts.androidx.viewbinding.viewInflateBinding
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.exts.material.dialog.showMaterialAlertDialog
import com.edricchan.studybuddy.features.tasks.compat.ui.adapter.TodosAdapter
import com.edricchan.studybuddy.features.tasks.compat.ui.adapter.itemListener
import com.edricchan.studybuddy.features.tasks.constants.sharedprefs.TodoOptionsPrefConstants.TodoSortValues
import com.edricchan.studybuddy.features.tasks.data.model.TodoItem
import com.edricchan.studybuddy.features.tasks.migrations.TasksMigrator
import com.edricchan.studybuddy.features.tasks.vm.TasksListViewModel
import com.edricchan.studybuddy.navigation.compat.navigateToDebug
import com.edricchan.studybuddy.navigation.compat.navigateToHelp
import com.edricchan.studybuddy.navigation.compat.navigateToSettings
import com.edricchan.studybuddy.navigation.compat.task.navigateToCreateTask
import com.edricchan.studybuddy.navigation.compat.task.navigateToTaskView
import com.edricchan.studybuddy.ui.common.MainViewModel
import com.edricchan.studybuddy.ui.common.SnackBarData
import com.edricchan.studybuddy.ui.dialogs.showAuthRequiredDialog
import com.edricchan.studybuddy.ui.theming.dynamicColorPrimary
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.dsl.items
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.interfaces.ModalBottomSheetGroup
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.showModalBottomSheet
import com.edricchan.studybuddy.utils.dev.isDevMode
import com.edricchan.studybuddy.utils.recyclerview.ItemTouchDirection
import com.edricchan.studybuddy.utils.recyclerview.setItemTouchHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

// FIXME: Fix whole code - it's very messy especially after migrating to Kotlin
@AndroidEntryPoint
class TodoFragment : Fragment() {
    private lateinit var adapter: TodosAdapter

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    private val binding by viewInflateBinding(FragTodoBinding::inflate)

    private val viewModel by viewModels<TasksListViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    private lateinit var navController: NavController

    private val itemListener = itemListener(
        onItemClick = { item, _ ->
            navController.navigateToTaskView(item.id)
        },
        onDeleteButtonClick = { item, _ ->
            requireContext().showMaterialAlertDialog {
                setTitle(R.string.todo_frag_delete_task_dialog_title)
                setMessage(R.string.todo_frag_delete_task_dialog_msg)
                setNegativeButton(R.string.dialog_action_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                setPositiveButton(R.string.dialog_action_ok) { dialog, _ ->
                    onRemoveTask(item, onSuccess = { dialog.dismiss() })
                }
            }
        },
        onMarkAsDoneButtonClick = { item, _ ->
            onToggleTaskDone(item)
        }
    )

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
                                binding.swipeRefreshLayout.isRefreshing = true
                                lifecycleScope.launch {
                                    viewModel.refresh()
                                    binding.swipeRefreshLayout.isRefreshing = false
                                }
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
                                navController.navigateToSettings()
                                dismiss()
                            }
                        }
                        item(context.getString(R.string.menu_help_title)) {
                            setIcon(R.drawable.ic_help_outline_24dp)
                            setItemClickListener {
                                navController.navigateToHelp()
                                dismiss()
                            }
                        }
                        item(context.getString(R.string.menu_debug_title)) {
                            setIcon(R.drawable.ic_bug_report_outline_24dp)
                            visible = context.isDevMode()
                            setItemClickListener {
                                navController.navigateToDebug()
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
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        activity?.addMenuProvider(menuProvider, viewLifecycleOwner)

        mainViewModel.fab.setOnClickListener { newTaskActivity() }

        adapter = TodosAdapter(context = requireContext(), itemListener = itemListener)

        binding.apply {
            swipeRefreshLayout.apply {
                setColorSchemeColors(requireContext().dynamicColorPrimary)
                setOnRefreshListener {
                    lifecycleScope.launch {
                        viewModel.refresh()
                        // Stop refreshing
                        // Add a delay as Firestore retrieves its data really quickly
                        // most of the time (so we don't get a jittery progress spinner)
                        delay(500)
                        isRefreshing = false
                    }
                }
            }

            recyclerView.apply {
                setHasFixedSize(false)
                layoutManager = LinearLayoutManager(context)
                itemAnimator = DefaultItemAnimator()
                adapter = this@TodoFragment.adapter
                setItemTouchHelper(
                    listOf(ItemTouchDirection.None),
                    listOf(
                        ItemTouchDirection.Left,
                        ItemTouchDirection.Right
                    ),
                    onSwiped = { viewHolder, _ ->
                        onRemoveTask(this@TodoFragment.adapter.currentList[viewHolder.bindingAdapterPosition])
                    }
                )
            }

            actionNewTodo.setOnClickListener { newTaskActivity() }

            lifecycleScope.launch {
                viewModel.tasks.flowWithLifecycle(lifecycle).collect {
                    todoEmptyStateView.isVisible = it.isEmpty()
                    swipeRefreshLayout.isVisible = it.isNotEmpty()
                    adapter.submitList(it)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        mainViewModel.fab.setOnClickListener(null)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            Log.d(TAG, "Not logged in")
            requireContext().showAuthRequiredDialog()
        }
    }

    private fun onRemoveTask(
        item: TodoItem,
        onSuccess: () -> Unit = {},
        onFail: (e: Exception) -> Unit = {}
    ) {
        lifecycleScope.launch {
            try {
                viewModel.removeTask(item)
                onSuccess()
            } catch (e: Exception) {
                showSnackbar(R.string.task_delete_fail_msg)
                Log.e(
                    TAG,
                    "An error occurred while attempting to delete the todo:",
                    e
                )
                onFail(e)
            }
        }
    }

    private fun onToggleTaskDone(
        item: TodoItem,
        onSuccess: () -> Unit = {},
        onFail: (e: Exception) -> Unit = {}
    ) {
        lifecycleScope.launch {
            // The pending updated isDone value to use. This is its flipped value
            val updatedDone = item.done != true
            val isDoneStr = if (updatedDone) "done" else "undone"
            try {
                viewModel.toggleTaskDone(item)
                onSuccess()
            } catch (e: Exception) {
                showSnackbar(
                    if (updatedDone) R.string.task_done_fail_msg else R.string.task_undone_fail_msg,
                    SnackBarData.Duration.Long
                )
                Log.e(
                    TAG,
                    "An error occurred while attempting to mark the todo as $isDoneStr:",
                    e
                )
                onFail(e)
            }
        }
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

        // The DSL currently does not support selection via ID(s) within the group builder,
        // so we have to do this manually for now
        // FIXME: Remove workaround
        val sheetItems = items {
            val context = requireContext()

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

        showModalBottomSheet(R.string.task_sort_dialog_header_title) {
            group(100, {
                checkableBehavior = ModalBottomSheetGroup.CHECKABLE_BEHAVIOR_SINGLE
                setItemCheckedChangeListener {
                    lifecycleScope.launch {
                        val noneValue = TodoSortValues.NONE
                        val newValue =
                            if (it.group?.selected?.contains(it) == true) itemOptions[it.id]
                                ?: noneValue else noneValue
                        viewModel.updateSort(newValue)
                    }
                }
                // Set selection status
                val selectedId =
                    itemOptions.entries.find { viewModel.compatQuery.value == it.value }?.key
                        ?: itemNoneId
                selected += sheetItems.first { it.id == selectedId }
            }) {
                items(sheetItems)
            }
        }
    }

    private fun newTaskActivity() {
        navController.navigateToCreateTask()
    }

    private fun showSnackbar(
        @StringRes textRes: Int,
        duration: SnackBarData.Duration = SnackBarData.Duration.Short
    ) {
        lifecycleScope.launch {
            mainViewModel.showSnackBar(
                messageRes = textRes,
                duration = duration
            )
        }
    }
}
