package com.edricchan.studybuddy.navigation.compat.auth

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.edricchan.studybuddy.navigation.compat.CompatDestination
import com.edricchan.studybuddy.navigation.compat.DefaultNavOptionsBuilder

/**
 * Navigates to the [CompatDestination.Auth.Account] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToAccountInfo(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Auth.Account, builder)

/**
 * Navigates to the [CompatDestination.Auth.ResetPassword] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToResetPassword(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Auth.ResetPassword, builder)

/**
 * Navigates to the [CompatDestination.Auth.Login] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToLogin(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Auth.Login, builder)

/**
 * Navigates to the [CompatDestination.Auth.Register] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToRegister(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.Auth.Register, builder)
