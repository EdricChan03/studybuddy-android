package com.edricchan.studybuddy.exts.androidx.compose.material3

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset

object TextFieldAnimations {
    /**
     * Suitable transition spec to be used for the `supportingText` slot in a
     * Material3 text-field.
     * @param effectsSpec Animation specification for the effects (used for
     * [fadeIn] and [fadeOut]).
     * @param spatialSpec Animation specification for the effects (used for
     * [slideInVertically] and [slideOutVertically]).
     */
    @Composable
    fun <S> supportingTextTransitionSpec(
        effectsSpec: FiniteAnimationSpec<Float> = MaterialTheme.motionScheme.fastEffectsSpec(),
        spatialSpec: FiniteAnimationSpec<IntOffset> = MaterialTheme.motionScheme.fastSpatialSpec()
    ): AnimatedContentTransitionScope<S>.() -> ContentTransform = {
        fadeIn(effectsSpec) + slideInVertically(spatialSpec) togetherWith
            fadeOut(effectsSpec) + slideOutVertically(spatialSpec) using
            // This is so that the error message doesn't get clipped horizontally
            SizeTransform(clip = false)
    }
}
