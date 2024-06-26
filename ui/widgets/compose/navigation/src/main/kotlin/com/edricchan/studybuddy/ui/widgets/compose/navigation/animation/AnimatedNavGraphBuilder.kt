package com.edricchan.studybuddy.ui.widgets.compose.navigation.animation

// Based from
// https://cs.android.com/android/platform/superproject/main/+/main:frameworks/base/packages/SettingsLib/Spa/spa/src/com/android/settingslib/spa/framework/compose/AnimatedNavGraphBuilder.kt;l=1

/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlin.reflect.KType

/**
 * Defines a composable destination in the receiver, with default enter/exit
 * sliding transitions as per the
 * [Material Design guidelines](https://m3.material.io/styles/motion/transitions/transition-patterns#df9c7d76-1454-47f3-ad1c-268a31f58bad).
 * @param route Route for the destination
 * @param arguments List of arguments to associate with destination
 * @param deepLinks List of deep links to associate with the destinations
 * @param content Composable for the destination
 * @see composable
 */
fun NavGraphBuilder.horizontalSlideComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = HorizontalSlideAnimations.EnterTransition,
    exitTransition = HorizontalSlideAnimations.ExitTransition,
    popEnterTransition = HorizontalSlideAnimations.PopEnterTransition,
    popExitTransition = HorizontalSlideAnimations.PopExitTransition,
    content = content,
)

/**
 * Defines a composable destination in the receiver, with default enter/exit
 * sliding transitions as per the
 * [Material Design guidelines](https://m3.material.io/styles/motion/transitions/transition-patterns#df9c7d76-1454-47f3-ad1c-268a31f58bad).
 * @param T Route from a KClass for the destination
 * @param typeMap Map of destination arguments' kotlin type [KType] to its respective
 * custom [NavType]. May be empty if [T] does not use custom [NavType]s.
 * @param deepLinks List of deep links to associate with the destinations
 * @param content Composable for the destination
 * @see composable
 */
inline fun <reified T : Any> NavGraphBuilder.horizontalSlideComposable(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) = composable<T>(
    typeMap = typeMap,
    deepLinks = deepLinks,
    enterTransition = HorizontalSlideAnimations.EnterTransition,
    exitTransition = HorizontalSlideAnimations.ExitTransition,
    popEnterTransition = HorizontalSlideAnimations.PopEnterTransition,
    popExitTransition = HorizontalSlideAnimations.PopExitTransition,
    content = content,
)

private const val FADE_OUT_MILLIS = 75
private const val FADE_IN_MILLIS = 300

private typealias EnterTransitionFn = AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition
private typealias ExitTransitionFn = AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition

object HorizontalSlideAnimations {
    val EnterTransition: EnterTransitionFn = {
        slideIntoContainer(
            towards = SlideDirection.Start,
            animationSpec = SlideInSpec,
            initialOffset = OffsetFn,
        ) + fadeIn(animationSpec = FadeInSpec)
    }

    val ExitTransition: ExitTransitionFn = {
        slideOutOfContainer(
            towards = SlideDirection.Start,
            animationSpec = SlideOutSpec,
            targetOffset = OffsetFn,
        ) + fadeOut(animationSpec = FadeOutSpec)
    }

    val PopEnterTransition: EnterTransitionFn = {
        slideIntoContainer(
            towards = SlideDirection.End,
            animationSpec = SlideInSpec,
            initialOffset = OffsetFn,
        ) + fadeIn(animationSpec = FadeInSpec)
    }

    val PopExitTransition: ExitTransitionFn = {
        slideOutOfContainer(
            towards = SlideDirection.End,
            animationSpec = SlideOutSpec,
            targetOffset = OffsetFn,
        ) + fadeOut(animationSpec = FadeOutSpec)
    }

    val SlideInSpec = tween<IntOffset>(
        durationMillis = FADE_IN_MILLIS,
        delayMillis = FADE_OUT_MILLIS,
        easing = LinearOutSlowInEasing,
    )
    val SlideOutSpec = tween<IntOffset>(durationMillis = FADE_IN_MILLIS)
    val FadeOutSpec = tween<Float>(
        durationMillis = FADE_OUT_MILLIS,
        easing = FastOutLinearInEasing,
    )
    val FadeInSpec = tween<Float>(
        durationMillis = FADE_IN_MILLIS,
        delayMillis = FADE_OUT_MILLIS,
        easing = LinearOutSlowInEasing,
    )
    val OffsetFn: (offsetForFullSlide: Int) -> Int = { it / 5 }
}
