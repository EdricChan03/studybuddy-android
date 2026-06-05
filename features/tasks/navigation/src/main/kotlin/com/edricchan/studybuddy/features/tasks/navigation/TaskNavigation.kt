package com.edricchan.studybuddy.features.tasks.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * Navigates to the [tasksGraph]'s [TaskDestination.TaskGraphRoot] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToTaskGraph(
    builder: NavOptionsBuilder.() -> Unit = { launchSingleTop = true }
) = navigate(TaskDestination.TaskGraphRoot, builder)

/** Navigates to the [TaskDestination.ListTasks] route. */
fun NavController.navigateToTasksList(
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(TaskDestination.ListTasks, builder)

/** Navigates to the [TaskDestination.ViewTask] route with the given [taskId]. */
fun NavController.navigateToViewTask(
    taskId: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(TaskDestination.ViewTask(taskId), builder)

/** Navigates to the [TaskDestination.EditTask] route with the given [taskId]. */
fun NavController.navigateToEditTask(
    taskId: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(TaskDestination.EditTask(taskId), builder)

/** Navigates to the [TaskDestination.NewTask] route. */
fun NavController.navigateToCreateTask(
    builder: NavOptionsBuilder.() -> Unit = {}
) = navigate(TaskDestination.NewTask, builder)
