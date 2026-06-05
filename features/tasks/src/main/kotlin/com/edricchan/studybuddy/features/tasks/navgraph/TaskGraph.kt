package com.edricchan.studybuddy.features.tasks.navgraph

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.fragment.fragment
import androidx.navigation.navigation
import com.edricchan.studybuddy.features.tasks.compat.ui.fragment.TaskDetailFragment
import com.edricchan.studybuddy.features.tasks.create.ui.compat.NewTaskFragment
import com.edricchan.studybuddy.features.tasks.edit.ui.compat.EditTaskFragment
import com.edricchan.studybuddy.features.tasks.list.ui.compat.TaskListFragment
import com.edricchan.studybuddy.features.tasks.navigation.TaskDestination
import com.edricchan.studybuddy.features.tasks.navigation.R as NavR

/**
 * Adds the tasks feature's NavGraph to the receiver [NavGraphBuilder].
 * @see TaskDestination
 */
context(context: Context)
fun NavGraphBuilder.taskGraph() = navigation<TaskDestination.TaskGraphRoot>(
    startDestination = TaskDestination.ListTasks::class
) {
    fragment<TaskListFragment, TaskDestination.ListTasks> {
        label = context.getString(NavR.string.nav_tasks_dest_list_label)
    }

    fragment<NewTaskFragment, TaskDestination.NewTask> {
        deepLink {
            action = NewTaskFragment.ACTION_NEW_TASK_SHORTCUT
        }
        label = context.getString(NavR.string.nav_tasks_dest_new_label)
    }

    fragment<TaskDetailFragment, TaskDestination.ViewTask> {
        label = context.getString(NavR.string.nav_tasks_dest_view_label)
    }

    fragment<EditTaskFragment, TaskDestination.EditTask> {
        label = context.getString(NavR.string.nav_tasks_dest_edit_label)
    }
}
