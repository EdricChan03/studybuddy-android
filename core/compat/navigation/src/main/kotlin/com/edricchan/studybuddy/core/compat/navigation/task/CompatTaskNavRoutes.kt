package com.edricchan.studybuddy.core.compat.navigation.task

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.DefaultNavOptionsBuilder
import com.edricchan.studybuddy.features.tasks.navigation.navigateToCreateTask
import com.edricchan.studybuddy.features.tasks.navigation.navigateToEditTask
import com.edricchan.studybuddy.features.tasks.navigation.navigateToTaskGraph
import com.edricchan.studybuddy.features.tasks.navigation.navigateToTasksList
import com.edricchan.studybuddy.features.tasks.navigation.navigateToViewTask

/**
 * Navigates to the [CompatDestination.Task.Root] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
@Deprecated(
    "Use navigateToTaskGraph from the tasks feature module", ReplaceWith(
        "this.navigateToTaskGraph(builder = builder)",
        "com.edricchan.studybuddy.features.tasks.navigation.navigateToTaskGraph"
    )
)
fun NavController.navigateToTaskRoot(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigateToTaskGraph(builder = builder)

/**
 * Navigates to the [CompatDestination.Task.List] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
@Deprecated(
    "Use navigateToTasksList from the tasks feature module",
    ReplaceWith(
        "this.navigateToTasksList(builder = builder)",
        "com.edricchan.studybuddy.features.tasks.navigation.navigateToTasksList"
    )
)
fun NavController.navigateToTasksList(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigateToTasksList(builder = builder)

/**
 * Navigates to the [CompatDestination.Task.New] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
@Deprecated(
    "Use navigateToCreateTask from the tasks feature module",
    ReplaceWith(
        "this.navigateToCreateTask(builder = builder)",
        "com.edricchan.studybuddy.features.tasks.navigation.navigateToCreateTask"
    )
)
fun NavController.navigateToCreateTask(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigateToCreateTask(builder = builder)

/**
 * Navigates to the [CompatDestination.Task.View] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
@Deprecated(
    "Use navigateToViewTask from the tasks feature module",
    ReplaceWith(
        "this.navigateToViewTask(taskId = taskId, builder = builder)",
        "com.edricchan.studybuddy.features.tasks.navigation.navigateToViewTask"
    )
)
fun NavController.navigateToTaskView(
    taskId: String,
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigateToViewTask(taskId = taskId, builder = builder)

/**
 * Navigates to the [CompatDestination.Task.Edit] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
@Deprecated(
    "Use navigateToEditTask from the tasks feature module",
    ReplaceWith(
        "this.navigateToEditTask(taskId = taskId, builder = builder)",
        "com.edricchan.studybuddy.features.tasks.navigation.navigateToEditTask"
    )
)
fun NavController.navigateToTaskEdit(
    taskId: String,
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigateToEditTask(taskId = taskId, builder = builder)
