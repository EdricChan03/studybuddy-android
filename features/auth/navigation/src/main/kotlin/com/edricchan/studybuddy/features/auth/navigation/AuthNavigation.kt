package com.edricchan.studybuddy.features.auth.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

val DefaultNavOptionsBuilder: NavOptionsBuilder.() -> Unit = {
    launchSingleTop = true
}

/**
 * Navigates to the [AuthDestination.AuthGraphRoot] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToAuthGraph(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(AuthDestination.AuthGraphRoot, builder)

/**
 * Navigates to the [AuthDestination.AccountInfo] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToAccountInfo(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(AuthDestination.AccountInfo, builder)

/**
 * Navigates to the [AuthDestination.Login] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToLogin(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(AuthDestination.Login, builder)

/**
 * Navigates to the [AuthDestination.Register] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToRegister(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(AuthDestination.Register, builder)

/**
 * Navigates to the [AuthDestination.Recovery] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToRecovery(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(AuthDestination.Recovery, builder)
