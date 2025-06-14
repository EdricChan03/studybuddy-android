package com.edricchan.studybuddy.core.compat.navigation.about

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.core.compat.navigation.DefaultNavOptionsBuilder

/**
 * Navigates to the [CompatDestination.About.ViewLicenses] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToLicenses(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.About.ViewLicenses, builder)

/**
 * Navigates to the [CompatDestination.About.SystemAppInfo] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToSystemAppInfo(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.About.SystemAppInfo, builder)

/**
 * Navigates to the [CompatDestination.About.AppAbout] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToAbout(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.About.AppAbout, builder)
