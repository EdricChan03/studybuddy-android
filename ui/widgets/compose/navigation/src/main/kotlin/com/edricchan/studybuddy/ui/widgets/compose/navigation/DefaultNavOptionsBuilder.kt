package com.edricchan.studybuddy.ui.widgets.compose.navigation

import androidx.navigation.NavOptionsBuilder

/**
 * The default [NavOptionsBuilder] configuration to use, where
 * [NavOptionsBuilder.launchSingleTop] is set to `true`.
 */
val DefaultNavOptionsBuilder: NavOptionsBuilder.() -> Unit = {
    launchSingleTop = true
}
