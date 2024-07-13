package com.edricchan.studybuddy.core.compat.navigation.task

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.DefaultNavOptionsBuilder

/**
 * Navigates to the [CompatDestination.Task.Root] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToTaskRoot(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Task.Root, builder)

/**
 * Navigates to the [CompatDestination.Task.List] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToTasksList(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Task.List, builder)

/**
 * Navigates to the [CompatDestination.Task.New] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToCreateTask(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Task.New, builder)

/**
 * Navigates to the [CompatDestination.Task.View] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToTaskView(
    taskId: String,
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Task.View(taskId), builder)

/**
 * Navigates to the [CompatDestination.Task.Edit] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToTaskEdit(
    taskId: String,
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Task.Edit(taskId), builder)
