package com.edricchan.studybuddy.features.tasks.list.ui.compat

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.edricchan.studybuddy.core.compat.navigation.navigateToDebug
import com.edricchan.studybuddy.core.compat.navigation.navigateToHelp
import com.edricchan.studybuddy.core.compat.navigation.navigateToSettings
import com.edricchan.studybuddy.core.resources.icons.AppIcons
import com.edricchan.studybuddy.core.resources.icons.compat.BugReport
import com.edricchan.studybuddy.core.resources.icons.compat.Help
import com.edricchan.studybuddy.core.resources.icons.compat.Plus
import com.edricchan.studybuddy.core.resources.icons.compat.Refresh
import com.edricchan.studybuddy.core.resources.icons.compat.Settings
import com.edricchan.studybuddy.exts.common.TAG
import com.edricchan.studybuddy.features.auth.navigation.navigateToLogin
import com.edricchan.studybuddy.features.auth.navigation.navigateToRegister
import com.edricchan.studybuddy.features.tasks.R
import com.edricchan.studybuddy.features.tasks.list.ui.ListTasksScreen
import com.edricchan.studybuddy.features.tasks.migrations.TasksMigrator
import com.edricchan.studybuddy.features.tasks.navigation.navigateToCreateTask
import com.edricchan.studybuddy.features.tasks.navigation.navigateToViewTask
import com.edricchan.studybuddy.features.tasks.vm.TasksListViewModel
import com.edricchan.studybuddy.ui.common.dialogs.showAuthRequiredDialog
import com.edricchan.studybuddy.ui.common.fab.FabConfig
import com.edricchan.studybuddy.ui.common.fragment.ComposableFragment
import com.edricchan.studybuddy.ui.widgets.modalbottomsheet.views.showModalBottomSheet
import com.edricchan.studybuddy.utils.dev.isDevMode
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.edricchan.studybuddy.core.resources.R as CoreResR

// FIXME: Fix whole code - it's very messy especially after migrating to Kotlin
@AndroidEntryPoint
class TaskListFragment : ComposableFragment() {
    @Inject
    lateinit var auth: FirebaseAuth

    private val viewModel by viewModels<TasksListViewModel>()

    override val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
            // Clear the main activity's menu before inflating the fragment's menu
            menu.clear()
            inflater.inflate(R.menu.menu_frag_todo, menu)
        }

        override fun onMenuItemSelected(item: MenuItem) = when (item.itemId) {
            R.id.action_show_more_options -> {
                val context = requireContext()
                showModalBottomSheet {
                    item(context.getString(R.string.menu_frag_task_refresh_todos_title)) {
                        setIcon(AppIcons.Compat.Refresh.iconRes)
                        setItemClickListener {
                            viewModel.requestRefresh()
                        }
                    }
                    item(context.getString(CoreResR.string.menu_settings_title)) {
                        setIcon(AppIcons.Compat.Settings.iconRes)
                        setItemClickListener {
                            navController.navigateToSettings()
                        }
                    }
                    item(context.getString(CoreResR.string.menu_help_title)) {
                        setIcon(AppIcons.Compat.Help.iconRes)
                        setItemClickListener {
                            navController.navigateToHelp()
                        }
                    }
                    item(context.getString(CoreResR.string.menu_debug_title)) {
                        setIcon(AppIcons.Compat.BugReport.iconRes)
                        visible = context.isDevMode()
                        setItemClickListener {
                            navController.navigateToDebug()
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

    @Composable
    override fun Content(modifier: Modifier) {
        ListTasksScreen(
            modifier = modifier.fillMaxSize(),
            viewModel = viewModel,
            contentPadding = PaddingValues(16.dp),
            onNavigateToItemView = {
                navController.navigateToViewTask(it.id)
            }
        )
    }

    override val fabConfig = FabConfig(
        iconRes = AppIcons.Compat.Plus.iconRes,
        contentDescriptionRes = R.string.action_create_task,
        onClick = ::newTaskActivity
    )

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            Log.d(TAG, "Not logged in")
            requireContext().showAuthRequiredDialog(
                onNavigateToRegister = navController::navigateToRegister,
                onNavigateToLogin = navController::navigateToLogin
            )
        }
    }

    private fun newTaskActivity() {
        navController.navigateToCreateTask()
    }
}
