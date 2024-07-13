package com.edricchan.studybuddy.navigation.compat.about

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.edricchan.studybuddy.core.compat.navigation.CompatDestination
import com.edricchan.studybuddy.navigation.compat.DefaultNavOptionsBuilder

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
 * Navigates to the [CompatDestination.About.AppAbout] route.
 *
 * By default, [NavOptionsBuilder.launchSingleTop] is set to `true`. If this
 * behaviour is undesired, [builder] should be specified.
 */
fun NavController.navigateToAbout(
    builder: NavOptionsBuilder.() -> Unit = DefaultNavOptionsBuilder
) = navigate(CompatDestination.About.AppAbout, builder)
