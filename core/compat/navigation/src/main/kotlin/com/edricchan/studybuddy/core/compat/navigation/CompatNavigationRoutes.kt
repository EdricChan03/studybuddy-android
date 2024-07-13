package com.edricchan.studybuddy.core.compat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

val DefaultNavOptionsBuilder: NavOptionsBuilder.() -> Unit = {
    launchSingleTop = true
}

/**
 * Navigates to the [CompatDestination.Debug] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToDebug(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Debug, builder)

/**
 * Navigates to the [CompatDestination.Updates] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToUpdates(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Updates, builder)

/**
 * Navigates to the [CompatDestination.Settings] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToSettings(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Settings, builder)

/**
 * Navigates to the [CompatDestination.Help] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToHelp(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Help, builder)

/**
 * Navigates to the [CompatDestination.Calendar] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToCalendar(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Calendar, builder)

/**
 * Navigates to the [CompatDestination.Tips] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToTips(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Tips, builder)
